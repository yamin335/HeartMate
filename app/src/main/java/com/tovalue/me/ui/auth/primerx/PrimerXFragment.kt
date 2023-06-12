package com.tovalue.me.ui.auth.primerx

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import com.tovalue.me.R
import com.tovalue.me.databinding.FragmentPrimerXBinding
import com.tovalue.me.ui.auth.AuthViewModel
import com.tovalue.me.ui.auth.primeriv.PrimerFourFragment
import com.tovalue.me.util.Constants
import com.tovalue.me.util.extensions.replaceFragmentSafely

class PrimerXFragment : Fragment() {
	private var _binding: FragmentPrimerXBinding? = null
	private val binding get() = _binding!!
	private val authViewModel: AuthViewModel by activityViewModels()
	
	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		_binding = FragmentPrimerXBinding.inflate(inflater, container, false)
		return binding.root
	}
	
	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		setUpViews()
		setUpClickListener()
	}
	
	private fun setUpClickListener() {
		binding.backBtn.setOnClickListener {
			requireActivity().supportFragmentManager.popBackStack()
		}
		binding.primeXBtn.setOnClickListener {
			authViewModel.setBookMarkProgress(Constants.PRIMERIV_STAGE)
			goToPrimerFourFragment()
		}
	}
	
	private fun setUpViews() {
		if (authViewModel.isEditMode) {
			binding.primeXBtn.isVisible = true
			binding.backBtn.isVisible = false
		} else {
			binding.backBtn.isVisible = true
			binding.primeXBtn.isVisible = false
		}
	}
	
	private fun goToPrimerFourFragment() {
		replaceFragmentSafely(
			PrimerFourFragment(),
			containerViewId = R.id.host_root,
			addToStack = false
		)
	}
	
	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}
}