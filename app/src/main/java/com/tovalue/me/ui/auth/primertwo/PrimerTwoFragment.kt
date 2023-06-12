package com.tovalue.me.ui.auth.primertwo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.tovalue.me.R
import com.tovalue.me.databinding.FragmentPrimerOneBinding
import com.tovalue.me.databinding.FragmentPrimerTwoBinding
import com.tovalue.me.ui.auth.AuthViewModel
import com.tovalue.me.util.Constants.PHOTO_STAGE
import com.tovalue.me.util.extensions.replaceFragmentSafely

class PrimerTwoFragment : Fragment() {
	private var _binding: FragmentPrimerTwoBinding? = null
	private val binding get() = _binding!!
	private val authViewModel: AuthViewModel by activityViewModels()
	
	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		_binding = FragmentPrimerTwoBinding.inflate(inflater, container, false)
		return binding.root
	}
	
	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		setUpViews()
		setUpClickListener()
	}
	
	private fun setUpViews() {
		val result = authViewModel.getLoginResponse()
		if (result != null && result.invitationCode.isNotEmpty()) {
			binding.profileImg.isVisible = true
			Glide.with(requireContext()).load(result.invitedUserPicture)
				.error(R.drawable.default_avatar)
				.into(binding.profileImg)
		} else {
			binding.profileImg.isVisible = false
		}
	}
	
	private fun setUpClickListener() {
		binding.startBtn.setOnClickListener {
			authViewModel.setBookMarkProgress(PHOTO_STAGE)
			goToUploadPhotoFrag()
		}
	}
	
	private fun goToUploadPhotoFrag() {
		replaceFragmentSafely(
			UploadPhotoFragment(),
			containerViewId = R.id.host_root,
			addToStack = false
		)
	}
	
	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}
}