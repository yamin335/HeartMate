package com.tovalue.me.model


import com.google.gson.annotations.SerializedName
import com.tovalue.me.ui.auth.primeriii.SideCategory
import com.tovalue.me.ui.auth.primeriii.spectrum.ProfileSpectrum

data class ProfileInfo(
    @SerializedName("avatar")
    val avatar: String,
    @SerializedName("date_number")
    val dateNumber: String,
    @SerializedName("subscription_status")
    val subscriptionStatus: String,
    @SerializedName("subscription_type")
    val subscriptionType: String,
    @SerializedName("match_source")
    val matchSources: ArrayList<String> = arrayListOf(),
    @SerializedName("displayname")
    val displayname: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("firstname")
    val firstname: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("lastname")
    val lastname: String,
    @SerializedName("level_1_score")
    val level1Score: String,
    @SerializedName("level_2_score")
    val level2Score: String,
    @SerializedName("location")
    val location: String,
    @SerializedName("max_distance")
    val maxDistance: String,
    @SerializedName("nickname")
    val nickname: String,
    @SerializedName("phone")
    val phone: String,
    @SerializedName("status")
    val status: String,
    @SerializedName("url")
    val url: String,
    @SerializedName("all_notifications")
    val allNotifications: Boolean,
    @SerializedName("announcements")
    val announcements: Boolean,
    @SerializedName("dating_availability")
    val datingAvailability: String,
    @SerializedName("email_all_notifications")
    val emailAllNotifications: Boolean,
    @SerializedName("email_announcements")
    val emailAnnouncements: Boolean,
    @SerializedName("email_new_discoveries")
    val emailNewDiscoveries: Boolean,
    @SerializedName("email_new_invitations")
    val emailNewInvitations: Boolean,
    @SerializedName("email_mood_rings")
    val emailNewMoodRings: Boolean,
    @SerializedName("email_promotions")
    val emailPromotions: Boolean,
    @SerializedName("new_invitations")
    val newInvitations: Boolean,
    @SerializedName("new_mood_rings")
    val newMoodRings: Boolean,
    @SerializedName("new_discoveries")
    val newDiscoveries: Boolean,
    @SerializedName("promotions")
    val promotions: Boolean,
    @SerializedName("inventory_categories")
    val inventoryCategories: List<SideCategory>,
    @SerializedName("profile_banner")
    val bannerInfo: ProfileBanner,
    @SerializedName("to_value_me")
    val toValueMe: UserProfileStats?,
    @SerializedName("active_dating_journeys")
    val activeDatingJourney: UserProfileStats?,
    @SerializedName("received_observations")
    val receivedObservations: UserProfileStats?,
    @SerializedName("daily_status")
    val dailyStatus: DailyStatus,
    @SerializedName("rhythm_of_life")
    val rhythmOfLife: RhythmOfLife,
    @SerializedName("icebreakers")
    val iceBreakers: IceBreakers,
    val spectrum: ProfileSpectrum,
    @SerializedName("invitations_array")
    val invitationArray: InvitationArray
) {
    @SerializedName("error")
    val error: String = ""
}

data class InvitationArray(
    @SerializedName("level_1")
    val level1: Level,

    @SerializedName("level_2")
    val level2: Level,

    @SerializedName("level_3")
    val level3: Level
)

data class Level(
    @SerializedName("invitation_sms_pt_one")
    val invitationSMSPtOne: String,

    @SerializedName("invitation_sms_pt_two")
    val invitationSMSPtTwo: String
)

data class UserProfileStats(
    @SerializedName("title")
    val title: String,
    @SerializedName("value")
    val value: Int?
)

data class DailyStatus(
    @SerializedName("title")
    val title: String,
    @SerializedName("button")
    val button: String
)

data class RhythmOfLife(
    @SerializedName("title")
    val title: String,
    @SerializedName("last_updated")
    val lastUpdated: String?,
    @SerializedName("background_image")
    val backgroundImage: String,
)

data class IceBreakers(
    @SerializedName("title")
    val title: String,
    @SerializedName("sub_heading")
    val subHeading: String,
    @SerializedName("background_image")
    val backgroundImage: String,
)