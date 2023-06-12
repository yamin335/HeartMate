package com.tovalue.me.model.datingJourney

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class DatingReviewCouplePlanResponse(
    @SerializedName("status")
    @Expose
    var status: String? = null,

    @SerializedName("success")
    @Expose
    var success: Boolean? = null,

    @SerializedName("results")
    @Expose
    var results: ResultsData? = null
)

data class ResultsData(
    @SerializedName("group_id")
    @Expose
    var groupId: Int? = null,
    @SerializedName("title")
    @Expose
    var title: String? = null,

    @SerializedName("field_1")
    @Expose
    var field1: String? = null,

    @SerializedName("field_2")
    @Expose
    var field2: String? = null,

    @SerializedName("field_3")
    @Expose
    var field3: String? = null,

    @SerializedName("for_month")
    @Expose
    var forMonth: Int? = null,

    @SerializedName("of_year")
    @Expose
    var ofYear: Int? = null,

    @SerializedName("date")
    @Expose
    var date: String? = null
)