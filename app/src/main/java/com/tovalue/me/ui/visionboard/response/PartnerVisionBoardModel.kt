package com.tovalue.me.ui.visionboard.response


import com.google.gson.annotations.SerializedName

data class PartnerVisionBoardModel(
    @SerializedName("response")
    val response: PartnerBoardResponse,
    @SerializedName("status")
    val status: String,
    @SerializedName("success")
    val success: Boolean
)

data class PartnerBoardResponse(
    @SerializedName("be_aware")
    val beAware: Any?,
    @SerializedName("dating_momentum")
    val datingMomentum: Any?,
    @SerializedName("dating_style")
    val datingStyle: String,
    @SerializedName("partner_mindset")
    val partnerMindset: String,
    @SerializedName("season_of_bliss")
    val seasonOfBliss: String,
    @SerializedName("your_person")
    val yourPerson: Any?
)