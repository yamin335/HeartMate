package com.tovalue.me.ui.dashboard.membership


import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.SkuDetails
import com.google.gson.Gson
import com.tovalue.me.R
import com.tovalue.me.billing.BillingEvent
import com.tovalue.me.billing.BillingHelper
import com.tovalue.me.billing.BillingHelperOB
import com.tovalue.me.billing.BillingListener
import com.tovalue.me.common.showToast
import com.tovalue.me.databinding.FragmentPlanMembershipBinding
import com.tovalue.me.helper.MomensityBingoApp
import com.tovalue.me.ui.dashboard.DashboardViewModel
import com.tovalue.me.util.Constants
import com.tovalue.me.util.DateFormatterUtils.DATE_FORMAT_4
import com.tovalue.me.util.DateFormatterUtils.getDateFromMillis
import com.tovalue.me.util.DialogUtils
import com.tovalue.me.util.extensions.clearBackStack


class PlanMemberShipFragment : Fragment(), BillingListener {

    private val viewModel: DashboardViewModel by activityViewModels()
    private var _binding: FragmentPlanMembershipBinding? = null
    private val binding get() = _binding!!
    private lateinit var billingClient: BillingHelper

    private var isQueryCompleted: Boolean = false

    private var planType: String? = null
    private var isPlanStatusActive = false
    private var cameFromSettings = false

    private var purchasedToken: String? = null
    private var skuDetails: SkuDetails? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            planType = arguments?.getString(Constants.PASSED_PLAN_TYPE)
            isPlanStatusActive = arguments?.getBoolean(Constants.PASSED_PLAN_STATUS) ?: false
            cameFromSettings =
                arguments?.getBoolean(Constants.SUBSCRIPTION_VIEW_FROM_SETTINGS) ?: false
        }

        billingClient = BillingHelperOB.getBillingOb(requireContext())
        billingClient.addBillingListener(this)

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlanMembershipBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        setUpViewsToSubscribe()
        setUpObserver()

        binding.crossBtn.setOnClickListener {
            goBack()
        }

        if (cameFromSettings && isPlanStatusActive) {
            binding.planInfoContentContainer.visibility = View.VISIBLE
            binding.plansContainer.visibility = View.GONE
            if (planType == Constants.LEVEL_3) {
                planType = Constants.LEVEL_2
                binding.planChangeBtn.text = getString(R.string.downgrade)
                binding.planInfoContentContainerTitle.text =
                    getString(R.string.plan_info_title_for_plan3)
            } else {
                planType = Constants.LEVEL_3
                binding.planChangeBtn.text = getString(R.string.upgrade)
                binding.planInfoContentContainerTitle.text =
                    getString(R.string.plan_info_title_for_plan2)
            }
            setUpViewsForPackageInfo()
        } else {
            binding.planInfoContentContainer.visibility = View.GONE
            binding.plansContainer.visibility = View.VISIBLE
        }


    }

    private fun setUpViewsForPackageInfo() {
        binding.planChangeBtn.setOnClickListener {
            binding.planInfoContentContainer.visibility = View.GONE
            binding.plansContainer.visibility = View.VISIBLE
            setUpViewsToSubscribe()

        }
        binding.cancelSubscriptionBtn.setOnClickListener {
            openPlayStoreAccountForSubscription()
        }

    }

    private fun setUpViewsToSubscribe() {
        // For Plan 3
        if (planType == Constants.LEVEL_3) {
            binding.level2MembershipContainer.visibility = View.GONE
            binding.level3MembershipContainer.visibility = View.VISIBLE

            binding.plan1AmountTv.text = getString(R.string.level_3_membership_base_plan)
            binding.plan2AmountTv.text = getString(R.string.level_3_membership_three_month_plan)
            binding.plan3AmountTv.text = getString(R.string.level_3_membership_six_month_plan)


            binding.plan1Container.setOnClickListener {
                if (isQueryCompleted) launchPurchaseDialog(Constants.BASIC_PLAN_3)
                else showToast(resources.getString(R.string.membership_failed))
            }

            binding.plan2Container.setOnClickListener {
                if (isQueryCompleted) launchPurchaseDialog(Constants.QUARTERLY_PLAN_3)
                else showToast(resources.getString(R.string.membership_failed))
            }

            binding.plan3Container.setOnClickListener {
                if (isQueryCompleted) launchPurchaseDialog(Constants.HALF_YEARLY_PLAN_3)
                else showToast(resources.getString(R.string.membership_failed))
            }
        } else {
            binding.level2MembershipContainer.visibility = View.VISIBLE
            binding.level3MembershipContainer.visibility = View.GONE
            binding.plan1Container.setOnClickListener {
                if (isQueryCompleted) launchPurchaseDialog(Constants.BASIC_PLAN)
                else showToast(resources.getString(R.string.membership_failed))
            }

            binding.plan2Container.setOnClickListener {
                if (isQueryCompleted) launchPurchaseDialog(Constants.QUARTERLY_PLAN)
                else showToast(resources.getString(R.string.membership_failed))
            }

            binding.plan3Container.setOnClickListener {
                if (isQueryCompleted) launchPurchaseDialog(Constants.HALF_YEARLY_PLAN)
                else showToast(resources.getString(R.string.membership_failed))
            }
        }
    }

    private fun goBack() {
        if (requireActivity().supportFragmentManager.backStackEntryCount == 0) {
            requireActivity().onBackPressed()
        } else
            clearBackStack()
    }


    private fun launchPurchaseDialog(subscriptionPlan: String) {
        when (subscriptionPlan) {
            Constants.BASIC_PLAN -> {
                if (billingClient.isPurchased(Constants.BASIC_PLAN))
                    showToast(
                        resources.getString(
                            R.string.membership_subscribed
                        )
                    )
                else goForLaunchingPurchaseFlow(Constants.BASIC_PLAN)
            }
            Constants.QUARTERLY_PLAN -> {
                if (billingClient.isPurchased(Constants.QUARTERLY_PLAN)) showToast(
                    resources.getString(R.string.membership_subscribed)
                )
                else goForLaunchingPurchaseFlow(Constants.QUARTERLY_PLAN)
            }
            Constants.HALF_YEARLY_PLAN -> {
                if (billingClient.isPurchased(Constants.HALF_YEARLY_PLAN)) showToast(
                    resources.getString(R.string.membership_subscribed)
                )
                else goForLaunchingPurchaseFlow(Constants.HALF_YEARLY_PLAN)
            }
            Constants.HALF_YEARLY_PLAN_3 -> {
                if (billingClient.isPurchased(Constants.HALF_YEARLY_PLAN_3)) showToast(
                    resources.getString(
                        R.string.membership_subscribed
                    )
                )
                else goForLaunchingPurchaseFlow(Constants.HALF_YEARLY_PLAN_3)
            }

            Constants.BASIC_PLAN_3 -> {
                if (billingClient.isPurchased(Constants.BASIC_PLAN_3)) showToast(
                    resources.getString(
                        R.string.membership_subscribed
                    )
                )
                else goForLaunchingPurchaseFlow(Constants.BASIC_PLAN_3)
            }
            Constants.QUARTERLY_PLAN_3 -> {
                if (billingClient.isPurchased(Constants.QUARTERLY_PLAN_3)) showToast(
                    resources.getString(
                        R.string.membership_subscribed
                    )
                )
                else goForLaunchingPurchaseFlow(Constants.QUARTERLY_PLAN_3)
            }
        }
    }

    private fun goForLaunchingPurchaseFlow(skuName: String) {
        if (isPlanStatusActive) {
            billingClient.launchSubChangeConfirmationFlow(
                requireActivity(),
                skuName,
                purchasedToken ?: ""
            )
        } else {
            billingClient.launchPurchaseFlow(
                requireActivity(),
                skuName
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onBillingEvent(event: BillingEvent, message: String?, responseCode: Int?) {
        when (event) {
            BillingEvent.BILLING_CONNECTED -> {

            }
            BillingEvent.BILLING_CONNECTION_FAILED -> {

            }
            BillingEvent.BILLING_DISCONNECTED -> {

            }
            BillingEvent.QUERY_SKU_DETAILS_COMPLETE -> {
                isQueryCompleted = billingClient.skuDetailsQueried
            }
            BillingEvent.QUERY_SKU_DETAILS_FAILED -> {

            }
            BillingEvent.QUERY_OWNED_PURCHASES_COMPLETE -> {
                billingClient.getAllPurchase().let {
                    if (it.isNotEmpty()) {
                        purchasedToken = it.first()?.purchaseToken
                        binding.subscribedPlanName.text = it.first()?.skus?.first()
                        binding.subscribedPlanDate.text = getDateFromMillis(
                            it.first()?.purchaseTime ?: 0,
                            DATE_FORMAT_4
                        )
                        binding.subscribedPlanCost.text =
                            billingClient.getSkuDetails(it.first()?.skus?.first() ?: "")?.price

                        skuDetails = billingClient.getSkuDetails(it.first()?.skus?.first() ?: "")
                    }
                }
            }
            BillingEvent.QUERY_OWNED_PURCHASES_FAILED -> {

            }
            BillingEvent.PURCHASE_COMPLETE -> {

            }
            BillingEvent.PURCHASE_FAILED -> {

            }
            BillingEvent.PURCHASE_CANCELLED -> {

            }
            BillingEvent.PURCHASE_ACKNOWLEDGE_SUCCESS -> {
                val purchase = Gson().fromJson(message, Purchase::class.java)
                initAppBillingValues(purchase)
                sendToken(purchase)
            }
            BillingEvent.PURCHASE_ACKNOWLEDGE_FAILED -> {
                val purchase = Gson().fromJson(message, Purchase::class.java)
                initAppBillingValues(purchase)
                goBack()
            }
            else -> {
            }
        }
    }

    private fun sendToken(purchase: Purchase) {
        DialogUtils.showDialog(requireActivity(), false)
        viewModel.updateSubscriptionStatus(
            subscriptionStatus = Constants.ACTIVE,
            subscriptionType = planType ?: "",
            subscriptionExpiryDate = purchase.purchaseTime.toString(),
            subscriptionToken = purchase.purchaseToken
        )


    }

    private fun setUpObserver() {
        viewModel.memberShipState.observe(viewLifecycleOwner) { res ->
            when (res) {
                is DashboardViewModel.MemberShipResponseEvent.DataResponse -> {
                    DialogUtils.hideDialog()
                    goBack()
                }
                is DashboardViewModel.MemberShipResponseEvent.Error -> {
                    DialogUtils.hideDialog()
                    res.errorMsg?.let { it -> showToast(it) }
                }
            }
        }

    }

    private fun openPlayStoreAccountForSubscription() {
        if (skuDetails != null) {
            try {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://play.google.com/store/account/subscriptions?sku=$skuDetails&package=${requireActivity().packageName}")
                    )
                )
            } catch (e: ActivityNotFoundException) {
                showToast("Cant open the browser")
                e.printStackTrace()
            }
        }
    }


    private fun initAppBillingValues(purchase: Purchase) {
        if (purchase.purchaseState == 1) {
            /* Save Purchase state */
            MomensityBingoApp.preferencesManager?.setStringValue(
                Constants.SUBSCRIPTION_PLAN_STATUS, Constants.ACTIVE
            )
            MomensityBingoApp.preferencesManager?.setStringValue(
                Constants.SUBSCRIPTION_PLAN_TYPE, planType
            )
        } else {
            MomensityBingoApp.preferencesManager?.setStringValue(
                Constants.SUBSCRIPTION_PLAN_STATUS,
                Constants.INACTIVE
            )
            MomensityBingoApp.preferencesManager?.setStringValue(
                Constants.SUBSCRIPTION_PLAN_TYPE,
                Constants.LEVEL_1
            )
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        billingClient.endClientConnection()
    }


}