package com.tovalue.me.ui.dashboard.upcomingplans

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.tovalue.me.R
import com.tovalue.me.common.showToast
import com.tovalue.me.databinding.FragmentUpcomingPlanDeailBinding
import com.tovalue.me.ui.dashboard.upcomingplans.dialog.CancelEventDialogFragment
import com.tovalue.me.ui.dashboard.upcomingplans.dialog.RescheduleEventOrOfferDialog
import com.tovalue.me.ui.dashboard.upcomingplans.models.DateNightEvent
import com.tovalue.me.ui.dashboard.upcomingplans.models.DateNightOffer
import com.tovalue.me.ui.dashboard.upcomingplans.models.UpcomingPlanDetailResponse
import com.tovalue.me.ui.dashboard.upcomingplans.viewmodel.UpcomingPlansViewModel
import com.tovalue.me.util.Constants
import com.tovalue.me.util.Constants.CANCEL_EVENT_DIALOG_FRAGMENT
import com.tovalue.me.util.Constants.RESCHEDULE_EVENT_OR_DIALOG_FRAGMENT
import com.tovalue.me.util.DialogUtils
import com.tovalue.me.util.extensions.clearBackStack


class UpcomingPlanDetailFragment : Fragment() {

//    private var isBtnPressed = false

    private val upcomingPlansViewModel: UpcomingPlansViewModel by activityViewModels()

    private var _binding: FragmentUpcomingPlanDeailBinding? = null
    private val binding get() = _binding!!

    private var eventId: Int = 0
    private var firstName: String = ""
    private var planType: String = ""
    private var groupId: Int = -1

    private var upcomingPlanDetailResponse: UpcomingPlanDetailResponse? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            eventId = arguments?.getInt(ARG_PARAM2) ?: 0
            firstName = arguments?.getString(ARG_PARAM3) ?: ""
            planType = arguments?.getString(ARG_PARAM4) ?: ""
            groupId = arguments?.getInt(ARG_PARAM5) ?: 0
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUpcomingPlanDeailBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.backBtn.setOnClickListener {
            if (requireActivity().supportFragmentManager.backStackEntryCount == 0) {
                requireActivity().onBackPressed()
            } else
                clearBackStack()
        }

        setUpObserver()

        setUpViewAndApiCall()
    }

    private fun setUpViewAndApiCall() {
        DialogUtils.showDialog(requireActivity(), false)
        if (planType.equals(
                Constants.PLAN_TYPE_DATE_NIGHT_OFFER,
                true
            ) /*or planType.equals(Constants.PLAN_TYPE_RESCHEDULE_OFFER, true)*/
        ) {
            binding.offerBtnLayout.visibility = View.VISIBLE
            binding.eventBtnLayout.visibility = View.GONE
            upcomingPlansViewModel.getDateNightOffer(eventId)
            binding.declineTv.visibility = View.VISIBLE
        } else if (planType.equals(Constants.PLAN_TYPE_RESCHEDULE_OFFER, true)) {
//            if (groupId == 0) {
//                binding.declineTv.visibility = View.INVISIBLE
//            } else {
//                binding.declineTv.visibility = View.VISIBLE
//            }
            binding.declineTv.visibility = View.VISIBLE
            binding.offerBtnLayout.visibility = View.VISIBLE
            binding.eventBtnLayout.visibility = View.GONE
            upcomingPlansViewModel.getReScheduledEventDetail(eventId)
        } else {
            binding.offerBtnLayout.visibility = View.GONE
            binding.eventBtnLayout.visibility = View.VISIBLE
            upcomingPlansViewModel.getEventDetail(eventId)
            binding.declineTv.visibility = View.INVISIBLE
        }

        binding.rescheduleEventBtn.setOnClickListener {
            if (planType.equals(Constants.PLAN_TYPE_ME_TIME, true)) {
                if (groupId == 0) {
                    openRescheduleEventDialog()
                } else {
                    showToast("You cannot cancel this Activity")
                }
            } else
                openRescheduleEventDialog()
        }

        binding.rescheduleOfferBtn.setOnClickListener {
            openRescheduleEventDialog()
        }

        binding.declineTv.setOnClickListener {
            //isBtnPressed = true
            DialogUtils.showDialog(requireContext(), false)
            if (planType.equals(Constants.PLAN_TYPE_RESCHEDULE_OFFER, true)) {
                openCancelReasonDialog()
            } else
                upcomingPlansViewModel.submitDateNightOfferDecision(eventId, 2)
        }

        binding.acceptOfferBtn.setOnClickListener {
            if (planType.equals(Constants.PLAN_TYPE_RESCHEDULE_OFFER, true)) {
                DialogUtils.showDialog(requireContext(), false)
                upcomingPlansViewModel.acceptRescheduleDateNightEvent(
                    upcomingPlanDetailResponse?.dateNightEvent?.parentEventId!!.toInt(),
                    upcomingPlanDetailResponse?.dateNightEvent?.rescheduleOfferId!!.toInt(),
                    upcomingPlanDetailResponse?.dateNightEvent?.eventStartDate!!,
                    upcomingPlanDetailResponse?.dateNightEvent?.eventStartTime!!
                )
            } else {
                DialogUtils.showDialog(requireContext(), false)
                upcomingPlansViewModel.submitDateNightOfferDecision(eventId, 1)
            }
        }

        binding.cancelButton.setOnClickListener {
         /*   if (planType.equals(Constants.PLAN_TYPE_ME_TIME, true)) {
                if (groupId == 0) {
                    //      isBtnPressed = true
                    DialogUtils.showDialog(requireContext(), false)
                    upcomingPlansViewModel.cancelMeTIme(eventId)
                } else
                    showToast("You cannot Cancel this Activity")
            } else {*/
                openCancelReasonDialog()
     //       }
        }

    }

    /*Need to make a separate class to load images*/
    private fun loadImage(imageUrl: String) {
        Glide.with(requireContext()).load(imageUrl)
            .error(R.drawable.attention)
            .into(binding.featureImg)
    }

    private fun setUpObserver() {

        upcomingPlansViewModel.acceptRescheduleDateNightEvent.observe(viewLifecycleOwner) {
            when (it) {
                is UpcomingPlansViewModel.AcceptRescheduleDateNightEvent.Error -> {
                    DialogUtils.hideDialog()
                    it.errorMsg?.let { it1 -> showToast(it1) }
                }
                is UpcomingPlansViewModel.AcceptRescheduleDateNightEvent.DataResponse -> {
                    DialogUtils.hideDialog()
                    showToast(it.response.planStatus.message.message!!)
                    clearBackStack()
                }
            }
        }


        upcomingPlansViewModel.upcomingPlanDetail.observe(viewLifecycleOwner) {
            when (it) {
                is UpcomingPlansViewModel.UpcomingPlanEventDetailEvent.Error -> {
                    DialogUtils.hideDialog()
                    it.errorMsg?.let { it1 -> showToast(it1) }
                }
                is UpcomingPlansViewModel.UpcomingPlanEventDetailEvent.DataResponse -> {
                    DialogUtils.hideDialog()
                    upcomingPlanDetailResponse = it.response
                    if (it.response.dateNightOffer != null) {
                        setDataToViewForOffer(it.response.dateNightOffer!!)
                    } else
                        setDataToView(it.response.dateNightEvent!!)
                }
            }
        }


        upcomingPlansViewModel.cancelMeTimeState.observe(viewLifecycleOwner) {
//            if (isBtnPressed) {
            when (it) {
                is UpcomingPlansViewModel.CancelMeTimeEvent.Error -> {
                    DialogUtils.hideDialog()
                    it.errorMsg?.let { msg -> showToast(msg) }
                }
                is UpcomingPlansViewModel.CancelMeTimeEvent.DataResponse -> {
                    DialogUtils.hideDialog()
                    showToast(it.response.planStatus)
                    clearBackStack()
                }
                //}
                // isBtnPressed = false
            }
        }

        upcomingPlansViewModel.submitDateNightOfferDecisionState.observe(viewLifecycleOwner) { offerDecision ->
            // if (isBtnPressed) {
            when (offerDecision) {
                is UpcomingPlansViewModel.SubmitDateNightOfferDecision.DataResponse -> {
                    showToast(offerDecision.response.offerStatus.message)
                    DialogUtils.hideDialog()
                    clearBackStack()
                }
                is UpcomingPlansViewModel.SubmitDateNightOfferDecision.Error -> {
                    DialogUtils.hideDialog()
                    offerDecision.errorMsg?.let { msg -> showToast(msg) }
                }
//                }
//                isBtnPressed = false
            }
        }
    }

    private fun setDataToView(dateNightEvent: DateNightEvent) {


        binding.startTimeTv.text = dateNightEvent.eventStartTime
        if (dateNightEvent.eventEndTime.isNullOrEmpty().not()) {
            binding.endTimeTv.text = dateNightEvent.eventEndTime
        }
        binding.planTv.text = dateNightEvent.eventDescription
        binding.webLinkTv.text = dateNightEvent.eventWebsite
        binding.locationTv.text = dateNightEvent.event_address
        binding.titleTv.text = dateNightEvent.eventTitle?.plus(" w/ $firstName")
        binding.daysTv.text = dateNightEvent.daysUntil
        binding.createdByTv.text = dateNightEvent.createdby
        binding.subTitleTv.text = dateNightEvent.datesSoFar
        loadImage(dateNightEvent.featuredImage ?: "")
    }

    private fun setDataToViewForOffer(dateNightOffer: DateNightOffer) {
        if (dateNightOffer.isSender == 1) {
            binding.acceptOfferBtn.visibility = View.GONE
            binding.rescheduleOfferBtn.layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
        }

        binding.startTimeTv.text = dateNightOffer.eventStartTime
        if (dateNightOffer.eventEndTime.isNullOrEmpty().not()) {
            binding.endTimeTv.text = dateNightOffer.eventEndTime
        }
        binding.planTv.text = dateNightOffer.dateNightDescription
        binding.createdByTv.text = dateNightOffer.createdby
        binding.daysTv.text = dateNightOffer.daysUntil
        binding.subTitleTv.text = dateNightOffer.datesSoFar
        binding.locationTv.text = dateNightOffer.location
        binding.titleTv.text = dateNightOffer.dateNightTitle?.plus(" w/ $firstName")
        loadImage(dateNightOffer.featuredImage ?: "")
    }


    private fun openRescheduleEventDialog() {
        if (upcomingPlanDetailResponse != null) {
            val dialogFragment =
                RescheduleEventOrOfferDialog.newInstance(upcomingPlanDetailResponse!!, planType,-1)
            dialogFragment.isCancelable = true
            val ft: FragmentTransaction = childFragmentManager.beginTransaction()
            val prev: Fragment? =
                childFragmentManager.findFragmentByTag(RESCHEDULE_EVENT_OR_DIALOG_FRAGMENT)
            if (prev != null) {
                ft.remove(prev)
            }
            ft.addToBackStack(null)
            dialogFragment.show(ft, RESCHEDULE_EVENT_OR_DIALOG_FRAGMENT)
        } else
            showToast("Data is null")
    }

    private fun openCancelReasonDialog() {
        val idToPass = if (planType.equals(Constants.PLAN_TYPE_RESCHEDULE_OFFER, true)) {
            upcomingPlanDetailResponse?.dateNightEvent?.parentEventId!!.toInt()
        } else
            eventId

        val dialogFragment = CancelEventDialogFragment.newInstance(idToPass,planType,groupId)
        dialogFragment.isCancelable = true
        val ft: FragmentTransaction = childFragmentManager.beginTransaction()
        val prev: Fragment? =
            childFragmentManager.findFragmentByTag(CANCEL_EVENT_DIALOG_FRAGMENT)
        if (prev != null) {
            ft.remove(prev)
        }
        ft.addToBackStack(null)
        dialogFragment.show(ft, CANCEL_EVENT_DIALOG_FRAGMENT)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    companion object {

        fun newInstance(
            param2: Int,
            param3: String?,
            param4: String?,
            param5: Int
        ): UpcomingPlanDetailFragment {
            val fragment = UpcomingPlanDetailFragment()
            val args = Bundle()
            args.putInt(ARG_PARAM2, param2)
            args.putString(ARG_PARAM3, param3)
            args.putString(ARG_PARAM4, param4)
            args.putInt(ARG_PARAM5, param5)
            fragment.arguments = args
            return fragment
        }

        private const val ARG_PARAM1 = "ARG_PARAM1"
        private const val ARG_PARAM2 = "ARG_PARAM2"
        private const val ARG_PARAM3 = "ARG_PARAM3"
        private const val ARG_PARAM4 = "ARG_PARAM4"
        private const val ARG_PARAM5 = "ARG_PARAM5"
    }


}