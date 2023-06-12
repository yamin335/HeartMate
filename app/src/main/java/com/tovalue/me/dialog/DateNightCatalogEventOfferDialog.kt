package com.tovalue.me.dialog

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.Nullable
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.bumptech.glide.util.Util
import com.google.android.gms.common.api.Status
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.tovalue.me.BuildConfig
import com.tovalue.me.R
import com.tovalue.me.common.showToast
import com.tovalue.me.databinding.DateNightCatalogEventOfferDialogBinding
import com.tovalue.me.helper.PermissionHelper
import com.tovalue.me.helper.getFilePathFromUri
import com.tovalue.me.model.SendOfferDateNight
import com.tovalue.me.util.Utils
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.util.*

enum class TimeState {
    START_TIME,
    END_TIME
}

class DateNightCatalogEventOfferDialog constructor(
    val topic: String,
    val callback: (SendOfferDateNight) -> Unit
) :
    DialogFragment() {


    private lateinit var vb: DateNightCatalogEventOfferDialogBinding

    private var selectedStartData: String = ""
    private var selectedEndData: String = ""
    private var selectedStartTime: String = ""
    private var selectedEndTime: String = ""
    private val permissionHelper: PermissionHelper = PermissionHelper()
    private var eventImg: MultipartBody.Part? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        vb = DateNightCatalogEventOfferDialogBinding.inflate(inflater, container, false)
        return vb.root
    }


    override fun getTheme(): Int {
        return R.style.DialogTheme
    }


    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setView()
        setOnClickListener()
    }

    private fun setView() {
        val c: Calendar = Calendar.getInstance()
//
        vb.topics.text = topic
//        selectedData = Utils.convertDateTimeFormat(c, Utils.month_day_year)
////        vb.dateTV.text = Utils.convertDateTimeFormat(c,Utils.yearT_Month_day)
//
//        selectedStartTime = Utils.convertDateTimeFormat(c, Utils.hour_min_second)
////        vb.startTime.text = Utils.convertDateTimeFormat(c,Utils.hourT_min_second)
//
//
//        c.add(Calendar.HOUR, 1)
//        selectedEndTime = Utils.convertDateTimeFormat(c, Utils.hour_min_second)
////        vb.endTime.text = Utils.convertDateTimeFormat(c,Utils.hourT_min_second)
//


    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun setOnClickListener() {
        Places.initialize(requireActivity(), BuildConfig.GOOGLE_MAPS_API_KEY)
        val fields = Arrays.asList(Place.Field.ID, Place.Field.NAME)

        vb.startDateTimeTV.setOnClickListener {
            showDatePickerDialog(TimeState.START_TIME)
        }
        vb.endDateTimeTV.setOnClickListener {
            showDatePickerDialog(TimeState.END_TIME)
        }

        vb.sendBtn.setOnClickListener {

            if (isValidateField()) {
                val sendOfferDateNight = SendOfferDateNight(
                    event_start_date = selectedStartData,
                    event_end_date = selectedEndData,
                    startTime = " $selectedStartTime",
                    endTime = " $selectedEndTime",
                    description = "${vb.detailET.text}",
                    title = "",
                    event_address = vb.destinationTv.text.toString(),
                    url = if (vb.urlTV.text.isEmpty()) "" else vb.urlTV.text.toString(),
                    eventImg = eventImg
                )

                callback.invoke(sendOfferDateNight)

                dismiss()
            }
        }
        vb.cancelBtn.setOnClickListener { dismiss() }

        vb.uploadImageEventLL.setOnClickListener {
            checkPermissions()
        }

        vb.destinationTv.setOnClickListener {
            //binding.frmContainer.visibility = VISIBLE
            val intent = Autocomplete.IntentBuilder(
                AutocompleteActivityMode.FULLSCREEN, fields
            )
                .build(requireActivity())
            startActivityForResult(intent, 101)
        }
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        @Nullable data: Intent?
    ) {

        if (requestCode == 101) {
            if (resultCode == Activity.RESULT_OK) {
                val place = Autocomplete.getPlaceFromIntent(data!!)

                val address = place.name
                vb.destinationTv.text = address
                // do query with address
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                // TODO: Handle the error.
                val status: Status = Autocomplete.getStatusFromIntent(data!!)
                Toast.makeText(
                    requireActivity(),
                    "Error: " + status.getStatusMessage(),
                    Toast.LENGTH_LONG
                ).show()
                Log.i("TAG-->", status.getStatusMessage()!!)
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.i("TAG-->", resultCode.toString())
                // The user canceled the operation.
            }
        }
    }

    private fun checkPermissions() {
        if (!permissionHelper.isGrantedStoragePermissions(requireContext())) {
            permissionHelper.checkStoragePermissions(
                vb.root,
                onPermissionDenied = ::onPermissionDenied,
                onPermissionGranted = ::onPermissionGranted
            )
            return
        }
        onPermissionGranted()
    }


    private fun isValidateField(): Boolean {

        if (vb.destinationTv.text.isEmpty()) {
            vb.destinationTv.error = resources.getString(R.string.destination_error)
            return false
        }

        if (vb.startDateTimeTV.text.isEmpty()) {
            vb.startDateTimeTV.error = resources.getString(R.string.startTimeError)
            return false
        }

        if (vb.endDateTimeTV.text.isEmpty()) {
            vb.endDateTimeTV.error = resources.getString(R.string.endTimeError)
            return false
        }

        if (vb.detailET.text.isEmpty()) {
            vb.detailET.error = resources.getString(R.string.detailError)
            return false
        }

        if(eventImg == null){
            showToast(R.string.uploadEventImageError)
            return false
        }

        return true
    }


    @RequiresApi(Build.VERSION_CODES.N)
    fun showDatePickerDialog(timeState: TimeState) {

        val c: Calendar = Calendar.getInstance()
        val mYear = c.get(Calendar.YEAR)
        val mMonth = c.get(Calendar.MONTH)
        val mDay = c.get(Calendar.DAY_OF_MONTH)


        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { view, year, monthOfYear, dayOfMonth ->

                val cal = Calendar.getInstance()
                cal.set(year, monthOfYear + 1, dayOfMonth)

                val selectedData = Utils.convertDateTimeFormat(cal, Utils.month_day_year)
                val selectedDateString = Utils.convertDateTimeFormat(cal, Utils.yearT_Month_day)

                showTimePickerDialog(timeState, selectedData, selectedDateString)

            }, mYear, mMonth, mDay
        )

        c.set(mYear, mMonth, mDay)
        val minDate = c.timeInMillis
        c.set(mYear, mMonth, mDay + 6)
        val maxDate = c.timeInMillis

        datePickerDialog.datePicker.minDate = minDate
        datePickerDialog.datePicker.maxDate = maxDate

        datePickerDialog.show()
        datePickerDialog.getButton(DatePickerDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
        datePickerDialog.getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK);

    }


    @SuppressLint("SetTextI18n")
    fun showTimePickerDialog(
        timeView: TimeState,
        selectedData: String,
        selectedDateString: String
    ) {


        // Get Current Time
        val c = Calendar.getInstance()
        val mHour = c[Calendar.HOUR_OF_DAY]
        val mMinute = c[Calendar.MINUTE]

        // Launch Time Picker Dialog

        // Launch Time Picker Dialog
        val timePickerDialog = TimePickerDialog(
            requireContext(),
            { view, hourOfDay, minute ->

                val c = Calendar.getInstance();
                c.set(Calendar.HOUR_OF_DAY, hourOfDay);
                c.set(Calendar.MINUTE, minute);


                if (timeView == TimeState.START_TIME) {

                    selectedStartData = selectedData
                    selectedStartTime = Utils.convertDateTimeFormat(c, Utils.hour_min_second)
                    vb.startDateTimeTV.text = "${selectedDateString}, ${
                        Utils.convertDateTimeFormat(
                            c,
                            Utils.hourT_min_second
                        )
                    }"

                } else if (timeView == TimeState.END_TIME) {
                    selectedEndData = selectedData
                    selectedEndTime = Utils.convertDateTimeFormat(c, Utils.hour_min_second)
                    vb.endDateTimeTV.text = "${selectedDateString}, ${
                        Utils.convertDateTimeFormat(
                            c,
                            Utils.hourT_min_second
                        )
                    }"
                }


                selectedEndTime = Utils.convertDateTimeFormat(c, Utils.hour_min_second)


            }, mHour, mMinute, false
        )
        timePickerDialog.show()
        timePickerDialog.getButton(DatePickerDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
        timePickerDialog.getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK);

    }


    private fun onPermissionGranted() {
        selectImageFromGallery()
    }

    // getting thumbnail only, need to save full picture for better image quality
    private val galleryLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK && result.data != null) {
                val photoUri: Uri? = result.data!!.data
                val filePath = photoUri?.let { getFilePathFromUri(it, requireContext()) }
                if (filePath != null) {
                    Glide.with(requireActivity()).load(filePath).into(vb.eventImageView)
                    vb.fileNameTV.setText(filePath.substring(filePath.lastIndexOf("/")+1))
                    prepareFileForUpload(filePath)
                }
            }
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


    private fun prepareFileForUpload(filePath: String) {
        val file = File(filePath)
        val fileBody = file.asRequestBody("text/plain; charset=utf-8".toMediaType())
        val requestFile: MultipartBody.Part =
            MultipartBody.Part.createFormData("avatar", file.name, fileBody)

        uploadUserSelectedAvatar(requestFile)
    }

    private fun uploadUserSelectedAvatar(requestFile: MultipartBody.Part) {
        eventImg = requestFile
    }


    private fun onPermissionDenied() {
        showToast("Permission Denied.")
    }


}