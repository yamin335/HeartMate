package com.tovalue.me.ui.dashboard.pushalert

import com.tovalue.me.network.APIClient
import com.tovalue.me.network.safeApiCall

class PushAlertRepository {

    suspend fun getNotifications(
        component: String? = null,
        searchTerms: String? = null,
        isNew: String? = null,
        page: Int? = null
    ) = safeApiCall {
        APIClient.aPIClient.getNotifications(
            component = component,
            searchTerms = searchTerms,
            isNew = isNew,
            page = page?.toString(),
            perPage = "10"
        )
    }

    suspend fun getReviewUpcomingPlan(itemId: Int) = safeApiCall {
        APIClient.aPIClient.getReviewUpcomingPlan(itemId = itemId)
    }


}