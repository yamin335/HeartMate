package com.tovalue.me.ui.dashboard.relationshipGoals

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.tovalue.me.common.showToast
import com.tovalue.me.databinding.FragmentRelationshipGoalsBinding
import com.tovalue.me.helper.MomensityBingoApp
import com.tovalue.me.model.MyRelationshipPlanResponseData
import com.tovalue.me.ui.auth.AuthHostActivity
import com.tovalue.me.ui.dashboard.DashboardViewModel
import com.tovalue.me.ui.dashboard.myMoodRing.MyMoodRingFragment
import com.tovalue.me.ui.dashboard.navigation.NavigationActivity
import com.tovalue.me.util.*
import com.tovalue.me.util.extensions.updateStatusBarBackgroundColor
import com.tovalue.me.util.livedata.EventObserver
import kotlin.collections.ArrayList

class MyRelationshipPlansFragment : Fragment() {
	private var _binding: FragmentRelationshipGoalsBinding? = null
	private val binding get() = _binding!!
	private val viewModel: DashboardViewModel by activityViewModels()
	private lateinit var myRelationshipPlanListAdapter: MyRelationshipPlanListAdapter
	
	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		// Inflate the layout for this fragment
		_binding = FragmentRelationshipGoalsBinding.inflate(layoutInflater, container, false)
		return binding.root
	}

	override fun onResume() {
		super.onResume()
		DialogUtils.showDialog(requireContext(), true)
		viewModel.getMyRelationshipPlans()
	}
	
	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		updateStatusBarBackgroundColor("#1596A1")

		myRelationshipPlanListAdapter = MyRelationshipPlanListAdapter(assignCallBack = {
			MyRelationshipPlanDetailsFragment.planId = it.plan_id
			startActivity(
				NavigationActivity.createIntent(
					requireActivity(),
					Constants.RELATIONSHIP_GOAL_DETAILS
				)
			)
		},
		deadlineCallBack = {
			val dialog = RelationshipGoalDeadlineAssignmentDialogFragment()
			dialog.show(parentFragmentManager, MyRelationshipPlansFragment::class.java.simpleName)
		})

		binding.recyclerPlan.apply {
			//addItemDecoration(RecyclerItemDivider(requireContext(), LinearLayoutManager.VERTICAL))
			adapter = myRelationshipPlanListAdapter
		}

		binding.firstName.text = MomensityBingoApp.preferencesManager!!.getStringValue(
			Constants.NAME_KEY
		).toString()

		binding.btnBack.setOnClickListener {
			requireActivity().onBackPressed()
		}

		viewModel.myPlanResponse.observe(viewLifecycleOwner,
			EventObserver { DialogUtils.hideDialog()
				when (it) {
					is DashboardViewModel.UiEventMyRelationshipPlan.MyRelationshipPlanData -> {
						myRelationshipPlanListAdapter.submitData(it.response.data ?: ArrayList())
						checkPlanData(it.response.data)
					}
					is DashboardViewModel.UiEventMyRelationshipPlan.COOKIEXPIRE -> {
						viewModel.flushSavedData()
						goToLoginScreen()
					}
					is DashboardViewModel.UiEventMyRelationshipPlan.Error -> {
						showToast(it.errorMsg)
//						val tempList = arrayListOf(
//							MyRelationshipPlanResponseData(
//								deadline_date_setting = "4234",
//								id = 1, name = "rwr", partner_name = "wrw",
//								percentage_complete = "30", plan_id = 2, plan_title = "erwrwerw",
//								reminder_status = "wrwrwrwer"
//							),
//							MyRelationshipPlanResponseData(
//								deadline_date_setting = "4234",
//								id = 1, name = "rwr", partner_name = "wrw",
//								percentage_complete = "30", plan_id = 2, plan_title = "erwrwerw",
//								reminder_status = "wrwrwrwer"
//							),
//							MyRelationshipPlanResponseData(
//								deadline_date_setting = "4234",
//								id = 1, name = "rwr", partner_name = "wrw",
//								percentage_complete = "30", plan_id = 2, plan_title = "erwrwerw",
//								reminder_status = "wrwrwrwer"
//							))
//
//						myRelationshipPlanListAdapter.submitData(tempList)
//						checkPlanData(tempList)
					}
				}
			})
	}

	private fun checkPlanData(list: List<MyRelationshipPlanResponseData>?) {
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