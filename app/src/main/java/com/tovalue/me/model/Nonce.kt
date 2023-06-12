package com.tovalue.me.model


import com.google.gson.annotations.SerializedName

data class Nonce(
    @SerializedName("controller")
    val controller: String,
    @SerializedName("method")
    val method: String,
    @SerializedName("nonce")
    val nonce: String,
    @SerializedName("status")
    val status: String
) {
    @SerializedName("error")
    val error: String = ""
}