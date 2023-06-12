package com.tovalue.me.ui.levelTwoInvitation.response

import com.google.gson.annotations.SerializedName

data class LevelTwoInvitationInfo(
    @SerializedName("description") val description: String,
    @SerializedName("header") val header: String,
    @SerializedName("comfortable_text") val comfortable_text: String,
    @SerializedName("balance_text") val balance_text: String,
    @SerializedName("safe_text") val safe_text: String
)