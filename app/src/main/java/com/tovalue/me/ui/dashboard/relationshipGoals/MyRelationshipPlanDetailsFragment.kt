package com.tovalue.me.ui.dashboard.relationshipGoals

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.tovalue.me.common.showToast
import com.tovalue.me.databinding.FragmentRelationshipGoalDetailsBinding
import com.tovalue.me.model.MyRelationshipPlanChecklistItem
import com.tovalue.me.model.MyRelationshipPlanDetails
import com.tovalue.me.ui.auth.AuthHostActivity
import com.tovalue.me.ui.dashboard.DashboardViewModel
import com.tovalue.me.util.*
import com.tovalue.me.util.extensions.updateStatusBarBackgroundColor
import com.tovalue.me.util.livedata.EventObserver
import java.util.*

class MyRelationshipPlanDetailsFragment : Fragment() {
	private var _binding: FragmentRelationshipGoalDetailsBinding? = null
	private val binding get() = _binding!!
	private val viewModel: DashboardViewModel by activityViewModels()
	private lateinit var relationshipIntervalListAdapter: RelationshipIntervalListAdapter
	
	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		// Inflate the layout for this fragment
		_binding = FragmentRelationshipGoalDetailsBinding.inflate(layoutInflater, container, false)
		return binding.root
	}

	override fun onResume() {
		super.onResume()
		DialogUtils.showDialog(requireContext(), true)
		viewModel.getMyRelationshipPlanDetails(planId)
	}
	
	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		updateStatusBarBackgroundColor("#EE43F1")

		binding.btnBack.setOnClickListener {
			requireActivity().onBackPressed()
		}

		binding.btnSave.setOnClickListener {
			updateCheckUncheckTasks()
		}

		binding.btnClear.setOnClickListener {
			for (interval in relationshipTasks) {
				for (task in interval.task) {
					task.isChecked = false
				}
			}
			relationshipIntervalListAdapter.notifyDataSetChanged()
		}

		viewModel.myPlanDetailResponse.observe(viewLifecycleOwner,
			EventObserver { DialogUtils.hideDialog()
				when (it) {
					is DashboardViewModel.UiEventMyRelationshipPlanDetails.MyRelationshipPlanDetailsData -> {
						it.response.plan_details?.let { data ->
							bindData(data)
						}

						it.response.checklist?.let { data ->
							relationshipTasks = data
							for (interval in relationshipTasks) {
								for (task in interval.task) {
									task.isChecked = task.status == "completed"
								}
							}
							relationshipIntervalListAdapter = RelationshipIntervalListAdapter(
								relationshipTasks
							)

							binding.recyclerInterval.adapter = relationshipIntervalListAdapter
						}
					}
					is DashboardViewModel.UiEventMyRelationshipPlanDetails.COOKIEXPIRE -> {
						viewModel.flushSavedData()
						goToLoginScreen()
					}
					is DashboardViewModel.UiEventMyRelationshipPlanDetails.Error -> {
						showToast(it.errorMsg)
					}
				}
			})

		viewModel.planUpdateResponse.observe(viewLifecycleOwner,
			EventObserver { DialogUtils.hideDialog()
				when (it) {
					is DashboardViewModel.UiEventDefaultResponse.DefaultResponseData -> {
						requireActivity().onBackPressed()
					}
					is DashboardViewModel.UiEventDefaultResponse.COOKIEXPIRE -> {
						viewModel.flushSavedData()
						goToLoginScreen()
					}
					is DashboardViewModel.UiEventDefaultResponse.Error -> {
						showToast(it.errorMsg)
					}
				}
			})
	}

	private fun updateCheckUncheckTasks() {
		var selectedIds = ""
		for (interval in relationshipTasks) {
			for (task in interval.task) {
				if (task.isChecked) {
					selectedIds = if (selectedIds.isEmpty()) task.task_id.toString() else "$selectedIds,${task.task_id}"
				}
			}
		}

		if (selectedIds.isEmpty()) {
			showToast("No task is selected")
		} else {
			DialogUtils.showDialog(requireContext(), true)
			viewModel.updateMyRelationshipPlanProgress(planId, selectedIds)
		}
	}

	private fun bindData(data: MyRelationshipPlanDetails) {
		binding.title.text = planId.toString()
		binding.title.text = if (data.title.isNullOrBlank()) "Untitled Plan" else data.title
		binding.assignTo.text = if (data.partner_assignment.isNullOrBlank()) "No One Yet" else data.partner_assignment
		binding.percentage.text = if (data.completed_task_percentage == null) "0% COMPLETED" else "${data.completed_task_percentage}% COMPLETED"
	}

	private fun goToLoginScreen() {
		// if cookie expire goto starter screen
		// not good practice to use user creds to get the fresh cookie
		startActivity(
			AuthHostActivity.createAuthIntent(requireActivity()).addFlags(
				Intent.FLAG_ACTIVITY_CLEAR_TOP or
						Intent.FLAG_ACTIVITY_NEW_TASK or
						Intent.FLAG_ACTIVITY_CLEAR_TASK
			)
		)
		requireActivity().finish()
	}
	
	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}

	companion object {
		var planId: Int? = -1
		var relationshipTasks: List<MyRelationshipPlanChecklistItem> = ArrayList()
	}
}