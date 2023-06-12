package com.tovalue.me.ui.levelTwoInvitation.response

import com.google.gson.annotations.SerializedName

data class CommitmentOfferResponse(
    @SerializedName("status") val status: String,
    @SerializedName("error") val error: String,
    @SerializedName("offer_post_id") val offer_post_id: Int,
    @SerializedName("notification_id") val notificationId: Int,
    @SerializedName("comfortable_label") val comfortable_label: String,
    @SerializedName("balance_label") val balance_label: String,
    @SerializedName("safe_label") val safe_label: String,
    @SerializedName("inviter_name") val inviter_name: String,
    @SerializedName("inviter_avatar") val inviter_avatar: String,
    @SerializedName("description_text") val description_text: String,
    @SerializedName("header_text") val header_text: String,
    @SerializedName("footer_text") val footer_text: String,
    @SerializedName("submission_options") val submission_options: List<String>,
    @SerializedName("group_id") val group_id: Int,
    @SerializedName("author_id") val author_id: Int,
    @SerializedName("initiator_status") val initiator_status: Int,
    @SerializedName("comfortable_response") val comfortable_response: String,
    @SerializedName("balance_response") val balance_response: String,
    @SerializedName("safe_response") val safe_response: String,
    @SerializedName("acknowledgement_status") val acknowledgement_status: Int,
    @SerializedName("acceptance_status") val acceptance_status: Int,
    @SerializedName("rejection_message") val rejection_message: String,
    @SerializedName("sender") val sender: Int,
    @SerializedName("level_2_invitation") val level_2_invitation: LevelTwoInvitationInfo
)