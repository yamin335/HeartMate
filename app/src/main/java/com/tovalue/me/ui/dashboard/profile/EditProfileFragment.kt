package com.tovalue.me.ui.dashboard.profile

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.tovalue.me.R
import com.tovalue.me.common.showToast
import com.tovalue.me.databinding.FragmentEditProfileBinding
import com.tovalue.me.helper.MomensityBingoApp
import com.tovalue.me.helper.PermissionHelper
import com.tovalue.me.helper.getFilePathFromUri
import com.tovalue.me.model.ProfileInfo
import com.tovalue.me.util.Constants
import com.tovalue.me.util.extensions.animateVisibility
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class EditProfileFragment : Fragment()  {
	private var _binding: FragmentEditProfileBinding? = null
	private val binding get() = _binding!!
	private val viewModel: UploadPhotoViewModel by viewModels()
	private val permissionHelper: PermissionHelper = PermissionHelper()
	
	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		_binding = FragmentEditProfileBinding.inflate(inflater, container, false)
		return binding.root
	}
	
	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		setUpViews()
		setUpClickListener()
		setUpObserver()
	}
	
	private fun setUpObserver() {
		viewModel.avatar.observe(viewLifecycleOwner) {
			binding.loader.isVisible = false
			when(it) {
				is UploadPhotoViewModel.AvatarState.UploadedPhoto -> loadImage(it.avatar.full)
				is UploadPhotoViewModel.AvatarState.Error -> it.error?.let { it1 -> showToast(it1) }
			}
		}
	}
	
	private fun setUpClickListener() {
		binding.topBarLayout.topBarTv.setOnClickListener {
			requireActivity().onBackPressed()
		}
		binding.uploadPhotoTv.setOnClickListener {
			showBottomBar()
		}
		binding.bottomSheet.uploadLayout.setOnClickListener {
			checkPermissions()
		}
	}
	
	private fun showBottomBar() {
		binding.bottomSheet.uploadLayout.animateVisibility(View.VISIBLE)
	}
	
	private fun hideBottomBar() {
		binding.bottomSheet.uploadLayout.animateVisibility(View.GONE)
	}
	
	private fun setUpViews() {
		// put the preferences in data layer
		val profileInfo =  Gson().fromJson(
			MomensityBingoApp.preferencesManager?.getStringValue(Constants.USER_DATA_CODE_KEY),
			ProfileInfo::class.java
		)
		
		binding.lastNameTv.setText(profileInfo.displayname)
		Glide.with(requireActivity()).load(profileInfo.avatar).into(binding.profileImg)
		binding.topBarLayout.topBarTv.text = requireActivity().resources.getString(R.string.edit_profile)
	}
	
	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}
	
	private fun checkPermissions() {
		if (!permissionHelper.isGrantedStoragePermissions(requireContext())) {
			permissionHelper.checkStoragePermissions(
				binding.root,
				onPermissionDenied = ::onPermissionDenied,
				onPermissionGranted = ::onPermissionGranted
			)
			return
		}
		onPermissionGranted()
	}
	
	private fun onPermissionGranted() {
		hideBottomBar()
		selectImageFromGallery()
	}
	
	@SuppressLint("IntentReset")
	private fun selectImageFromGallery() {
		Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI).also {
			it.type = "image/*"
			val imageType = arrayOf("image/jpeg", "image/png")
			it.putExtra(Intent.EXTRA_MIME_TYPES,imageType)
			galleryLauncher.launch(it)
		}
	}
	
	// getting thumbnail only, need to save full picture for better image quality
	private val galleryLauncher =
		registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
			if (result.resultCode == Activity.RESULT_OK && result.data != null) {
				val photoUri: Uri? = result.data!!.data
				val filePath = photoUri?.let { getFilePathFromUri(it, requireContext()) }
				if (filePath != null) {
					Glide.with(requireActivity()).load(filePath).into(binding.profileImg)
					prepareFileForUpload(filePath)
				}
			}
		}
	
	private fun prepareFileForUpload(filePath: String) {
		val file = File(filePath)
		val fileBody = file.asRequestBody("text/plain; charset=utf-8".toMediaType())
		val requestFile: MultipartBody.Part =
			MultipartBody.Part.createFormData("avatar", file.name, fileBody)
		
		uploadUserSelectedAvatar(requestFile)
	}
	
	private fun uploadUserSelectedAvatar(requestFile: MultipartBody.Part) {
		binding.loader.isVisible = true
		viewModel.uploadUserAvatar(requestFile)
	}
	
	private fun onPermissionDenied() {
		showToast("Permission Denied.")
	}
	
	/*Need to make a separate class to load images*/
	private fun loadImage(imageUrl: String) {
		Glide.with(requireContext()).load(imageUrl)
			.error(R.drawable.default_avatar)
			.into(binding.profileImg)
	}
}