package com.tovalue.me.model.datingJourney

import com.google.gson.annotations.SerializedName


data class DatingOurCalenderResponse(
    @SerializedName("status") var status: String? = null,
    @SerializedName("error") var error: String? = null,
    @SerializedName("journey_details") var journey_details: ArrayList<JourneyDetails> = arrayListOf(),
    @SerializedName("groups") var groups: ArrayList<Groups> = arrayListOf(),
    @SerializedName("partners") var partners: ArrayList<Partners> = arrayListOf()
)

data class JourneyDetails(
    @SerializedName("total_weeks") var total_weeks: String? = null,
    @SerializedName("names") var names: String? = null,

)

data class Groups(
    @SerializedName("firstName") var firstName: String? = null,
    @SerializedName("groupID") var groupID: Int? = null,
    @SerializedName("color_code") var colorCode: String? = null,
    var isChecked: Boolean = false
)

data class Partners(
    @SerializedName("time_of_day") var timeOfDay: String? = null,
    @SerializedName("plans") var plans: ArrayList<Plans> = arrayListOf()
)

data class Plans(
    @SerializedName("type") var type: String? = null,
    @SerializedName("id") var id: Int? = null,
    @SerializedName("date") var date: String? = null,
    @SerializedName("start_time") var startTime: String? = null,
    @SerializedName("excerpt") var excerpt: String? = null,
    @SerializedName("group_id") var groupId: Int? = null,
    @SerializedName("color_code") var colorCode: String? = null,
    @SerializedName("first_name") var firstName: String? = null,
    @SerializedName("avatar") var avatar: String? = null
)