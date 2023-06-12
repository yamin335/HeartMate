package com.tovalue.me.ui.auth.primerone

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.tovalue.me.R
import com.tovalue.me.common.showToast
import com.tovalue.me.common.trimmedText
import com.tovalue.me.databinding.FragmentNameBinding
import com.tovalue.me.ui.auth.AuthViewModel
import com.tovalue.me.util.Constants.EMAIL_STAGE
import com.tovalue.me.util.Utils.hideKeyboard
import com.tovalue.me.util.extensions.replaceFragmentSafely
import com.tovalue.me.util.extensions.useAdjustResize

class NameFragment : Fragment() {
	private var _binding: FragmentNameBinding? = null
	private val binding get() = _binding!!
	
	private val authViewModel: AuthViewModel by activityViewModels()
	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		_binding = FragmentNameBinding.inflate(inflater, container, false)
		return binding.root
	}
	
	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		setUpViews()
		setUpClickListener()
	}
	
	override fun onResume() {
		super.onResume()
		useAdjustResize()
	}
	
	private fun setUpViews() {
	
	}
	
	private fun setUpClickListener() {
		binding.nextImg.setOnClickListener {
			validateFields(
				binding.firstNameLayout.editText!!.trimmedText,
				binding.lastNameLayout.editText!!.trimmedText
			)
		}
	}
	
	private fun validateFields(firstName: String, lastName: String) {
		if (firstName.isEmpty())
			showToast("First name is empty.")
		else if (lastName.isEmpty())
			showToast("Last name is Empty.")
		else {
			authViewModel.setBookMarkProgress(EMAIL_STAGE)
			authViewModel.saveName(firstName.plus(" ").plus(lastName))
			hideKeyboard(requireActivity())
			goToEmailFrag()
		}
	}
	
	private fun goToEmailFrag() {
		replaceFragmentSafely(
			EmailFragment(),
			containerViewId = R.id.host_root,
			addToStack = false
		)
	}
	
	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}
}