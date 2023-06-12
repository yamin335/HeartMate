package com.tovalue.me.ui.datenightcatalog

import android.icu.text.CaseMap.Title
import com.tovalue.me.helper.MomensityBingoApp
import com.tovalue.me.network.APIClient
import com.tovalue.me.network.safeApiCall
import com.tovalue.me.util.Constants
import retrofit2.http.Query
import okhttp3.MultipartBody
import java.io.FileDescriptor

class DateNightCatalogRepository {


    suspend fun getDataNightCatalog(partnerUserId: Int, groupId: Int) = safeApiCall {

        APIClient.aPIClient.getPartnerDateNightCatalog(
            partnerUserId = partnerUserId,
            groupId = groupId,
            canSeePromoted = getIsPromoted()
        )

    }

    suspend fun getPromotedEvent(partnerUserId: Int, groupId: Int) = safeApiCall {

        APIClient.aPIClient.getAllPromotedEvents(
            partnerUserId = partnerUserId,
            groupId = groupId,
            promoted = getIsPromoted()
        )
    }


    suspend fun getDateNightCatalogDetail(dateNightId: String) = safeApiCall {

        APIClient.aPIClient.getDateNightCatalogDetail(
            dateNightId = dateNightId,
            isPartner =1 //Should Change
        )
    }


    suspend fun sendDateNightOffer(
        startTime: String,
        endTime: String,
        description: String,
        dateNightId: String,
        dateWeekId: String,
        inventoryTopic: String,
        groupId: Int,
        partnerUserId: Int,
        title: String,
        event_address: String,
        event_end_date: String,
        event_start_date: String,
        url: String,
        eventImage: MultipartBody.Part
    ) = safeApiCall {

        APIClient.aPIClient.sendOfferDateNight(
            description = description,
            dateNightId = dateNightId,
            dateWeekId = dateWeekId,
            inventoryTopic = inventoryTopic,
            partnerUserId = partnerUserId,
            title = title,
            event_address = event_address,
            event_end_date = event_end_date,
            event_start_date = event_start_date,
            startTime = startTime,
            endTime = endTime,
            groupId = groupId,
            url = url,
            attachment = eventImage

        )

    }

    fun getIsPromoted(): Int {
        return if (MomensityBingoApp.preferencesManager?.getStringValue(Constants.SUBSCRIPTION_PLAN_STATUS).equals(Constants.ACTIVE)) 1 else 0

    }

    suspend fun getDateNightIdeas(page: Int, groupId: Int, userId: Int) = safeApiCall {
        APIClient.aPIClient.getDateNightIdeas(
            page = page,
            canSeePromoted = getIsPromoted(),
            groupId = groupId,
            userId = userId
        )
    }

    suspend fun getDateNightIdeaDetails(dateNightId: Int, isPartner: Int) = safeApiCall {
        APIClient.aPIClient.getDateNightIdeaDetails(
            dateNightId = dateNightId,
            isPartner = isPartner
        )
    }
}