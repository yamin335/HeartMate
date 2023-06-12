package com.tovalue.me.ui.visionboard.response


import com.google.gson.annotations.SerializedName

data class IceBreakerResponseModel(
    @SerializedName("response")
    val response: List<Response>,
    @SerializedName("status")
    val status: String,
    @SerializedName("success")
    val success: Boolean
)

data class Response(
    @SerializedName("icebreaker_question")
    val icebreakerQuestion: String
)