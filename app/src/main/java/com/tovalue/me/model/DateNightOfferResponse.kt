package com.tovalue.me.model

import com.google.gson.annotations.SerializedName

data class DateNightOffer(
    @SerializedName("status") var status: String?=null,
    @SerializedName("date_offer_id") var dateOfferId: Int? = null,
    @SerializedName("notice_id") var noticeId: String? = null,
    @SerializedName("scheduled_on ") var scheduledOn: String? = null,
    @SerializedName("what_happened ") var whatHappened: String? = null
)