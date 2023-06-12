package com.tovalue.me.ui.dashboard.setting

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.tovalue.me.R
import com.tovalue.me.databinding.FragmentEmailSettingBinding
import com.tovalue.me.ui.auth.AuthViewModel
import com.tovalue.me.ui.dashboard.navigation.NavigationViewModel
import com.tovalue.me.util.metaFilterValues
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class EmailSettingFragment : Fragment(){
	private var _binding: FragmentEmailSettingBinding? = null
	private val binding get() = _binding!!
	private val viewModel: NavigationViewModel by activityViewModels()
	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		_binding = FragmentEmailSettingBinding.inflate(inflater, container, false)
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		setUpViews()
		setUpClickListener()
	}

	private fun setUpViews() {
		binding.topBarLayout.topBarTv.text = resources.getString(R.string.setting_emails)
		checkSavedValues()
	}

	// [metaFilterVales] @UpdateMetaValue is Global Object to manage the values for now
	private fun checkSavedValues() {
		if (metaFilterValues != null) {
			if (metaFilterValues?.enableAllEmailNotification != null)
				binding.allEmailSwitch.isChecked = metaFilterValues?.enableAllEmailNotification!!
			if (metaFilterValues?.emailDiscovery != null)
				binding.discoverySwitch.isChecked = metaFilterValues?.emailDiscovery!!
			if (metaFilterValues?.emailInvitation != null)
				binding.invitationSwitch.isChecked = metaFilterValues?.emailInvitation!!
			if (metaFilterValues?.emailNewMoodRings != null)
				binding.memorySwitch.isChecked = metaFilterValues?.emailNewMoodRings!!
			if (metaFilterValues?.emailPromotion != null)
				binding.promotionSwitch.isChecked = metaFilterValues?.emailPromotion!!
			if (metaFilterValues?.emailAnnouncement != null)
				binding.announcementSwitch.isChecked = metaFilterValues?.emailAnnouncement!!
		}
	}

	private fun setUpClickListener() {
//		binding.allEmailSwitch.setOnCheckedChangeListener(this)
//		binding.discoverySwitch.setOnCheckedChangeListener(this)
//		binding.invitationSwitch.setOnCheckedChangeListener(this)
//		binding.memorySwitch.setOnCheckedChangeListener(this)
//		binding.promotionSwitch.setOnCheckedChangeListener(this)
//		binding.announcementSwitch.setOnCheckedChangeListener(this)

		binding.allEmailSwitch.setOnClickListener {
			if (!binding.allEmailSwitch.isChecked){
				disableAllSwitches()
			}
			if (binding.allEmailSwitch.isChecked){
				enableAllSwitches()
			}
			binding.allEmailSwitch.isChecked.also {
				binding.allEmailSwitch.isChecked = it
			}
			sendValues()
		}

		binding.discoverySwitch.setOnClickListener {
			if (!binding.allEmailSwitch.isChecked) {
				binding.discoverySwitch.isChecked = false
			} else binding.discoverySwitch.isChecked.also {
				binding.discoverySwitch.isChecked = it
			}
			sendValues()
		}

		binding.memorySwitch.setOnClickListener {
			if (!binding.allEmailSwitch.isChecked){
				binding.memorySwitch.isChecked = false
			}
			else binding.memorySwitch.isChecked.also {
				binding.memorySwitch.isChecked = it
			}
			sendValues()
		}

		binding.invitationSwitch.setOnClickListener {
			if (!binding.allEmailSwitch.isChecked) binding.invitationSwitch.isChecked = false
			else binding.invitationSwitch.isChecked.also {
				binding.invitationSwitch.isChecked = it
			}
			sendValues()
		}

		binding.promotionSwitch.setOnClickListener {
			if (!binding.allEmailSwitch.isChecked) binding.promotionSwitch.isChecked = false
			else binding.promotionSwitch.isChecked.also {
				binding.promotionSwitch.isChecked = it
			}
			sendValues()
		}

		binding.announcementSwitch.setOnClickListener {
			if (!binding.allEmailSwitch.isChecked) binding.announcementSwitch.isChecked = false
			else binding.announcementSwitch.isChecked.also {
				binding.announcementSwitch.isChecked = it
			}
			sendValues()
		}

		binding.topBarLayout.topBarTv.setOnClickListener {
			lifecycleScope.launch {
				delay(400)
				requireActivity().onBackPressed()
			}
//			checkSwitchStates(
//				listOf(
//					binding.allEmailSwitch,
//					binding.discoverySwitch,
//					binding.invitationSwitch,
//					binding.memorySwitch,
//					binding.promotionSwitch,
//					binding.announcementSwitch
//				)
//			)
//			dashboardViewModel.isApiReuired = true
//			val metaValues = UpdatMetaKeys()
//			metaValues.enableAllEmailNotification = binding.allEmailSwitch.isChecked
//			metaValues.emailDiscovery = binding.discoverySwitch.isChecked
//			metaValues.emailInvitation = binding.invitationSwitch.isChecked
//			metaValues.emailMemory = binding.memorySwitch.isChecked
//			metaValues.emailPromotion = binding.promotionSwitch.isChecked
//			metaValues.emailAnnouncement = binding.announcementSwitch.isChecked
//			dashboardViewModel.onStateChangedAction(
//				DashboardViewModel.UiAction.Navigation(
//					DashboardViewModel.CurrentNavigationScreen.EMAIL_SETTING,
//					metaValues
//				)
//			)
		}
	}

//	private fun checkSwitchStates(list: List<SwitchCompat>): Boolean {
//		if (metaFilterValues == null) return  false
//		for (i in list.indices) {
//			if (metaFilterValues?.enableAllEmailNotification != null && list[i].id == binding.allEmailSwitch.id) {
//				if (meta)
//			}
//		}
//	}

	private fun sendValues() {
		Log.e("binding.allEmailSwitch.isChecked",binding.allEmailSwitch.isChecked.toString())
		Log.e("binding.discoverySwitch.isChecked",binding.discoverySwitch.isChecked.toString())
		Log.e("binding.invitationSwitch.isChecked",binding.invitationSwitch.isChecked.toString())
		Log.e("binding.memorySwitch.isChecked",binding.memorySwitch.isChecked.toString())
		Log.e("binding.promotionSwitch.isChecked",binding.promotionSwitch.isChecked.toString())
		Log.e("binding.announcementSwitch.isChecked",binding.announcementSwitch.isChecked.toString())
		viewModel.updateMetaValues(
			allEmailNotification = binding.allEmailSwitch.isChecked,
			discoveryEmailNotification = binding.discoverySwitch.isChecked,
			invitationEmailNotification = binding.invitationSwitch.isChecked,
			memoryEmailNotification = binding.memorySwitch.isChecked,
			promotionEmailNotification = binding.promotionSwitch.isChecked,
			announcementEmailNotification = binding.announcementSwitch.isChecked
		)
	}

	private fun enableAllSwitches() {
		binding.discoverySwitch.isChecked = true
		binding.invitationSwitch.isChecked = true
		binding.memorySwitch.isChecked = true
		binding.promotionSwitch.isChecked = true
		binding.announcementSwitch.isChecked = true
	}

	private fun disableAllSwitches() {
		binding.discoverySwitch.isChecked = false
		binding.invitationSwitch.isChecked = false
		binding.memorySwitch.isChecked = false
		binding.promotionSwitch.isChecked = false
		binding.announcementSwitch.isChecked = false
	}

	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}
}