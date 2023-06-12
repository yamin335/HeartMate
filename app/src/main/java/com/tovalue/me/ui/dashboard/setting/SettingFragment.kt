package com.tovalue.me.ui.dashboard.setting

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.tovalue.me.R
import com.tovalue.me.common.showToast
import com.tovalue.me.databinding.FragmentSettingBinding
import com.tovalue.me.helper.MomensityBingoApp
import com.tovalue.me.model.CorporateInfoResponse
import com.tovalue.me.model.ProfileInfo
import com.tovalue.me.ui.auth.AuthHostActivity.Companion.createAuthIntent
import com.tovalue.me.ui.browser.VisitLinksActivity
import com.tovalue.me.ui.dashboard.DashboardViewModel
import com.tovalue.me.ui.dashboard.navigation.NavigationActivity.Companion.createIntent
import com.tovalue.me.ui.dashboard.user.UpdateUserEmailDialog
import com.tovalue.me.util.Constants
import com.tovalue.me.util.Constants.EMAIL_SETTING
import com.tovalue.me.util.Constants.PUSH_SETTING
import com.tovalue.me.util.DialogUtils
import com.tovalue.me.util.Utils
import com.tovalue.me.util.livedata.EventObserver

class SettingFragment : Fragment(), Utils.AlertActionListener {
    private var _binding: FragmentSettingBinding? = null
    private val binding get() = _binding!!
    private lateinit var profileObj: ProfileInfo
    private lateinit var corporateLinks: CorporateInfoResponse
    private val viewModel: DashboardViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentSettingBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.topBarLayout.topBarTv.text = resources.getString(R.string.title_activity_settings)
        //binding.topBarLayout.topBarTv.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)

        setUpClickListener()
        setUpObserver()
        viewModel.getCorporateInfo()
    }

    private fun setUpObserver() {
        viewModel.state.observe(viewLifecycleOwner,
            EventObserver {
                DialogUtils.hideDialog()
                when (it) {
                    is DashboardViewModel.UiEventAccount.Exit -> {
                        when (it.exitType) {
                            DashboardViewModel.AppExitType.LOGOUT -> {
                                viewModel.flushSavedData()
                                goToAuthScreen()
                            }
                            DashboardViewModel.AppExitType.DEACTIVATE -> {
                                viewModel.flushSavedData()
                                goToAuthScreen()
                            }
                        }
                    }
                    is DashboardViewModel.UiEventAccount.Error -> it.errorMsg?.let { it1 ->
                        showToast(
                            it1
                        )
                    }
                }
            })

        viewModel.corporateInfoResponse.observe(viewLifecycleOwner,
            EventObserver {
                DialogUtils.hideDialog()
                when (it) {
                    is DashboardViewModel.UiEventCorporate.CorporateLinks -> {
                        corporateLinks = it.response
                    }
                    is DashboardViewModel.UiEventCorporate.Error -> showToast(
                        it.errorMsg
                    )
                }
            })

        viewModel.profileResponse.observe(viewLifecycleOwner,
            EventObserver {
                when (it) {
                    is DashboardViewModel.UiEventProfile.Profile -> {
                        setUpViews()
                    }
                    is DashboardViewModel.UiEventProfile.COOKIEXPIRE -> {
                        viewModel.flushSavedData()
                        goToAuthScreen()
                    }
                    is DashboardViewModel.UiEventProfile.Error -> {
                        showToast(it.errorMsg)
                    }
                }
            })
    }


    private fun setUpViews() {
        profileObj = viewModel.getUserObj()
        binding.emailTv.text = profileObj.email
        binding.phoneNoTv.text = "+" + profileObj.phone

        if (MomensityBingoApp.preferencesManager?.getStringValue(Constants.SUBSCRIPTION_PLAN_STATUS)== Constants.ACTIVE) {
            binding.upgradeMembershipTv.text = getString(R.string.manage_my_membership)
        }
    }

    override fun onResume() {
        super.onResume()
        setUpViews()
        Log.i("TAG-->", "onResume")
    }

    private fun setUpClickListener() {
        binding.topBarLayout.topBarTv.setOnClickListener {
            requireActivity().finish()
        }

        binding.logoutTv.setOnClickListener {
            logout()
        }
        binding.deleteAccountTv.setOnClickListener {
            Utils.showGenericAlertDialog(
                requireActivity(), resources.getString(R.string.alert),
                resources.getString(R.string.confirm_account_delete),
                this
            )
        }
        binding.licenseTv.setOnClickListener {
            goToLinksActivity(corporateLinks.licenses)
        }
        binding.membershipPrincipleTv.setOnClickListener {
            goToLinksActivity(corporateLinks.memberPrinciples)
        }
        binding.privacyPolicyTv.setOnClickListener {
            goToLinksActivity(corporateLinks.privacyPolicy)
        }
        binding.termsServiceTv.setOnClickListener {
            goToLinksActivity(corporateLinks.termsOfService)
        }
        binding.safeTipsTv.setOnClickListener {
            goToLinksActivity(corporateLinks.safeDatingTips)
        }
        binding.notificationTv.setOnClickListener {
            startActivity(createIntent(requireActivity(), PUSH_SETTING))
        }
        binding.pushEmailTv.setOnClickListener {
            startActivity(createIntent(requireActivity(), EMAIL_SETTING))
        }
        binding.upgradeMembershipTv.setOnClickListener {
            val planType =
                MomensityBingoApp.preferencesManager?.getStringValue(Constants.SUBSCRIPTION_PLAN_TYPE)
            val planStatus =
                MomensityBingoApp.preferencesManager?.getStringValue(Constants.SUBSCRIPTION_PLAN_STATUS)
            val bundle = Bundle()
            bundle.putString(Constants.PASSED_PLAN_TYPE, planType)
            bundle.putBoolean(Constants.SUBSCRIPTION_VIEW_FROM_SETTINGS, true)
            bundle.putBoolean(
                Constants.PASSED_PLAN_STATUS,
                planStatus.equals(Constants.ACTIVE, true)
            )
            startActivity(
                createIntent(
                    requireContext(),
                    Constants.SUBSCRIPTION,
                    bundle
                )
            )
        }
        binding.emailTv.setOnClickListener {
            showEmailChangeDialog()
        }
    }

    private fun showEmailChangeDialog() {

        val dialog = UpdateUserEmailDialog()
        dialog.show(parentFragmentManager, SettingFragment::class.java.simpleName)

    }

    private fun goToLinksActivity(link: String) {
        val intent = Intent(requireActivity(), VisitLinksActivity::class.java).apply {
            putExtra("url", link)
        }
        startActivity(intent)
    }

    private fun logout() {
        DialogUtils.showDialog(requireActivity(), false)
        viewModel.logOut()
    }

    private fun confirmAccountDeletion() {
        DialogUtils.showDialog(requireActivity(), false)
        viewModel.deactivateAccount()
    }

    private fun goToAuthScreen() {
        startActivity(
            createAuthIntent(requireActivity()).addFlags(
                Intent.FLAG_ACTIVITY_CLEAR_TOP or
                        Intent.FLAG_ACTIVITY_NEW_TASK or
                        Intent.FLAG_ACTIVITY_CLEAR_TASK
            )
        )
        requireActivity().finish()
    }

    override fun onAlertDismissWithOK() {
        confirmAccountDeletion()
    }

    override fun onAlertDismissWithCancel() {

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}