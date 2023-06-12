package com.tovalue.me.ui.dashboard.myMoodRing

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.tovalue.me.databinding.FragmentMoodRingHistoryBinding
import com.tovalue.me.model.moodRingModels.MoodRingHistory
import com.tovalue.me.ui.dashboard.DashboardViewModel
import com.tovalue.me.ui.dashboard.navigation.NavigationActivity
import com.tovalue.me.util.*
import com.tovalue.me.util.extensions.animateVisibility
import com.tovalue.me.util.extensions.updateStatusBarBackgroundColor
import java.util.*

class MoodRingHistoryFragment : Fragment() {
	private var _binding: FragmentMoodRingHistoryBinding? = null
	private val binding get() = _binding!!
	private val viewModel: DashboardViewModel by activityViewModels()

	private lateinit var historyAdapter: MoodRingHistoryAdapter

	private var weekendDate = ""
	
	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		// Inflate the layout for this fragment
		_binding = FragmentMoodRingHistoryBinding.inflate(layoutInflater, container, false)
		return binding.root
	}

	override fun onResume() {
		super.onResume()
		callHistoryApi(weekendDate)
	}
	
	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		updateStatusBarBackgroundColor("#041F1B")
		historyAdapter = MoodRingHistoryAdapter {
			historyDetailId = it.id ?: return@MoodRingHistoryAdapter
			startActivity(NavigationActivity.createIntent(requireActivity(), Constants.MOOD_RING))
		}

		binding.recyclerHistory.apply {
			adapter = historyAdapter
		}

		binding.btnBack.setOnClickListener {
			requireActivity().onBackPressed()
		}

		viewModel.moodHistory.observe(viewLifecycleOwner) {
			checkHistoryData(it)
			historyAdapter.submitData(it)
			DialogUtils.hideDialog()
		}

		binding.selectDate.setOnClickListener {
			showDatePicker()
		}

		weekendDate = DateFormatterUtils.formatDateToString(Date(), DateFormat.dateFormat2) ?: ""
		if (weekendDate.isNotEmpty()) {
			val date = DateFormatterUtils.formatDateFromString(weekendDate, DateFormat.dateFormat2) ?: Date()
			val dateString = DateFormatterUtils.formatDateToString(date, DateFormat.dateFormat3)
			binding.selectDate.text = dateString
		}
	}

	private fun callHistoryApi(withDate: String) {
		DialogUtils.showDialog(requireContext(), true)
		viewModel.getMoodRingHistory(withDate)
	}

	private fun showDatePicker() {
		val calender = Calendar.getInstance()
		calender.time = DateFormatterUtils.formatDateFromString(weekendDate, DateFormat.dateFormat2) ?: Date()
		DatePickerDialogFragment({ year, month, day ->
			weekendDate = "${year}-${(if (month < 10) "0$month" else month)}-${(if (day < 10) "0$day" else day)}"
			val date = DateFormatterUtils.formatDateFromString(weekendDate, DateFormat.dateFormat2) ?: return@DatePickerDialogFragment
			binding.selectDate.text = DateFormatterUtils.formatDateToString(date, DateFormat.dateFormat3)
			callHistoryApi(weekendDate)
		}, calender.get(Calendar.DAY_OF_MONTH), calender.get(Calendar.MONTH), calender.get(Calendar.YEAR)
		).show(childFragmentManager, "#DatePickerDialogFragment")
	}

	private fun checkHistoryData(list: List<MoodRingHistory>) {
		binding.emptyView.animateVisibility(if (list.isEmpty()) View.VISIBLE else View.GONE)
		binding.recyclerHistory.animateVisibility(if (list.isNotEmpty()) View.VISIBLE else View.GONE)
	}
	
	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}
}