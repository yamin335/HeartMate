package com.tovalue.me.ui.auth.primeriii.visualizer

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.google.gson.Gson
import com.tovalue.me.R
import com.tovalue.me.databinding.FragmentInventoryCategoryBinding
import com.tovalue.me.helper.MomensityBingoApp
import com.tovalue.me.model.ProfileInfo
import com.tovalue.me.ui.auth.AuthViewModel
import com.tovalue.me.ui.auth.primeriii.QuestionnairesFragment
import com.tovalue.me.ui.auth.primeriii.sides.InventoryPagerAdapter
import com.tovalue.me.ui.auth.primerx.PrimerXFragment
import com.tovalue.me.util.Constants
import com.tovalue.me.util.extensions.replaceFragmentSafely
import com.tovalue.me.util.livedata.EventObserver

class InventoryCategoryFragment : Fragment() {
	private var _binding: FragmentInventoryCategoryBinding? = null
	private val binding get() = _binding!!
	private val authViewModel: AuthViewModel by activityViewModels()
	private lateinit var profileInfo: ProfileInfo
	
	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		_binding = FragmentInventoryCategoryBinding.inflate(inflater, container, false)
		return binding.root
	}
	
	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		setUpViews()
		observeEvents()
		setUpClickListener()
	}
	
	private fun observeEvents() {
		authViewModel.cateogryState.observe(viewLifecycleOwner, EventObserver {
			when(it) {
				is AuthViewModel.UiEventCategory.CategoryPosition -> {
					authViewModel.setInventoryCategoryData(profileInfo.inventoryCategories[it.selectedCategoryPosition])
					authViewModel.categoryKey = profileInfo.inventoryCategories[it.selectedCategoryPosition].categoryId
					authViewModel.setSidePosition(it.selectedCategoryPosition + 1)
					goToQuestionaireFrag()
				}
			}
		})
	}
	
	private fun goToQuestionaireFrag() {
		replaceFragmentSafely(
			QuestionnairesFragment(),
			containerViewId = R.id.navigation_root_container,
			addToStack = false
		)
	}
	
	private fun setUpClickListener() {
		binding.backBtn.setOnClickListener {
			requireActivity().onBackPressed()
		}
		
		binding.importantBtn.setOnClickListener {
			goToPrimerXFrag()
		}
	}
	
	private fun goToPrimerXFrag() {
		replaceFragmentSafely(
			PrimerXFragment(),
			containerViewId = R.id.navigation_root_container
		)
	}
	
	private fun setUpViews() {
		// put the preferences in data layer
		profileInfo =  Gson().fromJson(
			MomensityBingoApp.preferencesManager?.getStringValue(Constants.USER_DATA_CODE_KEY),
			ProfileInfo::class.java
		)
		
		with(binding.categotyRv) {
			adapter = InventoryPagerAdapter(VIEW_TYPE_OPTION_CATEGORY,
				profileInfo.inventoryCategories,
				itemClickListner = {authViewModel.onUiAction(AuthViewModel.UiAction.ItemClicked(it))})
		}
	}
	
	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}
	companion object {
		const val VIEW_TYPE_OPTION_CATEGORY = "category"
	}
}