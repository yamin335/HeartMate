package com.tovalue.me.model


import com.google.gson.annotations.SerializedName

data class Capabilities(
    @SerializedName("subscriber")
    val subscriber: Boolean
)