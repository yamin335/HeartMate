package com.tovalue.me.dialog

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment
import com.tovalue.me.R
import com.tovalue.me.databinding.DateNightCatalogEventOfferDialogBinding
import com.tovalue.me.databinding.DateNightCatalogEventOfferDialogNewBinding
import com.tovalue.me.model.SendOfferDateNight
import com.tovalue.me.util.Utils
import java.text.SimpleDateFormat
import java.util.*

enum class TimeStates {
    START_TIME,
    END_TIME
}

class DateNightCatalogEventOfferNewDialog constructor(
    val callback: (SendOfferDateNight) -> Unit
) :
    DialogFragment() {


    private lateinit var vb: DateNightCatalogEventOfferDialogNewBinding

    private var selectedData: String = ""

    private var selectedStartDate: String = ""
    private var selectedStartTime: String = ""
    private var selectedEndDate: String = ""
    private var selectedEndTime: String = ""

    private var dateFormat = "yyyy-MM-dd"
    private var timeFormat = "hh:mm:ss"
    private var dateTimeFormat = "E, MMM dd, yyyy h:mm a"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        vb = DateNightCatalogEventOfferDialogNewBinding.inflate(inflater, container, false)
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

        //  vb.topics.text = topic
        selectedData = Utils.convertDateTimeFormat(c, Utils.month_day_year)
        //  vb.dateTV.text = Utils.convertDateTimeFormat(c,Utils.yearT_Month_day)

        selectedStartTime = Utils.convertDateTimeFormat(c, Utils.hour_min_second)
        vb.startTime.text = Utils.convertDateTimeFormat(c, Utils.hourT_min_second)


        c.add(Calendar.HOUR, 1)
        selectedEndTime = Utils.convertDateTimeFormat(c, Utils.hour_min_second)
        vb.endTime.text = Utils.convertDateTimeFormat(c, Utils.hourT_min_second)


    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun setOnClickListener() {

        /*? vb.dateTV.setOnClickListener {
             showDatePickerDialog()
         }*/

//        vb.startTime.setOnClickListener { showTimePickerDialog(TimeState.START_TIME) }
//        vb.endTime.setOnClickListener { showTimePickerDialog(TimeState.END_TIME) }

        vb.startTime.setOnClickListener {
            pickDateTime { cal ->
                selectedStartDate = SimpleDateFormat(timeFormat).format(cal.time)
                selectedStartTime = SimpleDateFormat(dateFormat).format(cal.time)
                val startTimeToShow = SimpleDateFormat(dateTimeFormat).format(cal.time)
                vb.startTime.text = startTimeToShow
            }
        }


        vb.endTime.setOnClickListener {
            pickDateTime { cal ->
                selectedEndDate = SimpleDateFormat(timeFormat).format(cal.time)
                selectedEndTime = SimpleDateFormat(dateFormat).format(cal.time)
                val startTimeToShow = SimpleDateFormat(dateTimeFormat).format(cal.time)
                vb.endTime.text = startTimeToShow
            }
        }



        vb.sendBtn.setOnClickListener {
            if (vb.title.text.isNotEmpty()) {
                if (vb.destination.text.isNotEmpty()) {
                    if (vb.detailET.text.isNotEmpty()) {
                        val sendOfferDateNight = SendOfferDateNight(
                            startDate = "$selectedStartDate",
                            startTime = "$selectedStartTime",
                            endDate = "$selectedEndDate",
                            endTime = "$selectedEndTime",
                            description = "${vb.detailET.text}",
                            destination = "${vb.destination.text}",
                            title = "${vb.title.text}",
                            url = "${vb.tvURL.text}"

                        )

                        callback.invoke(sendOfferDateNight)

                        dismiss()
                    } else {
                        Toast.makeText(requireContext(), "Please Enter Detail", Toast.LENGTH_SHORT)
                            .show()
                    }
                } else {
                    Toast.makeText(requireContext(), "Please Enter Destination", Toast.LENGTH_SHORT)
                        .show()
                }
            } else {
                Toast.makeText(requireContext(), "Please Enter Title", Toast.LENGTH_SHORT)
                    .show()
            }
        }


    }


/*    @RequiresApi(Build.VERSION_CODES.N)
    fun showDatePickerDialog() {

        val c: Calendar = Calendar.getInstance()
        val mYear = c.get(Calendar.YEAR)
        val mMonth = c.get(Calendar.MONTH)
        val mDay = c.get(Calendar.DAY_OF_MONTH)


        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { view, year, monthOfYear, dayOfMonth ->

                val cal = Calendar.getInstance()
                cal.set(year, monthOfYear + 1, dayOfMonth)

                selectedData = Utils.convertDateTimeFormat(cal, Utils.month_day_year)
                //  vb.dateTV.text = Utils.convertDateTimeFormat(cal,Utils.yearT_Month_day)
            }, mYear, mMonth, mDay
        )

        c.set(mYear, mMonth, mDay)
        val minDate = c.timeInMillis
        c.set(mYear, mMonth, mDay + 6)
        val maxDate = c.timeInMillis



        datePickerDialog.datePicker.minDate = minDate
        datePickerDialog.datePicker.maxDate = maxDate

        datePickerDialog.show()

    }

    fun showTimePickerDialog(timeView: TimeState) {


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

                    selectedStartTime = Utils.convertDateTimeFormat(c, Utils.hour_min_second)
                    vb.startTime.text = Utils.convertDateTimeFormat(c, Utils.hourT_min_second)

                } else if (timeView == TimeState.END_TIME) {

                    selectedEndTime = Utils.convertDateTimeFormat(c, Utils.hour_min_second)
                    vb.endTime.text = Utils.convertDateTimeFormat(c, Utils.hourT_min_second)

                }


                selectedEndTime = Utils.convertDateTimeFormat(c, Utils.hour_min_second)


            }, mHour, mMinute, false
        )
        timePickerDialog.show()

    }*/


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


}