package com.tovalue.me.ui.catalog

import com.tovalue.me.network.APIClient
import com.tovalue.me.network.safeApiCall

class UpGradeToLevelRepository {
    
    suspend fun upGradeToLevel(
        groupId: Int,
        level: Int,
        balanceResponse: String?,
        comfortResponse: String?,
        safetyResponse: String?
    ) =
        safeApiCall {
            APIClient.aPIClient.upGradeToLevel(
                groupId = groupId,
                progressLevel = level,
                balance_response = balanceResponse,
                comfort_response = comfortResponse,
                safety_response = safetyResponse
            )
        }

}