package com.tovalue.me.ui.auth.primer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.tovalue.me.R
import com.tovalue.me.common.showToast
import com.tovalue.me.common.trimmedText
import com.tovalue.me.databinding.FragmentPhoneAuthBinding
import com.tovalue.me.ui.auth.AuthViewModel
import com.tovalue.me.util.Utils.hideKeyboard
import com.tovalue.me.util.extensions.replaceFragmentSafely
import com.tovalue.me.util.extensions.useAdjustResize
import com.tovalue.me.util.livedata.EventObserver


class PhoneAuthFragment : Fragment(){
	private var _binding: FragmentPhoneAuthBinding? = null
	private val binding get() = _binding!!
	private val authViewModel: AuthViewModel by activityViewModels()
	
	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		_binding = FragmentPhoneAuthBinding.inflate(inflater, container, false)
		return binding.root
	}
	
	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		setUpClickListener()
	}
	
//	private fun setUpViews() {
//		authViewModel.state.observe(viewLifecycleOwner,
//			EventObserver {
//				hideLoader()
//				when (it) {
//					is AuthViewModel.UiEvent.GotoNextScreen -> {
//						goToOtpVerificationFrag()
//					}
//					is AuthViewModel.UiEvent.Error -> {
//						showToast(it.errorMsg.toString())
//					}
//				}
//			})
//	}
	
//	private fun hideLoader() {
//		binding.loader.isVisible = false
//	}
	
	private fun setUpClickListener() {
		binding.countryCodeSp.registerCarrierNumberEditText(binding.mobileNo)
		binding.nextImg.setOnClickListener {
			val numberTxt = binding.mobileNo.trimmedText
			if (binding.countryCodeSp.isValidFullNumber) {
				authViewModel.number = numberTxt.replace(" ","")
				authViewModel.countryCode = binding.countryCodeSp.selectedCountryCodeWithPlus
				goToOtpVerificationFrag()
			} else
				showToast(resources.getString(R.string.phone_valid_number))
		}
		
		binding.cancelImg.setOnClickListener {
			requireActivity().supportFragmentManager.popBackStack()
		}
	}
	
//	private fun verifyNumber() {
//		binding.loader.isVisible = true
//		hideKeyboard(requireActivity())
//		authViewModel.verifyPhoneNumber()
//	}
	
	private fun goToOtpVerificationFrag() {
		replaceFragmentSafely(
			PhoneVerificationFragment(),
			containerViewId = R.id.host_root
		)
	}
	
	override fun onResume() {
		super.onResume()
		useAdjustResize()
	}
	
	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}
}