package com.tovalue.me.ui.auth.primerone

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import com.tovalue.me.R
import com.tovalue.me.common.showToast
import com.tovalue.me.common.trimmedText
import com.tovalue.me.databinding.FragmentEmailBinding
import com.tovalue.me.ui.auth.AuthViewModel
import com.tovalue.me.util.Constants.BIRTHDAY_STAGE
import com.tovalue.me.util.Utils.hideKeyboard
import com.tovalue.me.util.Utils.isValidEmail
import com.tovalue.me.util.extensions.replaceFragmentSafely
import com.tovalue.me.util.extensions.useAdjustResize
import com.tovalue.me.util.livedata.EventObserver

class EmailFragment : Fragment() {
	private var _binding: FragmentEmailBinding? = null
	private val binding get() = _binding!!
	private val authViewModel: AuthViewModel by activityViewModels()
	
	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		_binding = FragmentEmailBinding.inflate(inflater, container, false)
		return binding.root
	}
	
	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		observeEvents()
		setUpClickListener()
	}
	
	private fun observeEvents() {
		authViewModel.state.observe(viewLifecycleOwner,
			EventObserver {
				binding.loader.isVisible = false
				when (it) {
					is AuthViewModel.UiEvent.GotoNextScreen -> {
						authViewModel.setBookMarkProgress(BIRTHDAY_STAGE)
						goToBirthdayFrag()
					}
					is AuthViewModel.UiEvent.Error -> {
						showToast(it.errorMsg.toString())
					}
				}
			})
	}
	
	override fun onResume() {
		super.onResume()
		useAdjustResize()
	}
	
	private fun setUpClickListener() {
		binding.nextImg.setOnClickListener {
			validateField()
		}
		binding.radioBtn.setOnClickListener{
			if (!binding.radioBtn.isSelected) {
				binding.radioBtn.isSelected = true
				binding.radioBtn.isChecked = true
			} else {
				binding.radioBtn.isSelected = false
				binding.radioBtn.isChecked = false
			}
		}
	}
	
	private fun validateField() {
		val emailTxt = binding.emailLayout.editText!!.trimmedText
		if (emailTxt.isEmpty() || !isValidEmail(emailTxt))
			showToast("Email is empty or invalid.")
		else {
			hideKeyboard(requireActivity())
			binding.loader.isVisible = true
			authViewModel.verifyEmail(emailTxt)
		}
	}
	
	private fun goToBirthdayFrag() {
		replaceFragmentSafely(
			BirthdayFragment(),
			containerViewId = R.id.host_root,
			addToStack = false
		)
	}
	
	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}
}