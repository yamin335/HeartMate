package com.tovalue.me.ui.dashboard.relationshipGoals

import android.R
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.DialogFragment
import com.tovalue.me.databinding.DialogFragmentRelationshipGoalDeadlineAssignmentBinding
import com.tovalue.me.util.DateFormat
import com.tovalue.me.util.DateFormatterUtils
import com.tovalue.me.util.DatePickerDialogFragment
import java.util.*


class RelationshipGoalDeadlineAssignmentDialogFragment: DialogFragment() {

    private lateinit var binding: DialogFragmentRelationshipGoalDeadlineAssignmentBinding
    /** The system calls this only when creating the layout in a dialog. */

    override fun getTheme(): Int {
        return com.tovalue.me.R.style.CustomDialogFragment
    }

    override fun onResume() {
        super.onResume()

        val params = dialog?.window?.attributes
        params?.width = WindowManager.LayoutParams.MATCH_PARENT
        params?.height = WindowManager.LayoutParams.WRAP_CONTENT

        dialog?.window?.attributes = params
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window?.requestFeature(Window.FEATURE_NO_TITLE)
        return dialog
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        // Inflate the layout for this fragment
        binding = DialogFragmentRelationshipGoalDeadlineAssignmentBinding.inflate(inflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnClose.setOnClickListener {
            dismiss()
        }

        binding.btnUpdate.setOnClickListener {
            dismiss()
        }

        binding.etDeadline.setOnClickListener {
            showDatePicker()
        }

        val adapter = ArrayAdapter(requireContext(), R.layout.simple_list_item_1, arrayOf("Partner-1", "Partner-2", "Partner-3"))
        binding.spinnerPartners.adapter = adapter

        binding.spinnerPartners.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapter: AdapterView<*>, v: View?, i: Int, lng: Long) {

            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {}
        }

    }

    private fun showDatePicker() {
        val calender = Calendar.getInstance()
        calender.time = Date()
        DatePickerDialogFragment({ year, month, day ->
            val dateString = "${year}-${(if (month < 10) "0$month" else month)}-${(if (day < 10) "0$day" else day)}"
            val date = DateFormatterUtils.formatDateFromString(dateString, DateFormat.dateFormat2) ?: return@DatePickerDialogFragment
            binding.etDeadline.setText(DateFormatterUtils.formatDateToString(date, DateFormat.dateFormat3))
        }, calender.get(Calendar.DAY_OF_MONTH), calender.get(Calendar.MONTH), calender.get(Calendar.YEAR)
        ).show(childFragmentManager, "#DatePickerDialogFragment")
    }
}