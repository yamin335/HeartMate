package com.tovalue.me.billing

import android.content.Context
import com.tovalue.me.util.Constants

object BillingHelperOB {

    private lateinit var billingHelper: BillingHelper

    fun getBillingOb(context: Context) = if (::billingHelper.isInitialized.not()) {
        BillingHelper(
            context = context,
            skuSubscriptions = listOf(
                Constants.BASIC_PLAN,
                Constants.QUARTERLY_PLAN,
                Constants.HALF_YEARLY_PLAN,
                Constants.BASIC_PLAN_3,
                Constants.QUARTERLY_PLAN_3,
                Constants.HALF_YEARLY_PLAN_3,
            ),
            enableLogging = true
        )
    } else
        billingHelper
}