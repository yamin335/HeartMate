package com.tovalue.me.ui.auth.primerone

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.tovalue.me.R
import com.tovalue.me.common.showToast
import com.tovalue.me.common.trimmedText
import com.tovalue.me.databinding.FragmentPasswordBinding
import com.tovalue.me.ui.auth.AuthViewModel
import com.tovalue.me.util.Constants.BIRTHDAY_STAGE
import com.tovalue.me.util.Constants.NOTIFICATION_STAGE
import com.tovalue.me.util.Utils.hideKeyboard
import com.tovalue.me.util.extensions.replaceFragmentSafely
import com.tovalue.me.util.extensions.useAdjustResize

class PasswordFragment : Fragment() {
	private var _binding: FragmentPasswordBinding? = null
	private val binding get() = _binding!!
	private val authViewModel: AuthViewModel by activityViewModels()
	
	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		_binding = FragmentPasswordBinding.inflate(inflater, container, false)
		return binding.root
	}
	
	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		setUpClickListener()
		observeEvents()
	}
	
	private fun observeEvents() {
		authViewModel.updateState.observe(viewLifecycleOwner) {
			binding.loader.isVisible = false
			when(it) {
				is AuthViewModel.UiEvent.GotoNextScreen -> {
					authViewModel.setBookMarkProgress(NOTIFICATION_STAGE)
					goToNotificationFrag()
				}
				is AuthViewModel.UiEvent.Error -> {
					showToast(it.errorMsg.toString())
				}
			}
		}
	}
	
	override fun onResume() {
		super.onResume()
		useAdjustResize()
	}
	
	private fun setUpClickListener() {
		binding.nextImg.setOnClickListener {
			validateFields()
		}
	}
	
	/*All validation logic should moved into Domain layer*/
	private fun validateFields() {
		val passwordTxt = binding.passwordLayout.editText!!.trimmedText
		val confirmPasswordTxt = binding.confirmPasswordLayout.editText!!.trimmedText
		if (passwordTxt.isEmpty() || confirmPasswordTxt.isEmpty()) 
			showToast("Please enter password.")
		else if (passwordTxt.length < 8 || confirmPasswordTxt.length < 8)
			showToast("password length atleast should be 8.")
		else if (passwordTxt != confirmPasswordTxt) 
			showToast("password should be same.")
		else 
			updateUser(passwordTxt)
	}
	
	private fun updateUser(passTxt: String) {
		binding.loader.isVisible = true
		hideKeyboard(requireActivity())
//		authViewModel.updateUser(passTxt)
	}
	
	private fun goToNotificationFrag() {
		replaceFragmentSafely(
			NotificationLandingFragment(),
			containerViewId = R.id.host_root,
			addToStack = false
		)
	}
	
	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}
}