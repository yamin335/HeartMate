package com.tovalue.me.ui.auth.primeriv

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import com.google.android.material.tabs.TabLayoutMediator
import com.tovalue.me.R
import com.tovalue.me.adapter.ViewPager2Adapter
import com.tovalue.me.databinding.FragmentGuideBinding
import com.tovalue.me.ui.auth.AuthViewModel
import com.tovalue.me.ui.auth.primerv.PrimerFiveFragment
import com.tovalue.me.ui.dashboard.membership.PlanMemberShipFragment
import com.tovalue.me.util.Constants.PRIMERV_STAGE
import com.tovalue.me.util.extensions.replaceFragmentSafely

class GuideFragment : Fragment() {
	private var _binding: FragmentGuideBinding? = null
	private val binding get() = _binding!!
	private val pagerAdapter: ViewPager2Adapter by lazy { ViewPager2Adapter(requireActivity()) }
	private val authViewModel: AuthViewModel by activityViewModels()
	
	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		_binding = FragmentGuideBinding.inflate(inflater, container, false)
		return binding.root
	}
	
	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		setUpViews()
		setUpClickListener()
	}
	
	private fun setUpViews() {
		if (authViewModel.isEditMode) {
			binding.learnMoreLayout.moreLayout.isVisible = false
			binding.nextImg.isVisible = true
			binding.topBarLayout.topLayout.isVisible = false
		} else {
			binding.learnMoreLayout.moreLayout.isVisible = true
			binding.nextImg.isVisible = false
			binding.topBarLayout.topLayout.isVisible = true
		}
		
		pagerAdapter.apply {
			add(ViewPager2Adapter.FragmentName.INVESTMENT)
			add(ViewPager2Adapter.FragmentName.POSSIBILITIES)
		}
		
		binding.guidePager.apply {
			isUserInputEnabled = false
			adapter = pagerAdapter
			offscreenPageLimit = 2
		}
		
		TabLayoutMediator(binding.guideTabs, binding.guidePager) { tab, position ->
			tab.text = resources.getStringArray(R.array.guide_tab)[position]
			binding.guidePager.setCurrentItem(tab.position, true)
		}.attach()
	}
	
	private fun setUpClickListener() {
		binding.nextImg.setOnClickListener {
			authViewModel.setBookMarkProgress(PRIMERV_STAGE)
			goToPrimerFiveFrag()
		}
		
		binding.learnMoreLayout.moreLayout.setOnClickListener {
			replaceFragmentSafely(
				PlanMemberShipFragment(),
				containerViewId = R.id.navigation_root_container
			)
		}
		
		binding.topBarLayout.topLayout.setOnClickListener {
			requireActivity().onBackPressed()
		}
	}
	
	private fun goToPrimerFiveFrag() {
		replaceFragmentSafely(
			PrimerFiveFragment(),
			containerViewId = R.id.host_root,
			addToStack = false
		)
	}
	
	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}
}