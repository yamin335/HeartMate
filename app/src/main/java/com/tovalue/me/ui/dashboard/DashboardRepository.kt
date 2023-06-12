package com.tovalue.me.ui.dashboard

import com.google.gson.Gson
import com.tovalue.me.helper.MomensityBingoApp
import com.tovalue.me.model.ProfileInfo
import com.tovalue.me.network.APIClient
import com.tovalue.me.network.safeApiCall
import com.tovalue.me.util.Constants
import com.tovalue.me.util.Constants.SUBSCRIPTION_PLAN_STATUS
import com.tovalue.me.util.Constants.SUBSCRIPTION_PLAN_TYPE
import com.tovalue.me.util.Constants.USER_DATA_CODE_KEY
import com.tovalue.me.util.metaFilterValues
import okhttp3.MultipartBody

class DashboardRepository {
    suspend fun getProfile(cache: String, id: Int) = safeApiCall {
        APIClient.aPIClient.getUserInfo(cacheParam = cache, userId = id)
    }

    suspend fun getNonce() = safeApiCall {
        APIClient.aPIClient.getNonceToken(Constants.CONTROLLER_NONCE, Constants.METHOD_NONCE_DELETE)
    }

    suspend fun deactivateAccount(nonce: String) = safeApiCall {
        APIClient.aPIClient.deactivateAccount(nonceValue = nonce)
    }

    suspend fun logout(token: String) = safeApiCall {
        APIClient.aPIClient.logout(token)
    }

    suspend fun getDatingJourney() = safeApiCall {
        APIClient.aPIClient.getDatingJourneys()
    }

    suspend fun getCorporateLinks() = safeApiCall {
        APIClient.aPIClient.getCorporateInfo()
    }

    suspend fun updateMetaValues(
        allNotification: Boolean?,
        discovery: Boolean?,
        invitation: Boolean?,
        memory: Boolean?,
        promotion: Boolean?,
        announcement: Boolean?,
        allEmailNotification: Boolean?,
        discoveryEmailNotification: Boolean?,
        invitationEmailNotification: Boolean?,
        memoryEmailNotification: Boolean?,
        promotionEmailNotification: Boolean?,
        announcementEmailNotification: Boolean?,
        subscriotionStatus: String?,
        subscriptionToken: String?,
        subscriptionType: String?,
        levelOneScore: Int?,
        levelTwoScore: Int?,
        location: String?,
        eventDuration: String?
    ) = safeApiCall {
        APIClient.aPIClient.updateUserMetaValues(
            notification = allNotification,
            discovery = discovery,
            invitation = invitation,
            newMoodRings = memory,
            promotion = promotion,
            announcement = announcement,
            allEmail = allEmailNotification,
            emailDiscovery = discoveryEmailNotification,
            emailInvitation = invitationEmailNotification,
            emailNewMoodRings = memoryEmailNotification,
            emailPromotion = promotionEmailNotification,
            emailAnnouncement = announcementEmailNotification,
            subscriptionStatus = subscriotionStatus,
            subscriptionToken = subscriptionToken,
            subscriptionType = subscriptionType,
            levelOneScore = levelOneScore,
            levelTwoScore = levelTwoScore,
            location = location,
            eventMaxDistance = eventDuration
        )
    }

    suspend fun sendDeviceToken(token: String) = safeApiCall {
        APIClient.aPIClient.updateUserMetaValues(deviceToken = token)
    }

    suspend fun levelOneInvitation(
        id: Int,
        number: String,
        firstName: String,
        invitationType: String,
        matchSource: String,
        groupLevel: Int,
    ) = safeApiCall {
        APIClient.aPIClient.levelOneInvitation(
            userId = id,
            inviteeNumber = number,
            firstName = firstName,
            invitationType = invitationType,
            matchSource = matchSource,
            groupLevel = groupLevel
        )
    }

    fun flushData() {
        MomensityBingoApp.preferencesManager!!.remove(Constants.USER_DATA_CODE_KEY)
        MomensityBingoApp.preferencesManager!!.remove(Constants.USER_ID_CODE_KEY)
        MomensityBingoApp.preferencesManager!!.remove(Constants.IS_USER_LOGGED_IN_CODE_KEY)
        MomensityBingoApp.preferencesManager!!.clear()
    }

    fun setUserObj(profile: ProfileInfo) {
        MomensityBingoApp.preferencesManager!!.setStringValue(
            USER_DATA_CODE_KEY,
            Gson().toJson(profile)
        )
    }

    // save only those values which reuired
    // [@refactor model]
    fun getUserObj(): ProfileInfo? {
        var profileInfo: ProfileInfo? = null
        MomensityBingoApp.preferencesManager?.let {
            profileInfo = Gson().fromJson(
                it.getStringValue(USER_DATA_CODE_KEY),
                ProfileInfo::class.java
            )
        }

        return profileInfo
    }

    // could be better if object could be added meta values in [@get_profile] api response
    fun setUserMetaValues(metaInfo: ProfileInfo) {
        metaFilterValues?.enableAllNotification = metaInfo.allNotifications
        metaFilterValues?.notificationDiscovery = metaInfo.newDiscoveries
        metaFilterValues?.notificationNewMoodRings = metaInfo.newMoodRings
        metaFilterValues?.notificationInvitation = metaInfo.newInvitations
        metaFilterValues?.notificationPromotion = metaInfo.promotions
        metaFilterValues?.notificationAnnouncement = metaInfo.announcements

        metaFilterValues?.enableAllEmailNotification = metaInfo.emailAllNotifications
        metaFilterValues?.emailDiscovery = metaInfo.emailNewDiscoveries
        metaFilterValues?.emailInvitation = metaInfo.emailNewInvitations
        metaFilterValues?.emailNewMoodRings = metaInfo.emailNewMoodRings
        metaFilterValues?.emailPromotion = metaInfo.emailPromotions
        metaFilterValues?.emailAnnouncement = metaInfo.emailAnnouncements

        metaFilterValues?.levelOneScore = metaInfo.level1Score
        metaFilterValues?.levelTwoScore = metaInfo.level2Score

    }

    suspend fun getDataNightCatalog(partnerUserId: Int, groupId: Int) = safeApiCall {

        APIClient.aPIClient.getPartnerDateNightCatalog(
            partnerUserId = partnerUserId,
            groupId = groupId,
            canSeePromoted = getIsPromoted()
        )

    }

    suspend fun getUnreadNotificationCount() = safeApiCall {
        APIClient.aPIClient.getUnreadNotificationCount()
    }

    suspend fun saveMoodRing(
        emotionally: Int, mentally: Int,
        physically: Int, communally: Int,
        professionally: Int, spiritually: Int,
        emotionally_explanation: String,
        mentally_explanation: String,
        spiritually_explanation: String,
        communally_explanation: String,
        physically_explanation: String,
        professionally_explanation: String
    ) = safeApiCall {
        APIClient.aPIClient.saveMoodRing(
            emotionally = emotionally,
            mentally = mentally,
            physically = physically,
            communally = communally,
            professionally = professionally,
            spiritually = spiritually,
            emotionally_explanation = emotionally_explanation,
            mentally_explanation = mentally_explanation,
            spiritually_explanation = spiritually_explanation,
            communally_explanation = communally_explanation,
            physically_explanation = physically_explanation,
            professionally_explanation = professionally_explanation
        )
    }

    suspend fun getMoodRingHistory(pageNumber: Int, forWeekEnding: String) = safeApiCall {
        APIClient.aPIClient.getMoodRingHistory(
            page_number = pageNumber, for_week_ending = forWeekEnding
        )
    }

    suspend fun getMoodRingHistoryDetails(moodRingId: Int) = safeApiCall {
        APIClient.aPIClient.getMoodRingHistoryDetails(mood_ring_id = moodRingId)
    }


    fun getIsPromoted(): Int {
        return if (MomensityBingoApp.preferencesManager!!.getStringValue(Constants.SUBSCRIPTION_PLAN_STATUS)
                .equals(Constants.ACTIVE)
        ) 1 else 0

    }

    suspend fun updateSubscriptionStatus(
        subscriptionStatus: String,
        subscriptionType: String,
        subscriptionExpiryDate: String,
        subscriptionToken: String
    ) = safeApiCall {

        APIClient.aPIClient.updateSubscriptionStatus(
            subscriptionStatus = subscriptionStatus,
            subscriptionType = subscriptionType,
            subscriptionExpiryDate = subscriptionExpiryDate,
            subscriptionToken = subscriptionToken
        )
    }

    suspend fun createOfferDateNight(
        eventDescription: String,
        dateNightId: Int,
        dateWeekId: Int,
        inventoryTopic: String,
        partnerId: Int,
        title: String,
        startDate: String,
        startTime: String,
        endDate: String,
        endTime: String,
        eventAddress: String,
        groupId: Int,
        url: String,
        attachment: MultipartBody.Part
    ) =
        safeApiCall {
            APIClient.aPIClient.createOfferDateNight(
                eventDescription = eventDescription,
                dateNightId = dateNightId,
                dateWeekId = dateWeekId,
                inventoryTopic = inventoryTopic,
                partnerId = partnerId,
                title = title,
                startDate = startDate,
                startTime = startTime,
                endDate = endDate,
                endTime = endTime,
                eventAddress = eventAddress,
                groupId = groupId,
                url = url,
                file = attachment
            )
        }

    suspend fun getMyRelationshipPlans() = safeApiCall {
        APIClient.aPIClient.getMyRelationshipPlans()
    }

    suspend fun getMyRelationshipPlanDetails(planId: Int?) = safeApiCall {
        APIClient.aPIClient.getMyRelationshipPlanDetails(planId = planId)
    }

    suspend fun getAvailableRelationshipPlans() = safeApiCall {
        APIClient.aPIClient.getAvailableRelationshipPlans()
    }

    suspend fun updateMyRelationshipPlanProgress(planId: Int?, taskIds: String?) = safeApiCall {
        APIClient.aPIClient.updateMyRelationshipPlanProgress(planId = planId, taskIds = taskIds)
    }

    fun getSubscriptionType(): String =
        MomensityBingoApp.preferencesManager?.getStringValue(SUBSCRIPTION_PLAN_TYPE).toString()

    fun isSubscriptionActive(): Boolean =
        MomensityBingoApp.preferencesManager?.getStringValue(SUBSCRIPTION_PLAN_STATUS).equals(Constants.ACTIVE)
}