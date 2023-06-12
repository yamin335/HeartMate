package com.tovalue.me.ui.levelTwoInvitation

import com.tovalue.me.network.APIClient
import com.tovalue.me.network.safeApiCall

class LevelTwoInvitationRepository {

    suspend fun getCommitmentOffer(offerPostId: Int) = safeApiCall {
        APIClient.aPIClient.getCommitmentOffer(offerPostId = offerPostId)
    }

    suspend fun acceptCommitmentOffer(
        offerPostId: Int,
        groupId: Int? = null,
        comfortResponse: String? = null,
        balanceResponse: String? = null,
        safetyResponse: String? = null,
    ) = safeApiCall {
        APIClient.aPIClient.acceptCommitmentOffer(
            offerPostId = offerPostId,
            groupId = groupId,
            comfortResponse = comfortResponse,
            balanceResponse = balanceResponse,
            safetyResponse = safetyResponse
        )
    }

    suspend fun declineCommitmentOffer(
        offerPostId: Int,
        groupId: Int? = null,
        decisionResponse: Int? = null
    ) = safeApiCall {
        APIClient.aPIClient.declineCommitmentOffer(
            offerPostId = offerPostId,
            groupId = groupId,
            decisionResponse = decisionResponse,
        )
    }

    suspend fun commitmentDoubleConfirmation(
        acceptancePostId: Int? = null,
        rejectionMessage: String? = null,
        acceptanceStatus: Int? = null,
        groupId: Int? = null,
    ) = safeApiCall {
        APIClient.aPIClient.commitmentDoubleConfirmation(
            groupId = groupId,
            acceptancePostId = acceptancePostId,
            rejectionMessage = rejectionMessage,
            acceptanceStatus = acceptanceStatus,
        )
    }

}