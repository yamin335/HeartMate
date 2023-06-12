package com.tovalue.me.ui.auth.invitation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.tovalue.me.R
import com.tovalue.me.common.showToast
import com.tovalue.me.databinding.FragmentAcceptInvitationBinding
import com.tovalue.me.ui.auth.AuthViewModel
import com.tovalue.me.ui.auth.primertwo.PrimerTwoFragment
import com.tovalue.me.util.Constants.PRIMERII_STAGE
import com.tovalue.me.util.extensions.replaceFragmentSafely

class AcceptInvitationFragment : Fragment() {
	private var _binding: FragmentAcceptInvitationBinding ?= null
	private val binding get() = _binding!!
	private val authViewModel: AuthViewModel by activityViewModels()
	
	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		_binding = FragmentAcceptInvitationBinding.inflate(inflater, container, false)
		return binding.root
	}
	
	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		setUpViews()
		setUpClickListener()
		setUpObserver()
	}
	
	private fun setUpViews() {
		val loginResult = authViewModel.getLoginResponse()!!
		Glide.with(requireContext()).load(loginResult.invitedUserPicture)
			.error(R.drawable.default_avatar)
			.into(binding.profileImg)
		binding.nameTv.text = loginResult.invitedBy
		binding.datingNumberTv.text = loginResult.invitedUserTvm
		binding.experienceSubTitle.text = "I invite you to invest in ${loginResult.invitedUserTvm} Experiences to ensure I feel seen &amp; heard by you."
	}
	
	private fun setUpObserver() {
		authViewModel.invitationState.observe(viewLifecycleOwner) {
			when(it) {
				is AuthViewModel.UiInvitation.Error -> {
					binding.loader.isVisible = false
					it.msg?.let { it1 -> showToast(it1) }
				}
				is AuthViewModel.UiInvitation.GoToNextScreen -> {
					binding.loader.isVisible = false
					authViewModel.setBookMarkProgress(PRIMERII_STAGE)
					goToPrimerTwoFrag()
				}
			}
		}
	}
	
	private fun goToPrimerTwoFrag() {
		replaceFragmentSafely(
			PrimerTwoFragment(),
			containerViewId = R.id.host_root,
			addToStack = false
		)
	}
	
	private fun setUpClickListener() {
		binding.acceptInvitationTv.setOnClickListener {
			binding.loader.isVisible = true
			authViewModel.levelOneInvitation(ACCEPT_INVITATION)
		}
		binding.declineInvitationTv.setOnClickListener {
			binding.loader.isVisible = true
			authViewModel.levelOneInvitation(DECLINE_INVITATION)
		}
	}
	
	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}
	
	companion object {
		const val ACCEPT_INVITATION = "accept"
		const val DECLINE_INVITATION = "decline"
	}
}