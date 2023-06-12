package com.tovalue.me.ui.dashboard.relationshipGoals

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.tovalue.me.databinding.FragmentGoalPurchaseSuccessBinding
import com.tovalue.me.databinding.FragmentMoodRingHistoryBinding
import com.tovalue.me.databinding.FragmentRelationshipGoalsBinding
import com.tovalue.me.helper.MomensityBingoApp
import com.tovalue.me.model.MyRelationshipPlanChecklistItem
import com.tovalue.me.model.moodRingModels.MoodRingHistory
import com.tovalue.me.ui.dashboard.DashboardViewModel
import com.tovalue.me.ui.dashboard.navigation.NavigationActivity
import com.tovalue.me.util.*
import com.tovalue.me.util.extensions.animateVisibility
import com.tovalue.me.util.extensions.updateStatusBarBackgroundColor
import java.util.*
import kotlin.collections.ArrayList

class RelationshipGoalPurchaseSuccessFragment : Fragment() {
	private var _binding: FragmentGoalPurchaseSuccessBinding? = null
	private val binding get() = _binding!!
	private val viewModel: DashboardViewModel by activityViewModels()
	
	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		// Inflate the layout for this fragment
		_binding = FragmentGoalPurchaseSuccessBinding.inflate(layoutInflater, container, false)
		return binding.root
	}
	
	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		updateStatusBarBackgroundColor("#EE43F1")

		binding.firstName.text = MomensityBingoApp.preferencesManager!!.getStringValue(
			Constants.NAME_KEY
		).toString()

	}
	
	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}
}