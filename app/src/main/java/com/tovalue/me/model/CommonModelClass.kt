package com.tovalue.me.model

import okhttp3.MultipartBody

data class SendOfferDateNight(
    var title: String = "",
    var startDate: String = "",
    var startTime: String = "",
    var endDate: String = "",
    var endTime: String = "",
    var description: String = "",
    var destination: String = "",
    var url: String = "",
    var dateNightId: String = "",
    var dateWeekId: Int =0,
    var inventoryTopic: String = "",
    var groupId: Int = 0,
    var partnerUserId: Int = 0,
    var event_address: String = "",
    var event_end_date: String = "",
    var event_start_date: String = "",
    var eventImg: MultipartBody.Part? = null
)