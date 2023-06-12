package com.tovalue.me.ui.auth.primertwo

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.tovalue.me.R
import com.tovalue.me.common.showToast
import com.tovalue.me.databinding.FragmentAvailabilityLandingBinding
import com.tovalue.me.ui.auth.AuthViewModel
import com.tovalue.me.ui.auth.primeriii.PrimerThreeFragment
import com.tovalue.me.ui.dashboard.DashboardActivity
import com.tovalue.me.util.Constants.PRIMERIII_STAGE
import com.tovalue.me.util.extensions.replaceFragmentSafely
import com.tovalue.me.util.livedata.EventObserver

class AvailabilityLandingFragment : Fragment(), CompoundButton.OnCheckedChangeListener {
	private var _binding: FragmentAvailabilityLandingBinding? = null
	private val binding get() = _binding!!
	private val authViewModel: AuthViewModel by activityViewModels()
	private val checkBoxList = ArrayList<String>()
	
	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		_binding = FragmentAvailabilityLandingBinding.inflate(inflater, container, false)
		return binding.root
	}
	
	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		setUpClickListener()
		observeState()
	}
	
	private fun observeState() {
		authViewModel.locationState.observe(viewLifecycleOwner, EventObserver {
			binding.loader.isVisible = false
			when(it) {
				is AuthViewModel.UiEvent.GotoNextScreen -> {
					// need to resume state from there for remaining registration screens
					authViewModel.setBookMarkProgress(PRIMERIII_STAGE)
//					MomensityBingoApp.preferencesManager!!.setBooleanValue(Constants.IS_USER_LOGGED_IN_CODE_KEY, true)
					// remove above line for now redirecting to the dashoard screen.
					
//					goToDashboardActivity()
					goToPrimerThreeFrag()
				}
				is AuthViewModel.UiEvent.Error -> {
					showToast(it.errorMsg.toString())
				}
			}
		})
	}
	
	private fun goToPrimerThreeFrag() {
		replaceFragmentSafely(
			PrimerThreeFragment(),
			containerViewId = R.id.host_root,
			addToStack = false
		)
	}
	
	private fun goToDashboardActivity() {
		context?.startActivity(Intent(context, DashboardActivity::class.java))
	}
	
	private fun setUpClickListener() {
		listOf(
			binding.availabilityLayout.mondayCb,
			binding.availabilityLayout.tuesdayCd,
			binding.availabilityLayout.wednesdayCb,
			binding.availabilityLayout.thursdayCb,
			binding.availabilityLayout.fridayCb,
			binding.availabilityLayout.saturdayCb,
			binding.availabilityLayout.sundayCb,
//			binding.availabilityLayout.allDayCb
		).forEach{
			it.setOnCheckedChangeListener(this)
		}
		
		binding.nextImg.setOnClickListener {
			if (checkBoxList.isEmpty()) showToast("Please select at least one option")
			else sendAvailabilityData()
		}
	}
	
	private fun sendAvailabilityData() {
		binding.loader.isVisible = true
		val commaSeparatedDays = checkBoxList.joinToString(separator = ",")
		authViewModel.updateLBA(commaSeparatedDays)
	}
	
	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}
	
	override fun onCheckedChanged(_btn: CompoundButton?, isChecked: Boolean) {
		_btn.let {
			when(it?.id) {
				binding.availabilityLayout.mondayCb.id -> {
					if (it.isChecked) checkBoxList.add(MONDAY)
					else checkBoxList.remove(MONDAY)
				}
				binding.availabilityLayout.tuesdayCd.id -> {
					if (it.isChecked) checkBoxList.add(TUESDAY)
					else checkBoxList.remove(TUESDAY)
				}
				binding.availabilityLayout.wednesdayCb.id -> {
					if (it.isChecked) checkBoxList.add(WEDNESDAY)
					else checkBoxList.remove(WEDNESDAY)
				}
				binding.availabilityLayout.thursdayCb.id -> {
					if (it.isChecked) checkBoxList.add(THURSDAY)
					else checkBoxList.remove(THURSDAY)
				}
				binding.availabilityLayout.fridayCb.id -> {
					if (it.isChecked) checkBoxList.add(FRIDAY)
					else checkBoxList.remove(FRIDAY)
				}
				binding.availabilityLayout.saturdayCb.id -> {
					if (it.isChecked) checkBoxList.add(SATURDAY)
					else checkBoxList.remove(SATURDAY)
				}
				binding.availabilityLayout.sundayCb.id -> {
					if (it.isChecked) checkBoxList.add(SUNDAY)
					else checkBoxList.remove(SUNDAY)
				}
//				binding.availabilityLayout.allDayCb.id -> {
//					if (it.isChecked) checkBoxList.add(ALL)
//					else checkBoxList.remove(ALL)
//				}
				else -> {
				
				}
			}
		}
	}
	
	companion object {
		const val MONDAY = "M"
		const val TUESDAY = "T"
		const val WEDNESDAY = "W"
		const val THURSDAY = "T"
		const val FRIDAY = "F"
		const val SATURDAY = "S"
		const val SUNDAY = "Su"
	}
}