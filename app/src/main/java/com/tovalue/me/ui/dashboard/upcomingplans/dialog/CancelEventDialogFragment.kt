package com.tovalue.me.ui.dashboard.upcomingplans.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.tovalue.me.R
import com.tovalue.me.adapter.CancelReasonsAdapter
import com.tovalue.me.common.showToast
import com.tovalue.me.databinding.DialogFragmentCancelEventBinding
import com.tovalue.me.ui.dashboard.upcomingplans.viewmodel.UpcomingPlansViewModel
import com.tovalue.me.util.Constants
import com.tovalue.me.util.DialogUtils
import com.tovalue.me.util.extensions.clearBackStack

class CancelEventDialogFragment : DialogFragment() {


    private val upcomingPlansViewModel: UpcomingPlansViewModel by activityViewModels()

    private var eventId: Int = 0
    private var groupId: Int = 0
    private var planType: String = ""

    private var _binding: DialogFragmentCancelEventBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: CancelReasonsAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogFragmentCancelEventBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = CancelReasonsAdapter(requireContext()) {
            DialogUtils.showDialog(requireContext(), false)
            if (planType.equals(Constants.PLAN_TYPE_ME_TIME, true)) {
                if (groupId == 0) {
                    //      isBtnPressed = true
                    DialogUtils.showDialog(requireContext(), false)
                    upcomingPlansViewModel.cancelMeTIme(eventId)
                } else
                    showToast("You cannot Cancel this Activity")

            } else {
                upcomingPlansViewModel.cancelDateNightEvent(eventId, it)
            }
        }
        binding.reasonsRv.adapter = adapter
        binding.reasonsRv.hasFixedSize()
        DialogUtils.showDialog(requireContext(), false)
        upcomingPlansViewModel.getCancelReason(eventId)

        binding.backBtn.setOnClickListener {
            dismiss()
        }

        setUpObserver()

    }


    private fun setUpObserver() {
        upcomingPlansViewModel.cancelReasonState.observe(viewLifecycleOwner) {
            when (it) {
                is UpcomingPlansViewModel.CancelEventReasonEvent.Error -> {
                    DialogUtils.hideDialog()
                    it.errorMsg?.let { it1 -> showToast(it1) }
                }
                is UpcomingPlansViewModel.CancelEventReasonEvent.DataResponse -> {
                    DialogUtils.hideDialog()
                    adapter.setData(it.response.cancelReasons)
                }
            }
        }
        upcomingPlansViewModel.cancelDateNightEvent.observe(viewLifecycleOwner) { res ->
            when (res) {
                is UpcomingPlansViewModel.CancelDateNightEvent.DataResponse -> {
                    val message = if (res.response.planStatus.dateNightEvent.isNullOrEmpty().not())
                        res.response.planStatus.dateNightEvent
                    else
                        res.response.planStatus.message
                    showToast(message)
                    DialogUtils.hideDialog()
                    dismiss()
                }
                is UpcomingPlansViewModel.CancelDateNightEvent.Error -> {
                    DialogUtils.hideDialog()
                    res.errorMsg?.let { it -> showToast(it) }
                    dismiss()
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
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.DialogStyle)

        if (arguments != null && requireArguments().containsKey(ARG_PARAM1)) {
            eventId = arguments?.getInt(ARG_PARAM1)!!
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {

        fun newInstance(id: Int, type: String, groupId: Int): CancelEventDialogFragment {
            val fragment = CancelEventDialogFragment()
            val args = Bundle()
            args.putInt(ARG_PARAM1, id)
            args.putString(ARG_PARAM2, type)
            args.putInt(ARG_PARAM3, groupId)
            fragment.arguments = args
            return fragment
        }

        private const val ARG_PARAM1 = "ARG_PARAM1"
        private const val ARG_PARAM2 = "ARG_PARAM2"
        private const val ARG_PARAM3 = "ARG_PARAM3"
    }


}