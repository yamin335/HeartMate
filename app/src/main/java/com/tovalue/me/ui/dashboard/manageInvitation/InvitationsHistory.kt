package com.tovalue.me.ui.dashboard.manageInvitation

import com.google.gson.annotations.SerializedName

data class InvitationsHistory(
    val status: String,
    val success: String,
    val error: String,
    val message: String,
    @SerializedName("data")
    val InvitationsHistory: ArrayList<InvitationData>,
)

class InvitationData(
    val invitee_number: String,
    val invitation_date: String,
    val invitation_code: String,
    val days_pending: Int,
    val group_id: Int
)

class DeleteInvitation(
    val status: String,
    val group_deleted: Int,
    val rejeted: Int,
    val error: String,
)

