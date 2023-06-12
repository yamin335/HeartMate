package com.tovalue.me.ui.auth.primeriii.sides

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.viewpager2.widget.ViewPager2
import com.tovalue.me.R
import com.tovalue.me.databinding.FragmentLifeInventoryBinding
import com.tovalue.me.ui.auth.AuthViewModel
import com.tovalue.me.ui.auth.primeriii.QuestionnairesFragment
import com.tovalue.me.ui.auth.primeriii.SideCategory
import com.tovalue.me.util.extensions.replaceFragmentSafely

class LifeInventoryFragment : Fragment() {
	private var _binding: FragmentLifeInventoryBinding? = null
	private val binding get() = _binding!!
	private val authViewModel: AuthViewModel by activityViewModels()
	private var categoryListL = listOf<SideCategory>()
	private var count = 0
	
	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		_binding = FragmentLifeInventoryBinding.inflate(inflater, container, false)
		return binding.root
	}
	
	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		if (authViewModel.inventory.value == null) {
			binding.loader.isVisible = true
			binding.continueBtn.isClickable = false
			binding.continueBtn.isEnabled = false
			authViewModel.getInventorySides()
		}
		setUpClickListener()
		observeStates()
	}
	
	private fun observeStates() {
		authViewModel.sidePosition.observe(viewLifecycleOwner) {
			count = it
			binding.viewPager.setCurrentItem(count, false)
		}
		
		authViewModel.inventory.observe(viewLifecycleOwner) {
			binding.loader.isVisible = false
			if (it.sideCategory.isNotEmpty()) {
				categoryListL = it.sideCategory
				setUpViews()
				binding.continueBtn.isClickable = true
				binding.continueBtn.isEnabled = true
			}
		}
	}
	
	private fun setUpViews() {
		binding.viewPager.apply {
			adapter = InventoryPagerAdapter(VIEW_TYPE_OPTION_SIDES,
				categoryListL,
			itemClickListner = {})
			currentItem = count
			isUserInputEnabled = false
		}
		binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
			override fun onPageSelected(position: Int) {
				count = position
			}
		})
	}
	
	private fun setUpClickListener() {
		binding.continueBtn.setOnClickListener {
			replaceFragmentSafely(
				QuestionnairesFragment(),
				containerViewId = R.id.host_root
			)
			authViewModel.categoryKey = categoryListL[count].categoryId // could be removed later as we already setting [@inventoryData]
			authViewModel.setInventoryCategoryData(categoryListL[count])
			authViewModel.setSidePosition(count+1)
		}
	}
	
	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}
	
	companion object {
		const val VIEW_TYPE_OPTION_SIDES = "sides"
	}
}