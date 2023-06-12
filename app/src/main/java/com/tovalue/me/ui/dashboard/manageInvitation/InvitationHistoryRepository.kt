package com.tovalue.me.ui.dashboard.manageInvitation

import com.tovalue.me.network.APIClient
import com.tovalue.me.network.safeApiCall

class InvitationHistoryRepository {


    suspend fun getInvitationHistory() = safeApiCall {
        APIClient.aPIClient.getInvitationHistory(
        )
    }

    suspend fun deleteInvitationHistory(
        invitationCode: String,
        groupId: Int
    ) = safeApiCall {
        APIClient.aPIClient.deleteInvitationHistory(
            invitationCode = invitationCode,
            groupId = groupId
        )
    }
}