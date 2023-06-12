package com.tovalue.me.ui.auth.primeriv

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.tovalue.me.R
import com.tovalue.me.databinding.FragmentPrimerFourBinding
import com.tovalue.me.ui.auth.AuthViewModel
import com.tovalue.me.ui.auth.primerv.TvmNumberFragment
import com.tovalue.me.util.Constants.FACET_STAGE
import com.tovalue.me.util.Constants.TVMNUMBER_STAGE
import com.tovalue.me.util.extensions.replaceFragmentSafely

class PrimerFourFragment : Fragment() {
	private var _binding: FragmentPrimerFourBinding? = null
	private val binding get() = _binding!!
	private val authViewModel: AuthViewModel by activityViewModels()
	
	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		_binding = FragmentPrimerFourBinding.inflate(inflater, container, false)
		return binding.root
	}
	
	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		setupClickListener()
	}
	
	private fun setupClickListener() {
		binding.startBtn.setOnClickListener{
			authViewModel.setBookMarkProgress(FACET_STAGE)
			goToPersonalityFacetFrag()
		}
	}
	
	private fun goToPersonalityFacetFrag() {
		replaceFragmentSafely(
			PersonalityFacetFragment(),
			containerViewId = R.id.host_root,
			addToStack = false
		)
	}
	
	private fun goToTVMNumberFrag() {
		replaceFragmentSafely(
			TvmNumberFragment(),
			containerViewId = R.id.host_root,
			addToStack = false
		)
	}
	
	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}
}