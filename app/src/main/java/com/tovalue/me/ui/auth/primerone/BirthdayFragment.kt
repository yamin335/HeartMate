package com.tovalue.me.ui.auth.primerone

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.*
import com.tovalue.me.R
import com.tovalue.me.common.showToast
import com.tovalue.me.databinding.FragmentBirthdayBinding
import com.tovalue.me.ui.auth.AuthViewModel
import com.tovalue.me.util.Constants
import com.tovalue.me.util.Constants.AGE_DIALOG_RESULT_CODE
import com.tovalue.me.util.Constants.PASSWORD_STAGE
import com.tovalue.me.util.Utils.getYearDifference
import com.tovalue.me.util.extensions.replaceFragmentSafely

class BirthdayFragment : Fragment() {
	private var _binding: FragmentBirthdayBinding? = null
	private val binding get() = _binding!!
	private val authViewModel: AuthViewModel by activityViewModels()
	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		_binding = FragmentBirthdayBinding.inflate(inflater, container, false)
		return binding.root
	}
	
	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		setUpClickListener()
	}
	
	private fun setUpClickListener() {
		binding.nextImg.setOnClickListener {
			val age = getYearDifference(binding.datePicker.year)
			val date = getUserSelectedDate()
			setUserAge(age)
			validateAge(age, date)
		}
	}
	
	private fun validateAge(age: Int, date: String) {
		if (age > 18) {
			authViewModel.setBookMarkProgress(Constants.NOTIFICATION_STAGE)
			authViewModel.saveBirthDate(date)
			goToNotificationFrag()
		}
		else showToast("You must me 18+")
	}
	
	private fun goToDialogFrag(date: String, age: Int) {
//		val dialogFrag = AgeConfirmationDialogFragment()
//		val bundle = Bundle()
//		bundle.putString(DATE_KEY, date)
//		bundle.putInt(AGE_KEY, age)
//		dialogFrag.arguments = bundle
//		dialogFrag.show(parentFragmentManager, DialogFragment::class.simpleName)
	}
	
	
	
	private fun setUserAge(age: Int) {
		binding.ageTv.text =
			resources.getString(R.string.primer_one_birthday_age).plus(" ").plus(age)
	}
	
	private fun getUserSelectedDate(): String {
		val month = binding.datePicker.month.toString()
		val day = binding.datePicker.dayOfMonth.toString()
		val year = binding.datePicker.year.toString()
		return year.plus("-").plus(month).plus("-").plus(day)
	}
	
	
	
	private fun goToPasswordFrag() {
		replaceFragmentSafely(
			PasswordFragment(),
			containerViewId = R.id.host_root,
			addToStack = false
		)
	}
	
	private fun goToNotificationFrag() {
		replaceFragmentSafely(
			NotificationLandingFragment(),
			containerViewId = R.id.host_root,
			addToStack = false
		)
	}
	
	override fun onDestroy() {
		super.onDestroy()
		_binding = null
	}
	companion object {
		const val DATE_KEY = "date"
		const val AGE_KEY = "age"
	}
}