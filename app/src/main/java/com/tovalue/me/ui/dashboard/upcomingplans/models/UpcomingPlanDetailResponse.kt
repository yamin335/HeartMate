package com.tovalue.me.ui.dashboard.upcomingplans.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class UpcomingPlanDetailResponse(
    @SerializedName("status") var status: String? = null,
    @SerializedName("error") var error: String? = null,
    @SerializedName("date_night_event") var dateNightEvent: DateNightEvent?,
    @SerializedName("date_night_offer") var dateNightOffer: DateNightOffer?
) : Serializable

data class DateNightEvent(
    @SerializedName("event_id")
    var eventId: String? = null,

    @SerializedName("event_title")
    var eventTitle: String? = null,

    @SerializedName("event_description")
    var eventDescription: String? = null,

    @SerializedName("event-address")
    var event_address: String? = null,

    @SerializedName("featured_image")
    var featuredImage: String? = null,

    @SerializedName("event_website")
    var eventWebsite: String? = null,

    @SerializedName("event_start_date")
    var eventStartDate: String? = null,

    @SerializedName("event_start_time")
    var eventStartTime: String? = null,

    @SerializedName("event_end_date")
    var eventEndDate: String? = null,

    @SerializedName("event_end_time")
    var eventEndTime: String? = null,

    @SerializedName("days_until")
    var daysUntil: String? = null,

    @SerializedName("dates_so_far")
    var datesSoFar: String? = null,

    @SerializedName("parent_event_id")
    var parentEventId: String? = null,

    @SerializedName("reschedule_offer_id")
    var rescheduleOfferId: String? = null,

    @SerializedName("created_by")
    var createdby: String? = null,

    @SerializedName("is_sender")
    var isSender: Int = 0

) : Serializable

data class DateNightOffer(
    @SerializedName("date_night_offer_id") var dateNightOfferId: String? = null,
    @SerializedName("date_night_title") var dateNightTitle: String? = null,
    @SerializedName("date_night_description") var dateNightDescription: String? = null,
    @SerializedName("location") var location: String? = null,
    @SerializedName("featured_image") var featuredImage: String? = null,
    @SerializedName("event_website") var eventWebsite: String? = null,
    @SerializedName("event_start_date") var eventStartDate: String? = null,
    @SerializedName("event_start_time") var eventStartTime: String? = null,
    @SerializedName("event_end_date") var eventEndDate: String? = null,
    @SerializedName("event_end_time") var eventEndTime: String? = null,
    @SerializedName("days_until") var daysUntil: String? = null,
    @SerializedName("dates_so_far") var datesSoFar: String? = null,
    @SerializedName("created_by") var createdby: String? = null,
    @SerializedName("is_sender") var isSender: Int = 0,
) : Serializable


