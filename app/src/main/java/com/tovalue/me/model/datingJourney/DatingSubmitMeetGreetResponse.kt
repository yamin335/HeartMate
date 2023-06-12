package com.tovalue.me.model.datingJourney

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class DatingSubmitMeetGreetResponse(
    @SerializedName("status")
    var status: String? = null,

    @SerializedName("testimonial_id")
    var testimonialId:String?=null,

)

