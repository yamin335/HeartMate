package com.tovalue.me.ui.auth.primervi

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.tovalue.me.R
import com.tovalue.me.databinding.FragmentPrimerSixBinding
import com.tovalue.me.ui.auth.AuthViewModel
import com.tovalue.me.ui.auth.primerv.TvmNumberFragment
import com.tovalue.me.util.Constants
import com.tovalue.me.util.Constants.TVMNUMBER_STAGE
import com.tovalue.me.util.extensions.replaceFragmentSafely

class PrimerSixFragment : Fragment() {
	private var _binding: FragmentPrimerSixBinding? = null
	private val binding get() = _binding!!
	private val authViewModel: AuthViewModel by activityViewModels()

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		_binding = FragmentPrimerSixBinding.inflate(inflater, container, false)
		return binding.root
	}
	
	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		setUpClickListener()
	}
	
	private fun setUpClickListener() {
		binding.startBtn.setOnClickListener{
			authViewModel.setBookMarkProgress(TVMNUMBER_STAGE)
			goToTVMNumberFrag()
		}
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