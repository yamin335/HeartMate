package com.tovalue.me.ui.datingjourneyjournal

import android.app.Dialog
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.gson.Gson
import com.tovalue.me.R
import com.tovalue.me.adapter.DatingJourneyOurCalenderAdapter
import com.tovalue.me.adapter.DatingOurCalendarGroupAdapter
import com.tovalue.me.common.showToast
import com.tovalue.me.customviews.calendarview.data.Day
import com.tovalue.me.customviews.calendarview.widget.CollapsibleCalendar
import com.tovalue.me.databinding.FragmentDatingPartnerOurCalenderBinding
import com.tovalue.me.dialog.DateNightCatalogEventOfferDialog
import com.tovalue.me.dialog.DateNightCatalogEventOfferNewDialog
import com.tovalue.me.model.SendOfferDateNight
import com.tovalue.me.model.datingJourney.Groups
import com.tovalue.me.model.datingJourney.Journey
import com.tovalue.me.model.datingJourney.JourneyHomeResponse
import com.tovalue.me.model.datingJourney.Partners
import com.tovalue.me.ui.dashboard.upcomingplans.UpcomingPlanDetailFragment
import com.tovalue.me.ui.datenightcatalog.DateNightCatalogIdeaDetailsFragment
import com.tovalue.me.ui.datenightcatalog.viewmodel.DateNightCatalogViewModel
import com.tovalue.me.ui.datingjourneyjournal.viewmodel.DatingPartnerOurCalenderViewModel
import com.tovalue.me.util.Constants
import com.tovalue.me.util.DialogUtils
import com.tovalue.me.util.extensions.replaceFragmentSafely

class DatingPartnerOurCalenderFragment : Fragment() {

    private var _binding: FragmentDatingPartnerOurCalenderBinding? = null
    private val binding get() = _binding!!
    private val mViewModel: DatingPartnerOurCalenderViewModel by activityViewModels()
    private var args: Bundle? = null
    private var datingData: JourneyHomeResponse? = null
    private var groupIds: String = ""
    private var toDate: String = ""
    private var fromDate: String = ""
    private lateinit var groupList: ArrayList<String>

    private lateinit var adapter: DatingJourneyOurCalenderAdapter
    private lateinit var groupAdapter: DatingOurCalendarGroupAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDatingPartnerOurCalenderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        args = arguments
        groupList = arrayListOf()


        setRecyclerview()
        setUpObserver()


        binding.calendarView.setCalendarListener(object : CollapsibleCalendar.CalendarListener {
            override fun onDaySelect() {
                val day: Day = binding.calendarView.selectedDay!!
                fromDate = "${day.day}-${day.month.plus(1)}-${day.year}"
                toDate = "${day.day}-${day.month.plus(1)}-${day.year}"
            }

            override fun onItemClick(view: View) {}
            override fun onDataUpdate() {}
            override fun onMonthChange() {}
            override fun onWeekChange(i: Int) {}
            override fun onClickListener() {}
            override fun onDayChanged() {}
        })

        binding.doneBtn.setOnClickListener {
            groupAdapter.getListData().filter { it.isChecked }.forEach {
                groupList.add(it.groupID.toString())
            }
            groupIds = TextUtils.join(",", groupList)
            makeApiCall()
        }

        binding.ourCalemdrtRv.setOnChildClickListener { parent, v, groupPosition, childPosition, id ->
            val plan = adapter.getChild(groupPosition, childPosition)
            replaceFragmentSafely(
                containerViewId = R.id.fragment_container,
                fragment = UpcomingPlanDetailFragment.newInstance(
                    plan.id!!,
                    plan.firstName,
                    plan.type,
                    plan.groupId ?: -1
                ),
                addToStack = true
            )
            true
        }

        binding.ivCreateDatingDialog.setOnClickListener {
            openEventDialog()
        }

        binding.backBtn.setOnClickListener {
            requireActivity().onBackPressed()
        }

        makeApiCall()

    }

    private fun makeApiCall() {
        DialogUtils.showDialog(requireActivity(), false)
        mViewModel!!.getMasterPlannerForCalender(fromDate, toDate, groupIds)
    }

    private fun setUpObserver() {
        mViewModel?.datingPartnerOurCalenderState!!.observe(viewLifecycleOwner) {
            when (it) {
                is DatingPartnerOurCalenderViewModel.DatingPartnerOurCalender.Error -> {
                    DialogUtils.hideDialog()
                    it.errorMsg?.let { it1 -> showToast(it1) }
                }
                is DatingPartnerOurCalenderViewModel.DatingPartnerOurCalender.DataResponse -> {
                    DialogUtils.hideDialog()

                    if (it.response.journey_details.size > 0) {
                        binding.toolTitle.visibility=View.VISIBLE
                        binding.tvNames.visibility=View.VISIBLE

                        binding.toolTitle.text =
                            it.response.journey_details.get(0).total_weeks ?: ""
                        binding.tvNames.text = it.response.journey_details.get(0).names ?: ""
                    }else{
                        binding.toolTitle.visibility=View.INVISIBLE
                        binding.tvNames.visibility=View.GONE
                    }
                    setGroupAdapter(it.response.groups)
                    toggleVisibility(it.response.partners)
                    groupList.clear()
                }
            }
        }
    }

    private fun setRecyclerview() {
        adapter = DatingJourneyOurCalenderAdapter(requireContext())
        binding.ourCalemdrtRv.setAdapter(adapter)

        groupAdapter = DatingOurCalendarGroupAdapter(requireContext())
        binding.datingOurCalendarGrpRv.adapter = groupAdapter
    }

    private fun toggleVisibility(partners: ArrayList<Partners>) {
        if (partners.isNotEmpty()) {
            binding.noUpcomingPlansTv.visibility = View.GONE
            binding.ourCalemdrtRv.visibility = View.VISIBLE
            val planDetailList = partners.groupBy { it.timeOfDay }
            val groupTitleList = planDetailList.keys.toList()
            adapter.setData(planDetailList, groupTitleList)
        } else {
            binding.noUpcomingPlansTv.visibility = View.VISIBLE
            binding.ourCalemdrtRv.visibility = View.GONE
        }

    }

    private fun setGroupAdapter(groups: ArrayList<Groups>) {
        groups.forEach { group ->
            group.isChecked = groupList.any { it.toInt() == group.groupID }
        }
        groupAdapter.setData(groups)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun openEventDialog() {
        val dialog = Dialog(requireActivity())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.open_create_event_dialog)
        dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent);
        val selectBtn = dialog.findViewById(R.id.tvSelectDateNight) as TextView
        val createBtn = dialog.findViewById(R.id.tvCreateDateNight) as TextView
        selectBtn.setOnClickListener {
            dialog.dismiss()
        }
        createBtn.setOnClickListener {
            dialog.dismiss()
            val dialog = DateNightCatalogEventOfferNewDialog() {

//                  it.dateNightId = dataDetailResponse?.date_night_code ?: ""
                     it.dateWeekId = datingData?.homeJourney!![0].week_id ?: 0
                     it.groupId = datingData?.homeJourney!![0].group_id
                     it.partnerUserId = datingData!!.homeJourney[0].partner_id
                     it.inventoryTopic = (datingData!!.homeJourney[0].topic_id?:"").toString()

                  sendDateNightOffer(it)

            }

            dialog.show(
                parentFragmentManager,
                DateNightCatalogIdeaDetailsFragment::class.java.simpleName
            )
        }
        dialog.show()
        val window: Window = dialog.window!!
        window.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
    }

    private fun sendDateNightOffer(sendOfferDataNight: SendOfferDateNight) {

        DialogUtils.showDialog(requireContext(), false)
        mViewModel.sendDateNightOffer(sendOfferDataNight)

        mViewModel.getDateNightCatalogOfferResponseLiveData()
            .observe(viewLifecycleOwner) {
                when (it) {
                    is DatingPartnerOurCalenderViewModel.UIEventSendDateNightOffer.Data -> {
                        DialogUtils.hideDialog()
                        Toast.makeText(
                            requireContext(),
                            "Successfully send Date Night Offer your date night offer id is ${it.data.date_offer_id}",
                            Toast.LENGTH_SHORT
                        ).show()
                        requireActivity().supportFragmentManager.popBackStack();
                    }
                    is DatingPartnerOurCalenderViewModel.UIEventSendDateNightOffer.Error -> {
                        DialogUtils.hideDialog()
                        showToast(it.errorMsg.toString())
                    }
                }


            }


    }

    companion object {

        fun newInstance(
            param1: String,
        ): DatingPartnerOurCalenderFragment {
            val fragment = DatingPartnerOurCalenderFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)

            fragment.arguments = args
            return fragment
        }

        private const val ARG_PARAM1 = "ARG_PARAM1"

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            datingData = Gson().fromJson(arguments?.getString(ARG_PARAM1) ?: "",JourneyHomeResponse::class.java)
        }
    }



}