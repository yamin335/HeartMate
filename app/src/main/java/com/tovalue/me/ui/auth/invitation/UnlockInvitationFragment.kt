package com.tovalue.me.ui.auth.invitation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.tovalue.me.R
import com.tovalue.me.common.showToast
import com.tovalue.me.common.trimmedText
import com.tovalue.me.databinding.FragmentUnlockInvitationBinding
import com.tovalue.me.ui.auth.AuthViewModel
import com.tovalue.me.util.Constants.ACCEPT_REJECT_INVITATION
import com.tovalue.me.util.extensions.replaceFragmentSafely

const val WEEKS_IN_YEAR = "52"
const val TOTAL_DATES_COUNT = "104"

class UnlockInvitationFragment : Fragment() {
	private var _binding: FragmentUnlockInvitationBinding? = null
	private val binding get() = _binding!!
	private val authViewModel: AuthViewModel by activityViewModels()
	
	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		_binding = FragmentUnlockInvitationBinding.inflate(inflater, container, false)
		return binding.root
	}
	
	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		setUpViews()
		setUpClickListener()
	}
	
	private fun setUpViews() {
		binding.nameTv.text = "Unlock ${authViewModel.getName().substringBefore(" ")}'s Value"
		binding.totalDatesTv.text = "If you had 2 dates &amp; other experiences per week with ${
			authViewModel.getName().substringBefore(" ")
		} - how many experiences would that be per year?"
	}
	
	private fun setUpClickListener() {
		binding.submitBtn.setOnClickListener {
			if (binding.weeksET.trimmedText.isEmpty() || binding.datesET.trimmedText.isEmpty())
				showToast("Enter required values.")
			else if (validateAnswers()) {
				authViewModel.setBookMarkProgress(ACCEPT_REJECT_INVITATION)
				goToInvitationFrag()
			} else {
				showToast("Answer is incorrect.")
			}
		}
	}
	
	private fun goToInvitationFrag() {
		replaceFragmentSafely(
			AcceptInvitationFragment(),
			containerViewId = R.id.host_root,
			addToStack = false
		)
	}
	
	private fun validateAnswers(): Boolean =
		binding.weeksET.trimmedText == WEEKS_IN_YEAR && binding.datesET.trimmedText == TOTAL_DATES_COUNT
	
	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}
}