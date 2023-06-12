package com.tovalue.me.model


import com.google.gson.annotations.SerializedName

data class Avatar(
    @SerializedName("full")
    val full: String,
    @SerializedName("status")
    val status: String,
    @SerializedName("thumb")
    val thumb: String
) {
    val error: String = ""
}