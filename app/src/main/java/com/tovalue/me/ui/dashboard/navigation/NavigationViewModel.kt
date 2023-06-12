package com.tovalue.me.ui.dashboard.navigation

import androidx.lifecycle.ViewModel
import com.tovalue.me.ui.auth.AuthRepository
import com.tovalue.me.ui.dashboard.DashboardRepository
import com.tovalue.me.util.extensions.runIO
import com.tovalue.me.util.metaFilterValues

class NavigationViewModel: ViewModel() {
	private val repo = DashboardRepository()
	private val authRepo = AuthRepository()
	
	/*[@Refactor] meta values module | need separate object rather standalone values*/
	// api call could be cancel if scope finished [@refactor]
	fun updateMetaValues(allNotification: Boolean? = null,
						 discovery: Boolean? = null,
						 invitation: Boolean? = null,
						 memory: Boolean? = null,
						 promotion: Boolean? = null,
						 announcement: Boolean? = null,
						 allEmailNotification: Boolean? = null,
						 discoveryEmailNotification: Boolean? = null,
						 invitationEmailNotification: Boolean? = null,
						 memoryEmailNotification: Boolean? = null,
						 promotionEmailNotification: Boolean? = null,
						 announcementEmailNotification: Boolean? = null,
						 subscriotionStatus: String? = null,
						 subscriptionToken: String? = null,
						 subscriptionType: String? = null,
						 levelOneScore: Int? = null,
						 levelTwoScore: Int? = null,
						 location: String? = null,
						 eventMaxDistance: String? = null) {
		runIO {
			repo.updateMetaValues(
				allNotification,
				discovery,
				invitation,
				memory,
				promotion,
				announcement,
				allEmailNotification,
				discoveryEmailNotification,
				invitationEmailNotification,
				memoryEmailNotification,
				promotionEmailNotification,
				announcementEmailNotification,
				subscriotionStatus,
				subscriptionToken,
				subscriptionType,
				levelOneScore,
				levelTwoScore,
				location,
				eventMaxDistance
			)
		}


		if (allNotification != null) metaFilterValues?.enableAllNotification = allNotification
		if (discovery != null) metaFilterValues?.notificationDiscovery = discovery
		if (invitation != null) metaFilterValues?.notificationInvitation = invitation
		if (memory != null) metaFilterValues?.notificationNewMoodRings = memory
		if (promotion != null) metaFilterValues?.notificationPromotion = promotion
		if (announcement != null) metaFilterValues?.notificationAnnouncement = announcement
		if (allEmailNotification != null) metaFilterValues?.enableAllEmailNotification = allEmailNotification
		if (discoveryEmailNotification != null) metaFilterValues?.emailDiscovery = discoveryEmailNotification
		if (invitationEmailNotification != null) metaFilterValues?.emailInvitation = invitationEmailNotification
		if (memoryEmailNotification != null) metaFilterValues?.emailNewMoodRings = memoryEmailNotification
		if (promotionEmailNotification != null) metaFilterValues?.emailPromotion = promotionEmailNotification
		if (announcementEmailNotification != null) metaFilterValues?.emailAnnouncement = announcementEmailNotification
		if (levelOneScore != null) metaFilterValues?.levelOneScore = levelOneScore.toString()
		if (levelTwoScore != null) metaFilterValues?.levelTwoScore = levelTwoScore.toString()
	}
}