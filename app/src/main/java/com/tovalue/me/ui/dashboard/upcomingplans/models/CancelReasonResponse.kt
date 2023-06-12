package com.tovalue.me.ui.dashboard.upcomingplans.models

import com.google.gson.annotations.SerializedName


data class CancelReasonResponse(
    val status: String,
    val error: String,
    @SerializedName("cancel_reasons")
    val cancelReasons: List<CancelReason>
)

data class CancelReason(
    val id: Long,
    @SerializedName("reason_text")
    val reasonText: String
)