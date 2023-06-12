package com.tovalue.me.ui.auth.primerone

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.tovalue.me.R
import com.tovalue.me.databinding.DialogFragmentAgeConfirmationBinding
import com.tovalue.me.util.Utils.getProperDateFormate

class AgeConfirmationDialogFragment : DialogFragment() {
	private var _binding: DialogFragmentAgeConfirmationBinding? = null
	private val binding get() = _binding!!
	
	private val userAgeDate: String by lazy { requireArguments().getString("date").toString() }
	private val age: Int by lazy { requireArguments().getInt("age") }
	
	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		// Inflate the layout for this fragment
		_binding = DialogFragmentAgeConfirmationBinding.inflate(inflater, container, false)
		return binding.root
	}
	
	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		setUpViews()
		setUpClickListener()
	}
	
	private fun setUpViews() {
		binding.ageTv.text =
			age.toString().plus(" ").plus(resources.getString(R.string.primer_one_years_old))
		binding.birthTv.text = "Born".plus(" ").plus(getProperDateFormate(userAgeDate))
	}
	
	
	private fun setUpClickListener() {
		binding.editAgeTv.setOnClickListener{
			dismiss()
		}
		binding.confirmAgeTv.setOnClickListener{
			dismiss()
		}
	}
	
	override fun onDestroy() {
		super.onDestroy()
		_binding = null
	}
}