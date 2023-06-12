//package com.tovalue.me.ui.dashboard.setting
//
//import android.os.Bundle
//import android.util.Log
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.core.view.isVisible
//import androidx.fragment.app.Fragment
//import androidx.fragment.app.activityViewModels
//import com.android.billingclient.api.Purchase
//import com.google.gson.Gson
//import com.tovalue.me.R
//import com.tovalue.me.billing.BillingEvent
//import com.tovalue.me.billing.BillingHelper
//import com.tovalue.me.billing.BillingListener
//import com.tovalue.me.common.showToast
//import com.tovalue.me.databinding.FragmentMembershipBinding
//import com.tovalue.me.helper.MomensityBingoApp
//import com.tovalue.me.ui.dashboard.navigation.NavigationViewModel
//import com.tovalue.me.util.Constants
//
///* Subscription Type
//* 0 -> subscription
//* 1 -> dating subscription
//* */
//class MembershipFragment : Fragment(), BillingListener {
//    private var _binding: FragmentMembershipBinding? = null
//    private val binding get() = _binding!!
//    private lateinit var billingClient: BillingHelper
//    var isQueryCompleted: Boolean = false
//    private val viewModel: NavigationViewModel by activityViewModels()
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        billingClient = BillingHelper(
//            context = requireContext(),
//            skuSubscriptions = listOf(ANNUALLY_PLAN, HALF_YEARLY_PLAN, QUARTERLY_PLAN, BASIC_PLAN),
//          //  billingListener = this,
//            enableLogging = true
//        )
//        billingClient.addBillingListener(this)
//    }
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        _binding = FragmentMembershipBinding.inflate(inflater, container, false)
//        return binding.root
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        setUpViews()
//        setUpClickListener()
//    }
//
//    private fun setUpViews() {
//            val type = arguments?.getInt(Constants.SUBSCRIPTION_TYPE,0)?:0
//
//            if (type == 0) {
//                setUpAppSubscriptionViews()
//            } else if (type == 1) {
//                setUpDatingSubscriptionViews()
//            }
//
////            billingClient = BillingHelper(
////                context = requireContext(),
////                skuSubscriptions = listOf(
////                    ANNUALLY_PLAN,
////                    HALF_YEARLY_PLAN,
////                    QUARTERLY_PLAN,
////                    BASIC_PLAN
////                ),
////                billingListener = this,
////                enableLogging = true
////            )
//    }
//
//    private fun setUpClickListener() {
//        binding.cancelImg.setOnClickListener {
//            requireActivity().onBackPressed()
//        }
//    }
//
//    private fun setUpAppSubscriptionViews() {
//
//        binding.logoImg.setImageResource(R.drawable.calender)
//        binding.billingTagImg.text = getString(R.string.membershipTitle)
//        binding.moreDatingPartnersLL.isVisible = true
//        binding.datingPartnersLL.isVisible = false
//
//        setUpSubscriptionClickListener()
//
//
//    }
//
//    private fun setUpSubscriptionClickListener() {
//        Log.d("DClickListenerDAta", "setUpSubscriptionClickListener:1 " )
//        binding.basicMembershipLayout.setOnClickListener {
//            if (isQueryCompleted) launchPurchaseDialog(BASIC_PLAN)
//            else showToast(resources.getString(R.string.membership_failed))
//        }
//        binding.plusMembershipLayout.setOnClickListener {
//            if (isQueryCompleted) launchPurchaseDialog(QUARTERLY_PLAN)
//            else showToast(resources.getString(R.string.membership_failed))
//        }
//        binding.quarterlyMembershipLayout.setOnClickListener {
//            if (isQueryCompleted) launchPurchaseDialog(HALF_YEARLY_PLAN)
//            else showToast(resources.getString(R.string.membership_failed))
//        }
//        binding.yearlyMembershipLayout.setOnClickListener {
//            if (isQueryCompleted) launchPurchaseDialog(ANNUALLY_PLAN)
//            else showToast(resources.getString(R.string.membership_failed))
//        }
//    }
//
//
//    private fun setUpDatingSubscriptionViews() {
//        binding.logoImg.setImageDrawable(null)
//        binding.logoImg.setBackgroundResource(R.drawable.dating_subscription_dialog_img)
//        binding.billingTagImg.text = getString(R.string.dating_dialog_title)
//        binding.moreDatingPartnersLL.isVisible = false
//        binding.datingPartnersLL.isVisible = true
//
//        setUpDatingSubscriptionOnClickListener()
//    }
//
//    private fun setUpDatingSubscriptionOnClickListener() {
//
//    }
//
//
//    private fun launchPurchaseDialog(subscriptionPlan: String) {
//        when (subscriptionPlan) {
//            BASIC_PLAN -> {
//                if (billingClient.isPurchased(BASIC_PLAN)) showToast(resources.getString(R.string.membership_subscribed))
//                else billingClient.launchPurchaseFlow(requireActivity(), BASIC_PLAN)
//            }
//            QUARTERLY_PLAN -> {
//                if (billingClient.isPurchased(QUARTERLY_PLAN)) showToast(resources.getString(R.string.membership_subscribed))
//                else billingClient.launchPurchaseFlow(requireActivity(), QUARTERLY_PLAN)
//            }
//            HALF_YEARLY_PLAN -> {
//                if (billingClient.isPurchased(HALF_YEARLY_PLAN)) showToast(resources.getString(R.string.membership_subscribed))
//                else billingClient.launchPurchaseFlow(requireActivity(), HALF_YEARLY_PLAN)
//            }
//            ANNUALLY_PLAN -> {
//                if (billingClient.isPurchased(ANNUALLY_PLAN)) showToast(resources.getString(R.string.membership_subscribed))
//                else billingClient.launchPurchaseFlow(requireActivity(), ANNUALLY_PLAN)
//            }
//        }
//    }
//
//    override fun onDestroyView() {
//        super.onDestroyView()
//        _binding = null
//    }
//
//    override fun onBillingEvent(event: BillingEvent, message: String?, responseCode: Int?) {
//        when (event) {
//            BillingEvent.BILLING_CONNECTED -> {
//
//            }
//            BillingEvent.BILLING_CONNECTION_FAILED -> {
//
//            }
//            BillingEvent.BILLING_DISCONNECTED -> {
//
//            }
//            BillingEvent.QUERY_SKU_DETAILS_COMPLETE -> {
//                isQueryCompleted = billingClient.skuDetailsQueried
//            }
//            BillingEvent.QUERY_SKU_DETAILS_FAILED -> {
//
//            }
//            BillingEvent.QUERY_OWNED_PURCHASES_COMPLETE -> {
//
//            }
//            BillingEvent.QUERY_OWNED_PURCHASES_FAILED -> {
//
//            }
//            BillingEvent.PURCHASE_COMPLETE -> {
//
//            }
//            BillingEvent.PURCHASE_FAILED -> {
//
//            }
//            BillingEvent.PURCHASE_CANCELLED -> {
//
//            }
//            BillingEvent.PURCHASE_ACKNOWLEDGE_SUCCESS -> {
//                showToast(resources.getString(R.string.membership_purchase_success_msg))
//                val purchase = Gson().fromJson(message, Purchase::class.java)
//                sendToken(purchase)
//            }
//            BillingEvent.PURCHASE_ACKNOWLEDGE_FAILED -> {
//
//            }
//            else -> {}
//        }
//    }
//
//    private fun sendToken(purchase: Purchase) {
//        viewModel.updateMetaValues(
//            subscriotionStatus = "subscribed",
//            subscriptionToken = purchase.purchaseToken,
//            subscriptionType = purchase.purchaseState.toString(),
//        )
//
//
//        if (purchase.purchaseState == 1) {
//            /* Save Purchase state */
//            MomensityBingoApp.preferencesManager?.setBooleanValue(
//                Constants.IS_SUBSCRIPTION_PLAN,
//                true
//            )
//
//        } else {
//            MomensityBingoApp.preferencesManager?.setBooleanValue(
//                Constants.IS_SUBSCRIPTION_PLAN,
//                false
//            )
//        }
//
//
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//        billingClient.endClientConnection()
//    }
//
//    companion object {
//        // subscription
//        const val ANNUALLY_PLAN = "annually"
//        const val HALF_YEARLY_PLAN = "biannual_89"
//        const val BASIC_PLAN = "monthly_29"
//        const val QUARTERLY_PLAN = "quarterly_59"
//
//        // in-app products
//        const val IN_APP_PRODUCT_SKU = "tvmtest"
//    }
//}