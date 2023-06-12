package com.tovalue.me.ui.dashboard.setting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.tovalue.me.R
import com.tovalue.me.databinding.FragmentPushSettingBinding
import com.tovalue.me.ui.dashboard.navigation.NavigationViewModel
import com.tovalue.me.util.metaFilterValues
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PushSettingFragment : Fragment(){
	private var _binding: FragmentPushSettingBinding? = null
	private val binding get() = _binding!!
	private val viewModel: NavigationViewModel by activityViewModels()

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		// Inflate the layout for this fragment
		_binding = FragmentPushSettingBinding.inflate(inflater, container, false)
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		setUpViews()
		setUpClickListener()
	}

	private fun setUpViews() {
		binding.topBarLayout.topBarTv.text = resources.getString(R.string.push_notification)
		checkSavedValues()
	}

	// [metaFilterVales] @UpdateMetaValue is Global Object to manage the values for now
	private fun checkSavedValues() {
		if (metaFilterValues != null) {
			if (metaFilterValues?.enableAllNotification != null)
				binding.allNotificationSwitch.isChecked = metaFilterValues?.enableAllNotification!!
			if (metaFilterValues?.notificationDiscovery != null)
				binding.discoverySwitch.isChecked = metaFilterValues?.notificationDiscovery!!
			if (metaFilterValues?.notificationInvitation != null)
				binding.invitationSwitch.isChecked = metaFilterValues?.notificationInvitation!!
			if (metaFilterValues?.notificationNewMoodRings != null)
				binding.memorySwitch.isChecked = metaFilterValues?.notificationNewMoodRings!!
			if (metaFilterValues?.notificationPromotion != null)
				binding.promotionSwitch.isChecked = metaFilterValues?.notificationPromotion!!
			if (metaFilterValues?.notificationAnnouncement != null)
				binding.announcementSwitch.isChecked = metaFilterValues?.notificationAnnouncement!!
		}
	}

	private fun setUpClickListener() {
//		binding.allNotificationSwitch.setOnCheckedChangeListener(this)
//		binding.discoverySwitch.setOnCheckedChangeListener(this)
//		binding.invitationSwitch.setOnCheckedChangeListener(this)
//		binding.memorySwitch.setOnCheckedChangeListener(this)
//		binding.promotionSwitch.setOnCheckedChangeListener(this)
//		binding.announcementSwitch.setOnCheckedChangeListener(this)
		binding.allNotificationSwitch.setOnClickListener {
			if (!binding.allNotificationSwitch.isChecked){
				disableAllSwitches()
			}
			if (binding.allNotificationSwitch.isChecked){
				enableAllSwitches()
			}
			binding.allNotificationSwitch.isChecked.also {
				binding.allNotificationSwitch.isChecked = it
			}
			sendValues()
		}

		binding.discoverySwitch.setOnClickListener {
			if (!binding.allNotificationSwitch.isChecked) {
				binding.discoverySwitch.isChecked = false
			} else binding.discoverySwitch.isChecked.also {
				binding.discoverySwitch.isChecked = it
			}
			sendValues()
		}

		binding.memorySwitch.setOnClickListener {
			if (!binding.allNotificationSwitch.isChecked) binding.memorySwitch.isChecked = false
			else binding.memorySwitch.isChecked.also {
				binding.memorySwitch.isChecked = it
			}
			sendValues()
		}

		binding.invitationSwitch.setOnClickListener {
			if (!binding.allNotificationSwitch.isChecked) binding.invitationSwitch.isChecked = false
			else binding.invitationSwitch.isChecked.also {
				binding.invitationSwitch.isChecked = it
			}
			sendValues()
		}

		binding.promotionSwitch.setOnClickListener {
			if (!binding.allNotificationSwitch.isChecked) binding.promotionSwitch.isChecked = false
			else binding.promotionSwitch.isChecked.also {
				binding.promotionSwitch.isChecked = it
			}
			sendValues()
		}

		binding.announcementSwitch.setOnClickListener {
			if (!binding.allNotificationSwitch.isChecked) binding.announcementSwitch.isChecked = false
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
		}
	}


/*	function should not have more than 3 parameters as it could be wrapped inside object but
	according to use case it could be like this*/

	private fun sendValues() {
		viewModel.updateMetaValues(
			allNotification = binding.allNotificationSwitch.isChecked,
			discovery = binding.discoverySwitch.isChecked,
			invitation = binding.invitationSwitch.isChecked,
			memory = binding.memorySwitch.isChecked,
			promotion = binding.promotionSwitch.isChecked,
			announcement = binding.announcementSwitch.isChecked,
		)
//		dashboardViewModel.isApiReuired = true
//		val metaValues = UpdatMetaKeys()
//		metaValues.enableAllNotification = binding.allNotificationSwitch.isChecked
//		metaValues.notificationDiscovery = binding.discoverySwitch.isChecked
//		metaValues.notificationInvitation = binding.invitationSwitch.isChecked
//		metaValues.notificationMemory = binding.memorySwitch.isChecked
//		metaValues.notificationPromotion = binding.promotionSwitch.isChecked
//		metaValues.notificationAnnouncement = binding.announcementSwitch.isChecked
//		dashboardViewModel.onStateChangedAction(
//			DashboardViewModel.UiAction.Navigation(
//				DashboardViewModel.CurrentNavigationScreen.PUSH_SETTING,
//				metaValues
//			)
//		)
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