package com.tovalue.me.ui.dashboard.upcomingplans.viewmodel

import com.tovalue.me.network.APIClient
import com.tovalue.me.network.safeApiCall

class UpcomingPlansRepository {


    suspend fun getMasterPlanner(
        fromDate: String,
        toDate: String,
        groupId: String
    ) = safeApiCall {
        APIClient.aPIClient.getMasterPlanner(
            fromDate = fromDate,
            toDate = toDate,
            groupId = groupId
        )
    }

    suspend fun scheduleMeTime(
        startTime: String,
        startDate: String,
        endTime: String,
        endDate: String
    ) =
        safeApiCall {
            APIClient.aPIClient.scheduleMeTime(
                startDate = startDate,
                startTime = startTime,
                endDate = endDate,
                endTime = endTime,
            )
        }

    suspend fun getEventDetail(eventId: Int) = safeApiCall {
        APIClient.aPIClient.getEventDetail(eventId = eventId)
    }

    suspend fun getReScheduledEventDetail(eventId: Int) =
        safeApiCall {
            APIClient.aPIClient.getReScheduledEventDetail(
                eventId = eventId
            )
        }

    suspend fun getDayNightOffer(
        dateNightOfferId: Int
    ) =
        safeApiCall {
            APIClient.aPIClient.getDateNightOffer(
                dateNightOfferId = dateNightOfferId
            )
        }

    suspend fun submitDateNightOfferDecision(
        dateNightOfferId: Int,
        decision: Int,
        startDate: String = "",
        startTime: String = "",
        endDate: String = "",
        endTime: String = "",
    ) =
        safeApiCall {
            APIClient.aPIClient.submitDateNightOfferDecision(
                dateNightOfferId = dateNightOfferId,
                decision = decision,
                startDate = startDate,
                startTime = startTime,
                endDate = endDate,
                endTime = endTime,

                )
        }

    suspend fun offerRescheduleDateNightEvent(
        eventId: Int,
        startDate: String = "",
        startTime: String = "",
        endDate: String = "",
        endTime: String = "",
    ) =
        safeApiCall {
            APIClient.aPIClient.offerRescheduleDateNightEvent(
                eventId = eventId,
                startDate = startDate,
                startTime = startTime,
                endDate = endDate,
                endTime = endTime,
            )
        }

    suspend fun acceptRescheduleDateNightEvent(
        eventId: Int,
        rescheduleOfferId: Int,
        startDate: String,
        startTime: String,
    ) =
        safeApiCall {
            APIClient.aPIClient.acceptRescheduleDateNightEvent(
                eventId = eventId,
                rescheduleOfferId = rescheduleOfferId,
                startDate,
                startTime
            )
        }

    suspend fun cancelMeTime(
        eventId: Int,
    ) =
        safeApiCall {
            APIClient.aPIClient.cancelMeTime(
                eventId = eventId,
            )
        }

    suspend fun getCancelReasons(eventId: Int) = safeApiCall {
        APIClient.aPIClient.getCancelReasons()
    }

    suspend fun cancelDateNightEvent(
        eventId: Int,
        cancelReasonId: Long
    ) =
        safeApiCall {
            APIClient.aPIClient.cancelDateNightEvent(
                eventId = eventId,
                cancelReasonID = cancelReasonId.toInt()
            )
        }


}

