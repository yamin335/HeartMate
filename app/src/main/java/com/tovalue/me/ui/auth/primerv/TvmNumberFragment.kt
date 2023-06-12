package com.tovalue.me.ui.auth.primerv

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tovalue.me.util.extensions.replaceFragmentSafely
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import com.tovalue.me.R
import com.tovalue.me.databinding.FragmentTvmNumberBinding
import com.tovalue.me.ui.auth.AuthViewModel
import com.tovalue.me.ui.dashboard.DashboardActivity
import com.tovalue.me.ui.dashboard.favorite.LevelOneInvitationGuideFragment
import com.tovalue.me.util.Constants.PRIMERV_STAGE

class TvmNumberFragment : Fragment() {
	private var _binding: FragmentTvmNumberBinding? = null
	private val binding get() = _binding!!
	private val authViewModel: AuthViewModel by activityViewModels()
	
	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		_binding = FragmentTvmNumberBinding.inflate(inflater, container, false)
		return binding.root
	}
	
	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		setUpClickListener()
//		observeEvents()
		setData()
	}
	
	private fun setData() {
		binding.dotProgress.startSpin()
		binding.nameTv.text = "TO Value ${authViewModel.getName().substringBefore(" ")}"
		binding.learnMore.text = "And, ${authViewModel.getName().substringBefore(" ")}, you're worth it."
		binding.scoreTv.text = authViewModel.UserTotalLifeScore.toString()
	}
	
//	private fun observeEvents() {
//		binding.dotProgress.startSpin()
//		authViewModel.guideState.observe(viewLifecycleOwner) {
//			when(it) {
//				is AuthViewModel.UiEventTvmReport.Report -> {
//
//					binding.dotProgress.stopSpin()
//				}
//				is AuthViewModel.UiEventTvmReport.Error -> {
//					showToast(it.errorMsg.toString())
//				}
//			}
//		}
//	}

	private fun setUpClickListener() {
		binding.startBtn.setOnClickListener {
			goToPrimerViiiFrag()
		}
	}


	private fun goToPrimerViiiFrag() {
		replaceFragmentSafely(
			LevelOneInvitationGuideFragment(),
			containerViewId = R.id.host_root,
			addToStack = false
		)
	}


	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}
}