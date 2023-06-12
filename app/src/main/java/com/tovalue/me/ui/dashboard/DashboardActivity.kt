package com.tovalue.me.ui.dashboard

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.navigation.NavigationBarView
import com.tovalue.me.R
import com.tovalue.me.adapter.ViewPager2Adapter
import com.tovalue.me.billing.BillingEvent
import com.tovalue.me.billing.BillingHelper
import com.tovalue.me.billing.BillingHelperOB
import com.tovalue.me.billing.BillingListener
import com.tovalue.me.databinding.ActivityDashboardBinding
import com.tovalue.me.helper.MomensityBingoApp
import com.tovalue.me.helper.PreferencesManager
import com.tovalue.me.ui.dashboard.navigation.NavigationActivity
import com.tovalue.me.util.Constants
import com.tovalue.me.util.livedata.EventObserver
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking


class DashboardActivity : AppCompatActivity(), NavigationBarView.OnItemSelectedListener,
    BillingListener {
    private lateinit var _binding: ActivityDashboardBinding
    private val pagerAdapter: ViewPager2Adapter by lazy { ViewPager2Adapter(this) }
    private lateinit var billingClient: BillingHelper
    private val viewModel: DashboardViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDashboardBinding.inflate(layoutInflater)
        checkBillingStatus()
        setContentView(_binding.root)
        checkDeviceToken()
        viewModel.updateState
        setUpViews()
        setUpObserver()
    }

    // parallel api calls
    private fun checkDeviceToken() {
        runBlocking {
            val countBadgeResult = async { viewModel.getUnreadNotificationCount() }
            if (!viewModel.isFreshTokenNeeded()) {
                val tokenResult = async { viewModel.sendToken() }
                tokenResult.await()
            }
            countBadgeResult.await()
        }
    }

    private fun checkBillingStatus() {
        MomensityBingoApp.preferencesManager?.let {
            billingClient = BillingHelperOB.getBillingOb(this)
            billingClient.addBillingListener(this)
        }
    }


    private fun setUpViews() {
        _binding.dashboardBottomNav.setOnItemSelectedListener(this)




        pagerAdapter.apply {
            add(ViewPager2Adapter.FragmentName.PROFILE)
            add(ViewPager2Adapter.FragmentName.PUSH)
            add(ViewPager2Adapter.FragmentName.HOME)
            add(ViewPager2Adapter.FragmentName.FAVORITE)
            add(ViewPager2Adapter.FragmentName.UPCOMING_PLANS)
        }

        _binding.dashboardPager.apply {
            isUserInputEnabled = false
            adapter = pagerAdapter
            offscreenPageLimit = 5
        }

        if (intent.hasExtra("isFromGuideActivity")) {
            if (intent.getIntExtra("guideId", 0) == 2) {
                _binding.dashboardBottomNav.selectedItemId = R.id.dashboard_fav
                _binding.dashboardPager.setCurrentItem(3, true)
            } else if (intent.getIntExtra("guideId", 0) == 3) {
                _binding.dashboardBottomNav.selectedItemId = R.id.dashboard_home
                _binding.dashboardPager.setCurrentItem(2, true)
            } else if (intent.getIntExtra("guideId", 0) == 4) {
                _binding.dashboardBottomNav.selectedItemId = R.id.dashboard_profile
                startActivity(
                    NavigationActivity.createIntent(
                        this@DashboardActivity,
                        Constants.DATE_NIGHT
                    )
                )
            } else {
                _binding.dashboardBottomNav.selectedItemId = R.id.dashboard_profile
                startActivity(
                    NavigationActivity.createIntent(
                        this@DashboardActivity,
                        Constants.INVENTORY_CATEGORY_FRAGMENT
                    )
                )
            }
        } else {
            _binding.dashboardBottomNav.selectedItemId = R.id.dashboard_profile
        }
    }

    private fun setUpObserver() {
        viewModel.unreadNotificationCountResponse.observe(this,
            EventObserver {
                when (it) {
                    is DashboardViewModel.UiEventUnreadNotificationCount.UiEventUnreadCount -> {
                        if (it.response.count > 0)
                            _binding.dashboardBottomNav.getOrCreateBadge(R.id.dashboard_notification).number =
                                it.response.count
                    }
                    is DashboardViewModel.UiEventUnreadNotificationCount.Error -> {
                        Log.d("DashboardActivity", it.errorMsg)
                    }
                }
            })
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.dashboard_profile -> {
                _binding.dashboardPager.setCurrentItem(0, true)
            }
            R.id.dashboard_notification -> {
                _binding.dashboardPager.setCurrentItem(1, true)
            }
            R.id.dashboard_home -> {
                _binding.dashboardPager.setCurrentItem(2, true)
            }
            R.id.dashboard_fav -> {
                _binding.dashboardPager.setCurrentItem(3, true)
            }
            R.id.dashboard_upcoming_plans -> {
                _binding.dashboardPager.setCurrentItem(4, true)
            }
        }
        return true
    }

    fun changeViewpager(id: Int) {
        _binding.dashboardBottomNav.selectedItemId = id
    }


    override fun onBillingEvent(event: BillingEvent, message: String?, responseCode: Int?) {
        if (event == BillingEvent.QUERY_OWNED_PURCHASES_COMPLETE) {

            PreferencesManager.getInstance(this)?.let {

                var planStatus = Constants.INACTIVE
                var planType = Constants.LEVEL_1

                if (billingClient.isPurchased(Constants.BASIC_PLAN) or
                    billingClient.isPurchased(Constants.QUARTERLY_PLAN) or
                    billingClient.isPurchased(Constants.HALF_YEARLY_PLAN)
                ) {
                    planStatus = Constants.ACTIVE
                    planType = Constants.LEVEL_2
                } else if (billingClient.isPurchased(Constants.BASIC_PLAN_3) or
                    (billingClient.isPurchased(Constants.QUARTERLY_PLAN_3)) or
                    (billingClient.isPurchased(Constants.HALF_YEARLY_PLAN_3))
                ) {
                    planStatus = Constants.ACTIVE
                    planType = Constants.LEVEL_3
                }
                /* Save purchased State*/
                it.setStringValue(
                    Constants.SUBSCRIPTION_PLAN_STATUS,
                    planStatus
                )
                it.setStringValue(
                    Constants.SUBSCRIPTION_PLAN_TYPE,
                    planType
                )
            }
        }
    }


    override fun onBackPressed() {
        super.onBackPressed()

    }
}