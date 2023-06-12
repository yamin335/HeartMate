package com.tovalue.me.model

import com.google.gson.annotations.SerializedName

class UpdateMetaKeys {
	@SerializedName("status")
	var status: String = ""
	
	@SerializedName("level_1_score")
	var levelOneScore: String? = null
	
	@SerializedName("level_2_score")
	var levelTwoScore: String? = null
	
	@SerializedName("my_location")
	var location: String? = null
	
	@SerializedName("max_distance")
	var filterRadius: Int? = null
	
	@SerializedName("all_notifications")
	var enableAllNotification: Boolean? = null
	
	@SerializedName("new_discoveries")
	var notificationDiscovery: Boolean? = null
	
	@SerializedName("new_invitations")
	var notificationInvitation: Boolean? = null
	
	@SerializedName("new_mood_rings")
	var notificationNewMoodRings: Boolean? = null
	
	@SerializedName("promotion")
	var notificationPromotion: Boolean? = null
	
	@SerializedName("announcements")
	var notificationAnnouncement: Boolean? = null
	
	@SerializedName("email_all_notifications")
	var enableAllEmailNotification: Boolean? = null
	
	@SerializedName("email_new_discoveries")
	var emailDiscovery: Boolean? = null
	
	@SerializedName("email_new_invitations")
	var emailInvitation: Boolean? = null
	
	@SerializedName("email_mood_rings")
	var emailNewMoodRings: Boolean? = null
	
	@SerializedName("email_promotions")
	var emailPromotion: Boolean?= null
	
	@SerializedName("email_announcements")
	var emailAnnouncement: Boolean? = null
	
	@SerializedName("subscription_status")
	var subscriptionStatus: String? = null
	
	@SerializedName("subscription_type")
	var subscriptionType: String? = null
	
	@SerializedName("subscription_token")
	var subscriptionToken: String? = null
}
