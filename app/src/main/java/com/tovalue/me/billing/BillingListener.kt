package com.tovalue.me.billing

import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingResult

interface BillingListener {
    /**
     * @param event one of resolved [BillingEvent]s
     * @param message as reported by [BillingResult.getDebugMessage]
     * @param responseCode as reported by [BillingResult.getResponseCode] as [BillingClient.BillingResponseCode].
     */
    fun onBillingEvent(event: BillingEvent, message: String?, responseCode: Int?)
}