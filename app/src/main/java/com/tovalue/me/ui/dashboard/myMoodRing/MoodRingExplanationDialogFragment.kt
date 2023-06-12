package com.tovalue.me.ui.dashboard.myMoodRing

import android.app.Dialog
import android.os.Bundle
import android.view.Window
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDialogFragment
import com.tovalue.me.databinding.DialogFragmentMoodRingExplanationBinding
import com.tovalue.me.util.*


class MoodRingExplanationDialogFragment internal constructor(
    private val emotionalValue: Int, private val mentalValue: Int,
    private val physicalValue: Int, private val communalValue: Int,
    private val professionalValue: Int, private val spiritualValue: Int
): AppCompatDialogFragment() {

    private lateinit var binding: DialogFragmentMoodRingExplanationBinding
    /** The system calls this only when creating the layout in a dialog. */
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())

        val inflater = requireActivity().layoutInflater
        // Inflate the layout for this fragment
        binding = DialogFragmentMoodRingExplanationBinding.inflate(inflater)

        binding.btnClose.setOnClickListener {
            dismiss()
        }

        binding.labelEmotional.text = "Emotionally I am at $emotionalValue% because"
        binding.labelMental.text = "Mentally I am at $mentalValue% because"
        binding.labelSpiritual.text = "Spiritually I am at $spiritualValue% because"
        binding.labelCommunal.text = "Communally I am at $communalValue% because"
        binding.labelPhysical.text = "Physically I am at $physicalValue% because"
        binding.labelProfessional.text = "Professionally I am at $professionalValue% because"

        binding.etEmotional.setText(emotionallyExplanation)
        binding.etMental.setText(mentallyExplanation)
        binding.etSpiritual.setText(spirituallyExplanation)
        binding.etCommunal.setText(communallyExplanation)
        binding.etPhysical.setText(physicallyExplanation)
        binding.etProfessional.setText(professionallyExplanation)

        binding.checkBoxIncludeEmotional.isChecked = emotionallyExplanation.isNotBlank()
        binding.checkBoxIncludeMental.isChecked = mentallyExplanation.isNotBlank()
        binding.checkBoxIncludeSpiritual.isChecked = spirituallyExplanation.isNotBlank()
        binding.checkBoxIncludeCommunal.isChecked = communallyExplanation.isNotBlank()
        binding.checkBoxIncludePhysical.isChecked = physicallyExplanation.isNotBlank()
        binding.checkBoxIncludeProfessional.isChecked = professionallyExplanation.isNotBlank()

        binding.btnSave.setOnClickListener {
            emotionallyExplanation = if (binding.checkBoxIncludeEmotional.isChecked) {
                binding.etEmotional.text.toString()
            } else {
                ""
            }

            mentallyExplanation = if (binding.checkBoxIncludeMental.isChecked) {
                binding.etMental.text.toString()
            } else {
                ""
            }

            spirituallyExplanation = if (binding.checkBoxIncludeSpiritual.isChecked) {
                binding.etSpiritual.text.toString()
            } else {
                ""
            }

            communallyExplanation = if (binding.checkBoxIncludeCommunal.isChecked) {
                binding.etCommunal.text.toString()
            } else {
                ""
            }

            physicallyExplanation = if (binding.checkBoxIncludePhysical.isChecked) {
                binding.etPhysical.text.toString()
            } else {
                ""
            }

            professionallyExplanation = if (binding.checkBoxIncludeProfessional.isChecked) {
                binding.etProfessional.text.toString()
            } else {
                ""
            }
            dismiss()
        }

        builder.setView(binding.root)
        val dialog = builder.create()
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        return dialog
    }
}