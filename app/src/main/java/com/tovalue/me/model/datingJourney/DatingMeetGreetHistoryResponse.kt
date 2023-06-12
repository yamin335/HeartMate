package com.tovalue.me.model.datingJourney

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class DatingMeetGreetHistoryResponse(
    @SerializedName("status")
    @Expose
    var status: String? = null,

    @SerializedName("success")
    @Expose
    var success: Boolean? = null,

    @SerializedName("response")
    @Expose
    var response: List<Response>? = null
)

data class MyTestmonial(
    @SerializedName("testimonial_id")
    @Expose
    var testimonialId: Int? = null,

    @SerializedName("first_name")
    @Expose
    var firstName: String? = null,

    @SerializedName("title")
    @Expose
    var title: String? = null,

    @SerializedName("date")
    @Expose
    var date: String? = null
)

data class PartnerTestimonial(
    @SerializedName("testimonial_id")
    @Expose
    var testimonialId: Int? = null,

    @SerializedName("first_name")
    @Expose
    var firstName: String? = null,

    @SerializedName("title")
    @Expose
    var title: String? = null,

    @SerializedName("date")
    @Expose
    var date: String? = null
)

data class Response(
    @SerializedName("my_testmonial")
    @Expose
    var myTestmonial: MyTestmonial? = null,

    @SerializedName("partner_testimonial")
    @Expose
    var partnerTestimonial: PartnerTestimonial? = null
)