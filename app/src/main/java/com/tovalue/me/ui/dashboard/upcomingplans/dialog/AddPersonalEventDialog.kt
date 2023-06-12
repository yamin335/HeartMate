package com.tovalue.me.ui.dashboard.upcomingplans.dialog

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.tovalue.me.R
import com.tovalue.me.common.showToast
import com.tovalue.me.databinding.DialogFragmentAddPersonalEventBinding
import com.tovalue.me.ui.dashboard.upcomingplans.viewmodel.UpcomingPlansViewModel
import com.tovalue.me.util.DialogUtils
import java.text.SimpleDateFormat
import java.util.*

class AddPersonalEventDialog : DialogFragment() {


    private val upcomingPlansViewModel: UpcomingPlansViewModel by activityViewModels()

    private var _binding: DialogFragmentAddPersonalEventBinding? = null
    private val binding get() = _binding!!

    private var startTime = ""
    private var startDate = ""
    private var endTime = ""
    private var endDate = ""

    private var dateFormat = "yyyy-MM-dd"
    private var timeFormat = "hh:mm:ss"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogFragmentAddPersonalEventBinding.inflate(layoutInflater)
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

        binding.doneBtn.setOnClickListener {
            val title = binding.titleEditText.text.toString()
            if (isDataValid(title)) {
                DialogUtils.showDialog(requireContext(), false)
                upcomingPlansViewModel.scheduleMeTime(
                    startDate = startDate,
                    startTime = startTime,
                    endDate = endDate,
                    endTime = endTime
                )
            }
        }

        binding.backBtn.setOnClickListener {
            dismiss()
        }
        setUpObserver()

    }

    private fun setUpObserver() {
        upcomingPlansViewModel.scheduleMeTime.observe(viewLifecycleOwner) {
            when (it) {
                is UpcomingPlansViewModel.ScheduleMeTimeEvent.Error -> {
                    DialogUtils.hideDialog()
                    it.errorMsg?.let { it1 -> showToast(it1) }
                }
                is UpcomingPlansViewModel.ScheduleMeTimeEvent.DataResponse -> {
                    showToast(it.response.planStatus)
                    DialogUtils.hideDialog()
                    dismiss()
                }
            }
        }
    }

    private fun isDataValid(title: String): Boolean {
        if (title.isEmpty()) {
            showToast("Please enter title")
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.DialogStyle)
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


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}