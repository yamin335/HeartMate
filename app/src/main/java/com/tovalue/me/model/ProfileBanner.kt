package com.tovalue.me.model


import com.google.gson.annotations.SerializedName

data class ProfileBanner(
    @SerializedName("slider")
    val slider: Slider,
    @SerializedName("timer")
    val timer: Int
)

data class Slider(
    @SerializedName("panels")
    val panels: List<Panel>
)

data class Panel(
    @SerializedName("description")
    val description: String,
    @SerializedName("icon")
    val icon: String,
    @SerializedName("title")
    val title: String
)