package com.tovalue.me.ui.dashboard.upcomingplans

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tovalue.me.R
import com.tovalue.me.adapter.UpcomingPlansAdapter
import com.tovalue.me.adapter.UpcomingPlansGroupAdapter
import com.tovalue.me.common.showToast
import com.tovalue.me.customviews.calendarview.data.Day
import com.tovalue.me.customviews.calendarview.widget.CollapsibleCalendar.CalendarListener
import com.tovalue.me.databinding.FragmentUpcomingPlansBinding
import com.tovalue.me.model.upcomingplans.Groups
import com.tovalue.me.model.upcomingplans.Partners
import com.tovalue.me.ui.dashboard.upcomingplans.dialog.AddPersonalEventDialog
import com.tovalue.me.ui.dashboard.upcomingplans.viewmodel.UpcomingPlansViewModel
import com.tovalue.me.util.Constants.ADD_PERSONAL_EVENT_DIALOG_FRAGMENT
import com.tovalue.me.util.DialogUtils
import com.tovalue.me.util.Utils
import com.tovalue.me.util.extensions.animateVisibility
import com.tovalue.me.util.extensions.replaceFragmentSafely
import java.util.*


class UpcomingPlansFragment : Fragment() {


    private val upcomingPlansViewModel: UpcomingPlansViewModel by activityViewModels()

    private var _binding: FragmentUpcomingPlansBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: UpcomingPlansAdapter
    private lateinit var groupAdapter: UpcomingPlansGroupAdapter

    private var groupIds: String = ""
    private var toDate: String = ""
    private var fromDate: String = ""

    private var todayDate: String = ""

    private lateinit var groupList: ArrayList<String>

    private var currentVisibleItem = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUpcomingPlansBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        groupList = arrayListOf()
        if (arguments != null && requireArguments().containsKey(ARG_PARAM1)) {
            groupIds = arguments?.getString(ARG_PARAM2) ?: ""
            todayDate = arguments?.getString(ARG_PARAM1) ?: ""
            groupList.addAll(listOf(groupIds))
        }

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        setRecyclerview()
        setUpObserver()
        setListenerForGroupRv()

        binding.calendarView.setCalendarListener(object : CalendarListener {
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

        binding.calendarView.mTodayIcon.setOnClickListener {
            openAddPersonalEventDialog()
        }

        if (todayDate.isNotEmpty()) {
            val date = Utils.stringToDate(todayDate)
            val cal = Calendar.getInstance()
            cal.time = date
            val day = cal.get(Calendar.DAY_OF_MONTH)
            val month = cal.get(Calendar.MONTH)
            val year = cal.get(Calendar.YEAR)
            binding.calendarView.select(
                Day(
                    year, month, day
                )
            )
            fromDate = "${day}-${month.plus(1)}-${year}"
            toDate = "${day}-${month.plus(1)}-${year}"
        }


        binding.upcomingPlansRv.setOnChildClickListener { parent, v, groupPosition, childPosition, id ->
            val plan = adapter.getChild(groupPosition, childPosition)
            replaceFragmentSafely(
                containerViewId = R.id.fragment_container,
                fragment = UpcomingPlanDetailFragment.newInstance(
                    plan.id!!, plan.firstName, plan.type, plan.groupId ?: -1
                )
            )

            true
        }
        makeApiCall()
    }

    private fun setListenerForGroupRv() {
        var programmaticallyScrolled = false
        binding.upcomingPlansGrpRv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                when (newState) {
                    RecyclerView.SCROLL_STATE_DRAGGING ->                     //Indicated that user scrolled.
                        programmaticallyScrolled = false
                    RecyclerView.SCROLL_STATE_IDLE -> if (!programmaticallyScrolled) {
                        val layoutManager =
                            binding.upcomingPlansGrpRv.layoutManager as LinearLayoutManager
                        currentVisibleItem = layoutManager.findFirstCompletelyVisibleItemPosition()
                        handleWritingViewNavigationArrows(false)
                    }
                    else -> {}
                }
            }
        })
    }

    /**
     * Handles the arrows visibility based on current visible items and scrolls the
     * current visibility item based on param scroll.
     *
     * Scroll - is False if User scrolls the Reycler Manually
     * - is True means User used arrows to navigate
     *
     * @param scroll
     */
    private fun handleWritingViewNavigationArrows(scroll: Boolean) {
        if (currentVisibleItem == binding.upcomingPlansGrpRv.adapter?.itemCount?.minus(2) &&
            binding.upcomingPlansGrpRv.adapter?.itemCount!! > 3
        ) {
            binding.rightArrow.visibility = View.GONE
            binding.leftArrow.visibility = View.VISIBLE
        } else if (currentVisibleItem != 0) {
            binding.rightArrow.visibility = View.VISIBLE
            binding.leftArrow.visibility = View.VISIBLE
        } else if (currentVisibleItem == 0 && binding.upcomingPlansGrpRv.adapter?.itemCount!! > 3) {
            binding.leftArrow.visibility = View.GONE
            binding.rightArrow.visibility = View.VISIBLE
        }
        if (scroll) {
            binding.upcomingPlansGrpRv.smoothScrollToPosition(currentVisibleItem)
        }
    }

    private fun makeApiCall() {
        DialogUtils.showDialog(requireActivity(), false)
        upcomingPlansViewModel.getMasterPlanner(fromDate, toDate, groupIds)
    }

    private fun setUpObserver() {
        upcomingPlansViewModel.upcomingPlansState.observe(viewLifecycleOwner) {
            when (it) {
                is UpcomingPlansViewModel.UpcomingPlansEvent.Error -> {
                    DialogUtils.hideDialog()
                    it.errorMsg?.let { it1 -> showToast(it1) }
                }
                is UpcomingPlansViewModel.UpcomingPlansEvent.DataResponse -> {
                    DialogUtils.hideDialog()
                    setGroupAdapter(it.response.groups)
                    toggleVisibility(it.response.partners)
                    groupList.clear()
                }
            }
        }
    }

    private fun setGroupAdapter(groups: ArrayList<Groups>) {
        groups.forEach { group ->
            group.isChecked = groupList.any { it.toInt() == group.groupID }
        }
        groupAdapter.setData(groups)
        if (groups.size > 3) {
            binding.rightArrow.animateVisibility(View.VISIBLE)
        }
    }


    private fun toggleVisibility(partners: ArrayList<Partners>) {
        if (partners.isNotEmpty()) {
            binding.noUpcomingPlansTv.visibility = View.GONE
            binding.upcomingPlansRv.visibility = View.VISIBLE
            val planDetailList = partners.groupBy { it.timeOfDay }
            val groupTitleList = planDetailList.keys.toList()
            adapter.setData(planDetailList, groupTitleList)
        } else {
            binding.noUpcomingPlansTv.visibility = View.VISIBLE
            binding.upcomingPlansRv.visibility = View.GONE
        }

    }

    private fun openAddPersonalEventDialog() {
        val dialogFragment = AddPersonalEventDialog()
        dialogFragment.isCancelable = true
        val ft: FragmentTransaction = childFragmentManager.beginTransaction()
        val prev: Fragment? =
            childFragmentManager.findFragmentByTag(ADD_PERSONAL_EVENT_DIALOG_FRAGMENT)
        if (prev != null) {
            ft.remove(prev)
        }
        ft.addToBackStack(null)
        dialogFragment.show(ft, ADD_PERSONAL_EVENT_DIALOG_FRAGMENT)
    }

    /* Set RecyclerView*/
    private fun setRecyclerview() {
        adapter = UpcomingPlansAdapter(requireContext())
        binding.upcomingPlansRv.setAdapter(adapter)

        groupAdapter = UpcomingPlansGroupAdapter(requireContext())
        binding.upcomingPlansGrpRv.adapter = groupAdapter

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {

        fun newInstance(
            time: String? = null,
            groupId: String? = null,
        ): UpcomingPlansFragment {
            val fragment = UpcomingPlansFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, time)
            args.putString(ARG_PARAM2, groupId)
            fragment.arguments = args
            return fragment
        }

        private const val ARG_PARAM1 = "ARG_PARAM1" //for time
        private const val ARG_PARAM2 = "ARG_PARAM2"  //for date
    }

}