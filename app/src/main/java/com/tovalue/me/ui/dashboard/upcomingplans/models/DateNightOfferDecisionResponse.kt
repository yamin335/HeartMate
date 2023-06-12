package com.tovalue.me.ui.dashboard.upcomingplans.models

import com.google.gson.annotations.SerializedName


data class DateNightOfferDecisionResponse(
    val status: String,
    val error:String,
    @SerializedName("offer_status")
    val offerStatus: OfferStatus
)

data class OfferStatus(
    val event_id: Int,
    @SerializedName("message")
    val message: String
)