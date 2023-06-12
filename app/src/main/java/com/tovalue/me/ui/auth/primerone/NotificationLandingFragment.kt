package com.tovalue.me.ui.auth.primerone

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.tovalue.me.R
import com.tovalue.me.common.showToast
import com.tovalue.me.databinding.FragmentNotificationLandingBinding
import com.tovalue.me.ui.auth.AuthViewModel
import com.tovalue.me.ui.auth.invitation.UnlockInvitationFragment
import com.tovalue.me.ui.auth.primertwo.PrimerTwoFragment
import com.tovalue.me.util.Constants.PRIMERII_STAGE
import com.tovalue.me.util.Constants.UNLOCK_INVITATION
import com.tovalue.me.util.extensions.replaceFragmentSafely

class NotificationLandingFragment : Fragment() {
	private var _binding: FragmentNotificationLandingBinding? = null
	private val binding get() = _binding!!
	private val authViewModel: AuthViewModel by activityViewModels()
	
	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		_binding = FragmentNotificationLandingBinding.inflate(inflater, container, false)
		return binding.root
	}
	
	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		setUpClickListener()
		observeState()
	}
	
	private fun observeState() {
		authViewModel.notificationState.observe(viewLifecycleOwner) {
			binding.loader.isVisible = false
			when(it) {
				is AuthViewModel.UiEvent.GotoNextScreen -> {
					if (authViewModel.getLoginResponse() != null) {
						authViewModel.setBookMarkProgress(UNLOCK_INVITATION)
						goToUnlockInvitationFrag()
					} else {
						authViewModel.setBookMarkProgress(PRIMERII_STAGE)
						goToPrimerTwoFrag()
					}
				}
				is AuthViewModel.UiEvent.Error -> {
					showToast(it.errorMsg.toString())
				}
			}
		}
	}
	
	private fun goToUnlockInvitationFrag() {
		replaceFragmentSafely(
			UnlockInvitationFragment(),
			containerViewId = R.id.host_root,
			addToStack = false
		)
	}
	
	private fun setUpClickListener() {
		binding.nextImg.setOnClickListener {
			var isEnable = true
			if (binding.chipEnable.isChecked) isEnable = true
			else if (binding.chipDisable.isChecked) isEnable = false
			
			binding.loader.isVisible = true
			authViewModel.updateNotificationSetting(isEnable)
		}
	}
	
	private fun goToPrimerTwoFrag() {
		replaceFragmentSafely(
			PrimerTwoFragment(),
			containerViewId = R.id.host_root,
			addToStack = false
		)
	}
	
	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}
}