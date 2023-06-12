package com.tovalue.me.model.datingJourney

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class DatingMeetGreetResponse(
    @SerializedName("status")
    var status: String? = null,

    @SerializedName("testimonial_id")
    var testimonialId:String?=null,

    @SerializedName("results")
    var results: Results? = null
)

data class Results(
    @SerializedName("experience_types")
    var experienceTypes: List<String>? = null,

    @SerializedName("0")
     var question0: QuestionObj? = null,

    @SerializedName("1")
     var question1: QuestionObj? = null,

    @SerializedName("2")
     var question2: QuestionObj? = null

)

data class QuestionObj(
    @SerializedName("question")
    var question: String? = null,

    @SerializedName("field")
    var field: String? = null,
)
