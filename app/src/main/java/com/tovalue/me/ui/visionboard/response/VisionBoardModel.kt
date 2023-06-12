package com.tovalue.me.ui.visionboard.response


import com.google.gson.annotations.SerializedName

data class VisionBoardModel(
    @SerializedName("response")
    val response: ResponseModel,
    @SerializedName("status")
    val status: String,
    @SerializedName("success")
    val success: Boolean
)

data class ResponseModel(
    @SerializedName("adulting_season_form")
    val adultingSeasonForm: List<AdultingSeasonForm>,
    @SerializedName("dating_style_form")
    val datingStyleForm: List<DatingStyleForm>,
    @SerializedName("mindset_form")
    val mindsetForm: List<MindsetForm>,
    @SerializedName("season_of_bliss_form")
    val seasonOfBlissForm: List<SeasonOfBlissForm>,
    @SerializedName("user_dating_style")
    val dateStyleList: List<DateStyleForm>,
    @SerializedName("user_adulting_season_primary")
    val userAdultingSeasonPrimary: String,
    @SerializedName("user_adulting_season_secondary")
    val adultingSeasonSecondary: String,
    @SerializedName("user_bliss_primary")
    val userBlissPrimary: String,
    @SerializedName("user_bliss_secondary")
    val userBlissSecondary: String,
    @SerializedName("user_mindset")
    val userMindset: String,
    @SerializedName("vision_board_id")
    val visionBoardId: Int
)

data class AdultingSeasonForm(
    @SerializedName("title")
    val title: String,
    @SerializedName("definition")
    val definition: String
)

data class DatingStyleForm(
    @SerializedName("title")
    val title: String,
    @SerializedName("definition")
    val definition: String
)

data class MindsetForm(
    @SerializedName("title")
    val title: String,
    @SerializedName("definition")
    val definition: String
)

data class SeasonOfBlissForm(
    @SerializedName("title")
    val title: String,
    @SerializedName("definition")
    val definition: String
)

data class DateStyleForm(
    @SerializedName("user_order")
    val orderId: Int,
    @SerializedName("title")
    val title: String,
    @SerializedName("definition")
    val definition: String
)