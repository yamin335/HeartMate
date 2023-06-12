package com.tovalue.me.ui.dashboard.relationshipGoals

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.tovalue.me.common.showToast
import com.tovalue.me.databinding.FragmentRelationshipGoalGiftBinding
import com.tovalue.me.model.MyRelationshipPlanResponseData
import com.tovalue.me.model.RelationshipPlan
import com.tovalue.me.ui.auth.AuthHostActivity
import com.tovalue.me.ui.dashboard.DashboardViewModel
import com.tovalue.me.util.DialogUtils
import com.tovalue.me.util.extensions.updateStatusBarBackgroundColor
import com.tovalue.me.util.livedata.EventObserver

class RelationshipPlansFragment : Fragment() {
	private var _binding: FragmentRelationshipGoalGiftBinding? = null
	private val binding get() = _binding!!
	private val viewModel: DashboardViewModel by activityViewModels()
	private lateinit var relationshipPlanListAdapter: RelationshipPlanListAdapter

	override fun onResume() {
		super.onResume()
		DialogUtils.showDialog(requireContext(), true)
		viewModel.getAvailableRelationshipPlans()
	}

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		// Inflate the layout for this fragment
		_binding = FragmentRelationshipGoalGiftBinding.inflate(layoutInflater, container, false)
		return binding.root
	}
	
	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		updateStatusBarBackgroundColor("#1596A1")

		relationshipPlanListAdapter = RelationshipPlanListAdapter {

		}

		binding.recyclerPlan.adapter = relationshipPlanListAdapter

		binding.btnBack.setOnClickListener {
			requireActivity().onBackPressed()
		}

		viewModel.relationshipPlanResponse.observe(viewLifecycleOwner,
			EventObserver { DialogUtils.hideDialog()
				when (it) {
					is DashboardViewModel.UiEventRelationshipPlan.RelationshipPlanData -> {
						relationshipPlanListAdapter.submitData(it.response.data ?: ArrayList())
						checkPlanData(it.response.data)
					}
					is DashboardViewModel.UiEventRelationshipPlan.COOKIEXPIRE -> {
						viewModel.flushSavedData()
						goToLoginScreen()
					}
					is DashboardViewModel.UiEventRelationshipPlan.Error -> {
						showToast(it.errorMsg)
					}
				}
			})
	}

	private fun checkPlanData(list: List<RelationshipPlan>?) {
		if (list.isNullOrEmpty()) {
			binding.noGoalsText.visibility = View.VISIBLE
			binding.recyclerPlan.visibility = View.GONE
		} else {
			binding.noGoalsText.visibility = View.GONE
			binding.recyclerPlan.visibility = View.VISIBLE
		}
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
}