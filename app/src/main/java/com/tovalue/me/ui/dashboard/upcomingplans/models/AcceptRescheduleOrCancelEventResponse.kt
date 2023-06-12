package com.tovalue.me.ui.dashboard.upcomingplans.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class AcceptRescheduleOrCancelEventResponse(
    val status: String,
    val error: String,
    @SerializedName("plan_status")
    val planStatus: PlanStatuss
)

data class PlanStatuss(
    @SerializedName("reschedule_offer_id")
    val rescheduleOfferId: Long,
    @SerializedName("message")
    val message: Messages,
    @SerializedName("date_night_event")
    val dateNightEvent: String
)

class Messages(
    @SerializedName("message")
    var message: String? = null
)