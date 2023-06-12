package com.tovalue.me.ui.whatworkguide

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tovalue.me.helper.MomensityBingoApp
import com.tovalue.me.model.notification.NotificationListResponse
import com.tovalue.me.model.notification.UnreadCountModel
import com.tovalue.me.network.APIClient
import com.tovalue.me.network.safeApiCall
import com.tovalue.me.ui.dashboard.DashboardViewModel
import com.tovalue.me.util.Constants
import com.tovalue.me.util.livedata.Event

class WhatWorksGuideRepository {

    suspend fun getWhatWorksGuide() = safeApiCall {
        APIClient.aPIClient.getWhatWorks()
    }

    suspend fun getWhatWorksGuideData(guideID: Int) = safeApiCall {
        APIClient.aPIClient.getWhatWorksData(guideID)
    }

    suspend fun sendVote(guideID: Int, vote: Int) = safeApiCall {
        APIClient.aPIClient.sendVote(guideID, vote)
    }

}