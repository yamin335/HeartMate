package com.tovalue.me.ui.dashboard.upcomingplans.models

import com.google.gson.annotations.SerializedName


data class RescheduleOrCancelEventResponse(
    val status: String,
    val error: String,
    @SerializedName("plan_status")
    val planStatus: PlanStatus
)

data class PlanStatus(
    @SerializedName("reschedule_offer_id")
    val rescheduleOfferId: Long,
    @SerializedName("message")
    val message: String,
    @SerializedName("date_night_event")
    val dateNightEvent: String
)