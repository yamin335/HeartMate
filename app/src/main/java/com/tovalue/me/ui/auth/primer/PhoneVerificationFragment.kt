package com.tovalue.me.ui.auth.primer

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.tovalue.me.R
import com.tovalue.me.common.showToast
import com.tovalue.me.common.trimmedText
import com.tovalue.me.common.views.EditableKeyWacther
import com.tovalue.me.common.views.EditableTextWatcher
import com.tovalue.me.databinding.FragmentPhoneVerificationBinding
import com.tovalue.me.model.Oauth
import com.tovalue.me.ui.auth.AuthViewModel
import com.tovalue.me.ui.auth.primeriii.PrimerThreeFragment
import com.tovalue.me.ui.auth.primeriii.sides.LifeInventoryFragment
import com.tovalue.me.ui.auth.primerone.BirthdayFragment
import com.tovalue.me.ui.auth.primerone.EmailFragment
import com.tovalue.me.ui.auth.primerone.NotificationLandingFragment
import com.tovalue.me.ui.auth.primerone.PrimerOneFragment
import com.tovalue.me.ui.auth.primertwo.AvailabilityLandingFragment
import com.tovalue.me.ui.auth.primertwo.LocationFragment
import com.tovalue.me.ui.dashboard.DashboardActivity
import com.tovalue.me.util.Constants.AVAILABILITY_STAGE
import com.tovalue.me.util.Constants.BIRTHDAY_STAGE
import com.tovalue.me.util.Constants.EMAIL_STAGE
import com.tovalue.me.util.Constants.INVENTORY_STAGE
import com.tovalue.me.util.Constants.LOCATION_STAGE
import com.tovalue.me.util.Constants.NOTIFICATION_STAGE
import com.tovalue.me.util.Constants.Name_STAGE
import com.tovalue.me.util.Constants.PHOTO_STAGE
import com.tovalue.me.util.Utils.hideKeyboard
import com.tovalue.me.util.extensions.clearBackStack
import com.tovalue.me.util.extensions.replaceFragmentSafely
import com.tovalue.me.util.livedata.EventObserver
import java.util.concurrent.TimeUnit

class PhoneVerificationFragment : Fragment(){
	private var _binding: FragmentPhoneVerificationBinding? = null
	private val binding get() = _binding!!
	private val authViewModel: AuthViewModel by activityViewModels()
	
	private var storedVerificationId: String = ""
	private lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
	private lateinit var auth: FirebaseAuth
	private lateinit var phoneAuthCallaBacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
	
	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		_binding = FragmentPhoneVerificationBinding.inflate(inflater, container, false)
		return binding.root
	}
	
	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		setUpViews()
		setUpClickListener()
		observerEvents()
	}
	
	private fun observerEvents() {
		authViewModel.otpState.observe(viewLifecycleOwner,
			EventObserver {
				binding.loader.isVisible = false
				when(it) {
					is AuthViewModel.UiEventOtp.GotoPrimerScreen -> {
						clearBackStack()
						goToPrimerFrag()
					}
					is AuthViewModel.UiEventOtp.Data -> {
						filterData(it.userRecordStatus)
					}
					is AuthViewModel.UiEventOtp.Error -> {
						clearBackStack()
						showToast(it.errorMsg.toString())
					}
				}
			})
	}
	
	private fun filterData(record: Oauth) {
		if (record.invitationCode.isNotEmpty() && record.registrationStatus.isNotEmpty()) {
			clearBackStack()
			gotoSpecificScreen(record.registrationStatus)
			return
		}
		if (record.invitationCode.isNotEmpty() && record.registrationStatus.isEmpty()) {
			enabledLoggedInUser()
			goToDashboard()
			return
		}
		if (record.invitationCode.isEmpty() && record.registrationStatus.isNotEmpty()) {
			clearBackStack()
			gotoSpecificScreen(record.registrationStatus)
			return
		}
		if (record.invitationCode.isEmpty() && record.registrationStatus.isEmpty()) {
			enabledLoggedInUser()
			goToDashboard()
			return
		}
	}
	
	private fun enabledLoggedInUser() {
		authViewModel.setUserLoggedIn(true)
		authViewModel.setBookMarkProgress("")
	}
	
	private fun gotoSpecificScreen(screenStatus: String) {
		when(screenStatus) {
			Name_STAGE -> goToPrimerFrag()
			EMAIL_STAGE -> {
				authViewModel.setBookMarkProgress(EMAIL_STAGE)
				goToEmailFrag()
			}
			BIRTHDAY_STAGE -> {
				authViewModel.setBookMarkProgress(BIRTHDAY_STAGE)
				goToBirthdayFrag()
			}
			NOTIFICATION_STAGE -> {
				authViewModel.setBookMarkProgress(NOTIFICATION_STAGE)
				goToNotificationFrag()
			}
			PHOTO_STAGE -> {
				authViewModel.setBookMarkProgress(PHOTO_STAGE)
				goToUploadPhotoFrag()
			}
			LOCATION_STAGE -> {
				authViewModel.setBookMarkProgress(LOCATION_STAGE)
				goToLocationFrag()
			}
			AVAILABILITY_STAGE -> {
				authViewModel.setBookMarkProgress(AVAILABILITY_STAGE)
				goToAvailabilityFrag()
			}
			INVENTORY_STAGE -> {
				authViewModel.setBookMarkProgress(INVENTORY_STAGE)
				goToInventoryFrag()
			}
		}
	}
	
	private fun goToInventoryFrag() {
		replaceFragmentSafely(
			PrimerThreeFragment(),
			containerViewId = R.id.host_root,
			addToStack = false
		)
	}
	
	private fun goToLocationFrag() {
		replaceFragmentSafely(
			LocationFragment(),
			containerViewId = R.id.host_root,
			addToStack = false
		)
	}
	
	private fun goToAvailabilityFrag() {
		replaceFragmentSafely(
			AvailabilityLandingFragment(),
			containerViewId = R.id.host_root,
			addToStack = false
		)
	}
	
	private fun goToUploadPhotoFrag() {
		replaceFragmentSafely(
			PrimerOneFragment(),
			containerViewId = R.id.host_root,
			addToStack = false
		)
	}
	
	private fun goToEmailFrag() {
		replaceFragmentSafely(
			EmailFragment(),
			containerViewId = R.id.host_root,
			addToStack = false
		)
	}
	
	private fun goToBirthdayFrag() {
		replaceFragmentSafely(
			BirthdayFragment(),
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
	
	private fun goToDashboard() {
		context?.startActivity(Intent(context, DashboardActivity::class.java))
		requireActivity().finish()
	}
	
	private fun setUpViews() {
		binding.mobileNoTv.text =
			resources.getString(R.string.phone_send_to).plus(" ").plus(authViewModel.number)
		auth = Firebase.auth
		
		setUpPhoneAuthCallBack()
		startPhoneVerification()
	}
	
	private fun setUpClickListener() {
		binding.nextImg.setOnClickListener { verifyTheCode() }
		
		binding.numberAlertTv.setOnClickListener {
//			resendVerificationCode(
//				authViewModel.countryCode.plus(authViewModel.number),
//				resendToken
//			)
		}
		
		binding.editNumberTv.setOnClickListener {
		
		}
		
		binding.firstDigitEt.addTextChangedListener(
			EditableTextWatcher(binding.firstDigitEt, binding.secondDigitEt))
		binding.secondDigitEt.addTextChangedListener(
			EditableTextWatcher(binding.secondDigitEt, binding.thirdDigitEt))
		binding.thirdDigitEt.addTextChangedListener(
			EditableTextWatcher(binding.thirdDigitEt, binding.fourthDigitEt))
		binding.fourthDigitEt.addTextChangedListener(
			EditableTextWatcher(binding.fourthDigitEt, binding.fifthDigitEt))
		binding.fifthDigitEt.addTextChangedListener(
			EditableTextWatcher(binding.fifthDigitEt, binding.sixthDigitEt))
		binding.sixthDigitEt.addTextChangedListener(
			EditableTextWatcher(binding.sixthDigitEt, null))
		
		binding.firstDigitEt.setOnKeyListener(EditableKeyWacther(binding.firstDigitEt, null))
		binding.secondDigitEt.setOnKeyListener(EditableKeyWacther(binding.secondDigitEt, binding.firstDigitEt))
		binding.thirdDigitEt.setOnKeyListener(EditableKeyWacther(binding.thirdDigitEt, binding.secondDigitEt))
		binding.fourthDigitEt.setOnKeyListener(EditableKeyWacther(binding.fourthDigitEt, binding.thirdDigitEt))
		binding.fifthDigitEt.setOnKeyListener(EditableKeyWacther(binding.fifthDigitEt, binding.fourthDigitEt))
		binding.sixthDigitEt.setOnKeyListener(EditableKeyWacther(binding.sixthDigitEt, binding.fifthDigitEt))
	}
	
	private fun verifyTheCode() {
		val manualCode = binding.firstDigitEt.trimmedText
			.plus(binding.secondDigitEt.trimmedText)
			.plus(binding.thirdDigitEt.trimmedText)
			.plus(binding.fourthDigitEt.trimmedText)
			.plus(binding.fifthDigitEt.trimmedText)
			.plus(binding.sixthDigitEt.trimmedText)
		
		if (manualCode.isEmpty()) showToast("Please enter otp.")
		else verifyPhoneNumberWithCode(storedVerificationId, manualCode)
	}
	
	private fun goToPrimerFrag() {
		replaceFragmentSafely(
			PrimerOneFragment(),
			containerViewId = R.id.host_root,
			addToStack = false
		)
	}
	
	private fun setUpPhoneAuthCallBack() {
		phoneAuthCallaBacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
			override fun onVerificationCompleted(credentials: PhoneAuthCredential) {
				// auto code reterieve depends on devices and google services
				val secretCode = credentials.smsCode
				if (!secretCode.isNullOrBlank()) {
					filledCodeAuto(secretCode)
					signInWithPhoneAuthCredential(credentials)
				}
			}
			
			override fun onVerificationFailed(exception: FirebaseException) {
				// This callback is invoked in an invalid request for verification is made,
				// for instance if the the phone number format is not valid.
				
				if (exception is FirebaseAuthInvalidCredentialsException) {
					// Invalid request
					showToast(exception.message.toString())
				} else if (exception is FirebaseTooManyRequestsException) {
					// The SMS quota for the project has been exceeded
					showToast(exception.message.toString())
				}
			}
			
			override fun onCodeSent(
				verificationId: String,
				token: PhoneAuthProvider.ForceResendingToken
			) {
				// code sent to provider number
				storedVerificationId = verificationId
				resendToken = token
			}
		}
	}
	
	private fun filledCodeAuto(secretCode: String) {
		binding.firstDigitEt.setText(secretCode.substring(0, 1))
		binding.secondDigitEt.setText(secretCode.substring(1, 2))
		binding.thirdDigitEt.setText(secretCode.substring(2, 3))
		binding.fourthDigitEt.setText(secretCode.substring(3, 4))
		binding.fifthDigitEt.setText(secretCode.substring(4, 5))
		binding.sixthDigitEt.setText(secretCode.substring(5, 6))
	}
	
	private fun startPhoneVerification() {
		val options = PhoneAuthOptions.newBuilder(auth)
			.setPhoneNumber(authViewModel.countryCode.plus(authViewModel.number))
			.setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit 60 seconds
			.setActivity(requireActivity())
			.setCallbacks(phoneAuthCallaBacks)
			.build()
		PhoneAuthProvider.verifyPhoneNumber(options)
	}
	
	private fun resendVerificationCode(
		phoneNumber: String,
		token: PhoneAuthProvider.ForceResendingToken?
	) {
		val optionsBuilder = PhoneAuthOptions.newBuilder(auth)
			.setPhoneNumber(phoneNumber)       // Phone number to verify
			.setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
			.setActivity(requireActivity())                 // Activity (for callback binding)
			.setCallbacks(phoneAuthCallaBacks)          // OnVerificationStateChangedCallbacks
		if (token != null) {
			optionsBuilder.setForceResendingToken(token) // callback's ForceResendingToken
		}
		PhoneAuthProvider.verifyPhoneNumber(optionsBuilder.build())
	}
	
	private fun verifyPhoneNumberWithCode(verificationId: String, code: String) {
		if (verificationId.isNotEmpty()) {
			val credential = PhoneAuthProvider.getCredential(verificationId!!, code)
			signInWithPhoneAuthCredential(credential)
		} else {
			showToast("Incorrect code.")
		}
	}
	
	private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
		binding.loader.isVisible = true
		auth.signInWithCredential(credential).addOnCompleteListener(requireActivity()) { task ->
			if (task.isSuccessful) {
				val uid = task.result.user!!.uid
				createUser(uid)
			} else {
				if (task.exception is FirebaseAuthInvalidCredentialsException) {
					val exceptionMsg = task.exception
					showToast(exceptionMsg!!.message.toString())
					binding.loader.isVisible = false
				}
			}
		}
	}
	
	private fun createUser(uid: String) {
		hideKeyboard(requireActivity())
		val countryCode = authViewModel.countryCode!!.replace("+","")
		authViewModel.register(
			username = authViewModel.countryCode.plus(authViewModel.number.toString()),
			email = countryCode.plus(authViewModel.number).plus("@tovalueme.com"),
			password = uid.plus(countryCode).plus(authViewModel.number)
		)
	}
	
	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}
}