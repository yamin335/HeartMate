package com.tovalue.me.util

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import com.tovalue.me.R
import java.util.*

class DatePickerDialogFragment constructor(
    private val listener: (Int, Int, Int) -> Unit,
    private val day: Int?,
    private val month: Int?,
    private val year: Int?,
    private val minDate: Long? = null,
    private val maxDate: Long? = null
) : DialogFragment(), DatePickerDialog.OnDateSetListener {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Use the current date as the default date in the picker
        val calendar = Calendar.getInstance()
        val year = year ?: calendar.get(Calendar.YEAR)
        val month =  month ?: calendar.get(Calendar.MONTH)
        val day = day ?: calendar.get(Calendar.DAY_OF_MONTH)

        val dialog = DatePickerDialog(requireContext(), R.style.DatePickerTheme, this, year, month, day)

        minDate?.let {
            dialog.datePicker.minDate = it
        }

        maxDate?.let {
            dialog.datePicker.maxDate = it
        }

        // Create a new instance of DatePickerDialog and return it
        return dialog
    }

    override fun onDateSet(view: DatePicker, year: Int, month: Int, day: Int) {
        // Do something with the date chosen by the user
        listener.invoke(year, month + 1, day)
    }
}
