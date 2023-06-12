package com.tovalue.me.ui.auth.primeriii.spectrum

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.tovalue.me.R
import com.tovalue.me.databinding.FragmentGreatJobBinding
import com.tovalue.me.ui.auth.AuthViewModel
import com.tovalue.me.ui.auth.primeriv.PrimerFourFragment
import com.tovalue.me.ui.auth.primerx.PrimerXFragment
import com.tovalue.me.ui.dashboard.navigation.NavigationActivity
import com.tovalue.me.util.Constants.PRIMERIV_STAGE
import com.tovalue.me.util.Constants.PRIMERX_STAGE
import com.tovalue.me.util.extensions.clearBackStack
import com.tovalue.me.util.extensions.replaceFragmentSafely

class GreatJobFragment : Fragment() {

	private var _binding: FragmentGreatJobBinding? = null
	private val binding get() = _binding!!
	private val authViewModel: AuthViewModel by activityViewModels()
	
	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		_binding = FragmentGreatJobBinding.inflate(inflater, container, false)
		return binding.root
	}
	
	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		
		// 8 == total categories
		// isEditMode -> true in registration and false when no registration in process
		binding.continueBtn.setOnClickListener {
			if (authViewModel.getSidePosition() == CATEGORIES_SIZE && authViewModel.isEditMode) {
				clearBackStack()
				authViewModel.setBookMarkProgress(PRIMERX_STAGE)
				goToPrimerXFragment()
				return@setOnClickListener
			}
			if (authViewModel.getSidePosition() != CATEGORIES_SIZE && authViewModel.isEditMode) {
				requireActivity().supportFragmentManager.popBackStack()
				return@setOnClickListener
			}
			if (!authViewModel.isEditMode) (activity as NavigationActivity?)?.goToUnityPlayer()
		}
		observeState()
	}
	
	private fun observeState() {
		authViewModel.jobState.observe(viewLifecycleOwner) { spectrumResponse ->
			setData(spectrumResponse)
		}
	}
	
	private fun setData(response: SpectrumResponse?) {
		authViewModel.setSpectrumMusicUrl(response!!.spectrum_response)
		authViewModel.setInventoryData(response)
		
		binding.scoreTv.text = response.category_total.toString()
		binding.inventoryTv.text = response.category_title
		binding.firstTv.text = response.category_comment
		binding.subTitleTv.text = response.direction
	}
	
	private fun goToPrimerXFragment() {
		replaceFragmentSafely(
			PrimerXFragment(),
			containerViewId = R.id.host_root,
			addToStack = false
		)
	}
	
	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}
	
	companion object {
		const val CATEGORIES_SIZE = 8
	}
}