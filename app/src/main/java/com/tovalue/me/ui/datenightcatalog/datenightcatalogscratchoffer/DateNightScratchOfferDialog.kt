package com.tovalue.me.ui.datenightcatalog.datenightcatalogscratchoffer

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.tovalue.me.R
import com.tovalue.me.common.showToast
import com.tovalue.me.databinding.DialogDateNightScratchOfferBinding
import com.tovalue.me.helper.PermissionHelper
import com.tovalue.me.helper.getFilePathFromUri
import com.tovalue.me.model.datingJourney.HomeJourney
import com.tovalue.me.ui.dashboard.home.HomeViewModel
import com.tovalue.me.util.DialogUtils
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class DateNightScratchOfferDialog(
    private val homeJourney:HomeJourney,
    private val dismissListener: () -> Unit
) : DialogFragment() {
    private var _binding: DialogDateNightScratchOfferBinding? = null
    private val binding get() = _binding!!

    private var startTime = ""
    private var startDate = ""
    private var endTime = ""
    private var endDate = ""

    private var dateFormat = "yyyy-MM-dd"
    private var timeFormat = "hh:mm:ss"

    private val permissionHelper: PermissionHelper = PermissionHelper()
    private val homeViewModel: HomeViewModel by activityViewModels()
    private var fileAttachmentPath: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.DialogStyle)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogDateNightScratchOfferBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.startTimeEditText.setOnClickListener {
            pickDateTime {
                startTime = SimpleDateFormat(timeFormat).format(it.time)
                startDate = SimpleDateFormat(dateFormat).format(it.time)
                val startTimeToShow = SimpleDateFormat("E, MMM dd, yyyy h:mm a").format(it.time)
                binding.startTimeEditText.text = startTimeToShow
            }
        }

        binding.endTimeEditText.setOnClickListener {
            pickDateTime {
                endTime = SimpleDateFormat(timeFormat).format(it.time)
                endDate = SimpleDateFormat(dateFormat).format(it.time)
                val endTimeToShow = SimpleDateFormat("E, MMM dd, yyyy h:mm a").format(it.time)
                binding.endTimeEditText.text = endTimeToShow
            }
        }

        binding.llUploadImage.setOnClickListener {
            checkPermissions()
        }

        binding.doneBtn.setOnClickListener {

            val title = binding.titleEditText.text.toString()
            val eventAddress = binding.destinationEditText.text.toString()
            val url = binding.urlEditText.text.toString()
            val eventDescription = binding.detailsEditText.text.toString()

            val dateNightId = 4
            val dateWeekId = homeJourney.week_id
            val inventoryTopic = "career_goals"
            val partnerId = homeJourney.partner_id
            val groupId = homeJourney.group_id

            val file = fileAttachmentPath?.let { it1 -> File(it1) }
            val fileBody = file?.asRequestBody("text/plain; charset=utf-8".toMediaType())
            val requestFile: MultipartBody.Part? =
                fileBody?.let { it1 ->
                    MultipartBody.Part.createFormData(
                        "attachment", file.name,
                        it1
                    )
                }

            if (isDataValid(title, eventAddress, eventDescription, requestFile)) {
                DialogUtils.showDialog(requireContext(), false)
                requestFile?.let { it1 ->
                    homeViewModel.createOfferDateNight(
                        eventDescription = eventDescription,
                        dateNightId = dateNightId,
                        dateWeekId = dateWeekId,
                        inventoryTopic = inventoryTopic,
                        partnerId = partnerId,
                        title = title,
                        startDate = startDate,
                        startTime = startTime,
                        endDate = endDate,
                        endTime = endTime,
                        eventAddress = eventAddress,
                        groupId = groupId,
                        url = url,
                        attachment = it1
                    )
                }
            }
        }
        setUpObserver()
    }

    private fun setUpObserver() {
        homeViewModel.dateNightOffer.observe(viewLifecycleOwner) {
            when (it) {
                is HomeViewModel.DateNightOfferEvent.Error -> {
                    DialogUtils.hideDialog()
                    val message = it.errorMsg ?: "Error Occurred!"
                    showToast(message)
                }
                is HomeViewModel.DateNightOfferEvent.DataResponse -> {
                    val message = it.response.whatHappened ?: "Offer Send Successful!"
                    showToast(message)
                    DialogUtils.hideDialog()
                    dismissListener.invoke()
                    dismiss()
                }
            }
        }
    }

    private fun isDataValid(
        title: String,
        destination: String,
        details: String,
        attachment: MultipartBody.Part?
    ): Boolean {
        if (title.isEmpty()) {
            showToast("Please enter title")
            return false
        } else if (destination.isEmpty()) {
            showToast("Please enter destination")
            return false
        } else if (details.isEmpty()) {
            showToast("Please enter details")
            return false
        } else if (attachment == null) {
            showToast("Please enter event image")
            return false
        } else if (startDate.isEmpty() or startTime.isEmpty()) {
            showToast("Please enter start time")
            return false
        } else if (endDate.isEmpty() or endTime.isEmpty()) {
            showToast("Please enter end time")
            return false
        }
        return true
    }

    private fun pickDateTime(callback: (Calendar) -> Unit) {
        val currentDateTime = Calendar.getInstance()
        val startYear = currentDateTime.get(Calendar.YEAR)
        val startMonth = currentDateTime.get(Calendar.MONTH)
        val startDay = currentDateTime.get(Calendar.DAY_OF_MONTH)
        val startHour = currentDateTime.get(Calendar.HOUR_OF_DAY)
        val startMinute = currentDateTime.get(Calendar.MINUTE)

        DatePickerDialog(requireContext(), R.style.DatePickerTheme, { _, year, month, day ->
            TimePickerDialog(requireContext(), R.style.DatePickerTheme, { _, hour, minute ->
                val pickedDateTime = Calendar.getInstance()
                pickedDateTime.set(year, month, day, hour, minute)
                callback.invoke(pickedDateTime)
            }, startHour, startMinute, false).show()
        }, startYear, startMonth, startDay).show()
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
        showToast("Permission Denied.")
    }

    @SuppressLint("IntentReset")
    private fun selectImageFromGallery() {
        Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI).also {
            it.type = "image/*"
            val imageType = arrayOf("image/jpeg", "image/png")
            it.putExtra(Intent.EXTRA_MIME_TYPES, imageType)
            galleryLauncher.launch(it)
        }
    }


    private val galleryLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK && result.data != null) {
                val photoUri: Uri? = result.data!!.data
                val filePath = photoUri?.let { getFilePathFromUri(it, requireContext()) }
                if (filePath != null) {
                    Glide.with(requireActivity()).load(filePath).into(binding.uploadIv)
                    fileAttachmentPath = filePath
                }
            }
        }

}