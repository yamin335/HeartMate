package com.tovalue.me.ui.auth.primeriii.visualizer

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tovalue.me.R
import com.tovalue.me.databinding.FragmentAudioVisualizerBinding
import com.tovalue.me.ui.auth.primeriv.PersonalityFacetFragment
import com.tovalue.me.util.extensions.replaceFragmentSafely

class AudioVisualizerFragment : Fragment() {
	private var _binding: FragmentAudioVisualizerBinding? = null
	private val binding get() = _binding!!
	
	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		_binding = FragmentAudioVisualizerBinding.inflate(inflater, container, false)
		return binding.root
	}
	
	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		setUpView()
		setUpClickListener()
	}
	
	private fun setUpView() {
	
	}
	
	private fun setUpClickListener() {
		binding.backImg.setOnClickListener {
			requireActivity().onBackPressed()
		}
		binding.updateInventoryBtn.setOnClickListener {
			goToInventoryCategoryFrag()
		}
		binding.whatMeanBtn.setOnClickListener {
			goToPersonalityFacetFrag()
		}
	}
	
	private fun goToPersonalityFacetFrag() {
		replaceFragmentSafely(
			PersonalityFacetFragment(),
			containerViewId = R.id.navigation_root_container,
		)
	}
	
	private fun goToInventoryCategoryFrag() {
		replaceFragmentSafely(
			InventoryCategoryFragment(),
			containerViewId = R.id.navigation_root_container
		)
	}
	
	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}
}