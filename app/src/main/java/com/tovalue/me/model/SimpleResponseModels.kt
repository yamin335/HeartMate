package com.tovalue.me.model

data class DateNightOfferResponse(
    var status: String,
    var date_offer_id: Int,
    var notice_id: Int,
    var scheduled_on: String,
    var what_happened: String
)