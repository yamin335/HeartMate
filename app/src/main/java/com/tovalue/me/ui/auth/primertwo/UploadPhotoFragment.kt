package com.tovalue.me.ui.auth.primertwo

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.tovalue.me.R
import com.tovalue.me.common.showToast
import com.tovalue.me.databinding.FragmentUploadPhotoBinding
import com.tovalue.me.helper.PermissionHelper
import com.tovalue.me.helper.getFilePathFromUri
import com.tovalue.me.ui.auth.AuthViewModel
import com.tovalue.me.ui.dashboard.profile.UploadPhotoViewModel
import com.tovalue.me.util.Constants.LOCATION_STAGE
import com.tovalue.me.util.extensions.replaceFragmentSafely
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class UploadPhotoFragment : Fragment(){
	private var _binding: FragmentUploadPhotoBinding? = null
	private val binding get() = _binding!!
	private val authViewModel: AuthViewModel by activityViewModels()
	private val photoViewModel: UploadPhotoViewModel by viewModels()
	private val permissionHelper: PermissionHelper = PermissionHelper()
	private var isImageLoaded = false
	
	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		_binding = FragmentUploadPhotoBinding.inflate(inflater, container, false)
		return binding.root
	}
	
	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		setUpViews()
		setUpClickListener()
	}
	
	private fun setUpViews() {
		photoViewModel.avatar.observe(viewLifecycleOwner) {
			hideLoader()
			when(it) {
				is UploadPhotoViewModel.AvatarState.UploadedPhoto -> {
					showToast("Upload successful.")
					isImageLoaded = true
				}
				is UploadPhotoViewModel.AvatarState.Error -> it.error?.let { it1 -> showToast(it1) }
			}
		}
	}
	
	private fun setUpClickListener() {
		binding.nextImg.setOnClickListener {
			if (isImageLoaded) {
				authViewModel.setBookMarkProgress(LOCATION_STAGE)
				goToLocationFrag()
			}
			else showToast("Please upload the image.")
		}
		binding.photoPickerBtn.setOnClickListener {
			checkPermissions()
		}
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
		selectImageFromGallery()
	}
	
	private fun onPermissionDenied() {
		showToast("Permission Denied. ")
	}
	
	private fun goToLocationFrag() {
		replaceFragmentSafely(
			LocationFragment(),
			containerViewId = R.id.host_root,
			addToStack = false
		)
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
	
	private val galleryLauncher =
		registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
			if (result.resultCode == Activity.RESULT_OK && result.data != null) {
				val photoUri: Uri? = result.data!!.data
				val filePath = photoUri?.let { getFilePathFromUri(it, requireContext()) }
				if (filePath != null) {
					Glide.with(requireContext()).load(filePath).into(binding.uploadImage)
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
		showLoader()
		photoViewModel.uploadUserAvatar(requestFile)
	}
	
	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}
	
	private fun showLoader() {
		binding.loader.isVisible = true
	}
	
	private fun hideLoader() {
		binding.loader.isVisible = false
	}
	
}