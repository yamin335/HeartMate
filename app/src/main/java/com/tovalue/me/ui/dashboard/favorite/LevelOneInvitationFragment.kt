package com.tovalue.me.ui.dashboard.favorite

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.tovalue.me.R
import com.tovalue.me.common.showToast
import com.tovalue.me.common.trimmedText
import com.tovalue.me.databinding.FragmentLevelOneInvitationBinding
import com.tovalue.me.helper.MomensityBingoApp
import com.tovalue.me.model.ProfileInfo
import com.tovalue.me.ui.dashboard.DashboardViewModel
import com.tovalue.me.ui.dashboard.manageInvitation.InvitationHistoryActivity
import com.tovalue.me.ui.dashboard.navigation.NavigationActivity
import com.tovalue.me.util.Constants
import com.tovalue.me.util.Utils.hideKeyboard
import com.tovalue.me.util.Utils.sendMessage
import com.tovalue.me.util.extensions.animateVisibility
import com.tovalue.me.util.extensions.useAdjustPan
import com.tovalue.me.util.livedata.EventObserver


class LevelOneInvitationFragment : Fragment() {
    private var _binding: FragmentLevelOneInvitationBinding? = null
    private val binding get() = _binding!!
    private val invitationViewModel: LevelOneInvitationViewModel by viewModels()
    private var userProfileInfo: ProfileInfo? = null

    private var invitationType = Constants.LEVEL_1
    private var groupLevel = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLevelOneInvitationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpClickListener()
        setUpObserver()

        userProfileInfo = invitationViewModel.getUserObj()

        if (userProfileInfo == null) {
            binding.loader.animateVisibility(View.VISIBLE)
            invitationViewModel.getProfileInfo()
        } else {
            setUpSpinnerAdapter()
        }
    }

    private fun setUpObserver() {
        invitationViewModel.invitation.observe(viewLifecycleOwner) {
            when (it) {
                is LevelOneInvitationViewModel.Invitation.OpenInvitationDialog -> {
                    binding.loader.animateVisibility(View.GONE)
                    shareOnInviteeNumber()
                }
                is LevelOneInvitationViewModel.Invitation.Error -> {
                    binding.loader.animateVisibility(View.GONE)
                    it.msg?.let { it1 -> showToast(it1) }
                }
            }
        }
        invitationViewModel.profileResponse.observe(viewLifecycleOwner,
            EventObserver {
                when (it) {
                    is DashboardViewModel.UiEventProfile.Profile -> {
                        binding.loader.animateVisibility(View.GONE)
                        userProfileInfo = it.response
                        setUpSpinnerAdapter()

                    }
                    is DashboardViewModel.UiEventProfile.COOKIEXPIRE -> {
                        binding.loader.animateVisibility(View.GONE)
                        showToast(it.toString())
                    }
                    is DashboardViewModel.UiEventProfile.Error -> {
                        binding.loader.animateVisibility(View.GONE)
                        showToast(it.errorMsg)
                    }
                }
            })
    }

    private fun shareOnInviteeNumber() {
        val invitationText = getInvitationText()
        sendMessage(requireActivity(), binding.emailPhoneEt.trimmedText, invitationText)
        binding.firstNameET.setText("")
        binding.emailPhoneEt.setText("")
    }


    private fun setUpSpinnerAdapter() {
        userProfileInfo?.let {
            if (it.matchSources == null )
                return

            it.matchSources.add(0, "In Real Life")
            val adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_list_item_1, it.matchSources
            )
            binding.matchSourcesSpinner.adapter = adapter
            //initAppBillingValues(it)
        }
    }

//    private fun initAppBillingValues(profileInfo: ProfileInfo) {
//        MomensityBingoApp.preferencesManager?.setStringValue(
//            Constants.SUBSCRIPTION_PLAN_STATUS, profileInfo.subscriptionStatus
//        )
//        MomensityBingoApp.preferencesManager?.setStringValue(
//            Constants.SUBSCRIPTION_PLAN_TYPE, profileInfo.subscriptionType
//        )
//    }

    private fun setUpClickListener() {

        binding.manageExistingInvitationBtn.setOnClickListener {
            startActivity(Intent(requireContext(), InvitationHistoryActivity::class.java))
        }

        binding.startBtn.setOnClickListener {
            sendInvitation()
        }

        binding.invitationTypeRG.setOnCheckedChangeListener { group, checkedId ->
            val planType =
                MomensityBingoApp.preferencesManager?.getStringValue(Constants.SUBSCRIPTION_PLAN_TYPE)
            when (checkedId) {
                R.id.exploring_matchRB -> {
                    invitationType = Constants.LEVEL_1
                    groupLevel = 1
                }
                R.id.start_dating_journeyRB -> {
                    if (isSubscriptionPlanActive) {
                        invitationType = Constants.LEVEL_2
                        groupLevel = 2
                    } else {
                        group.clearCheck()
                        val bundle = Bundle()
                        bundle.putString(Constants.PASSED_PLAN_TYPE, Constants.LEVEL_2)
                        bundle.putBoolean(Constants.PASSED_PLAN_STATUS, isSubscriptionPlanActive)
                        startActivity(
                            NavigationActivity.createIntent(
                                requireContext(),
                                Constants.SUBSCRIPTION,
                                bundle
                            )
                        )
                    }
                }
                R.id.committed_coupleRB -> {
                    userProfileInfo?.let {
                        if (isSubscriptionPlanActive && planType == Constants.LEVEL_3) {
                            invitationType = Constants.LEVEL_3
                            groupLevel = 3
                        } else {
                            group.clearCheck()
                            val bundle = Bundle()
                            bundle.putString(Constants.PASSED_PLAN_TYPE, Constants.LEVEL_3)
                            bundle.putBoolean(Constants.SUBSCRIPTION_VIEW_FROM_SETTINGS, false)
                            bundle.putBoolean(
                                Constants.PASSED_PLAN_STATUS,
                                isSubscriptionPlanActive
                            )
                            startActivity(
                                NavigationActivity.createIntent(
                                    requireContext(),
                                    Constants.SUBSCRIPTION,
                                    bundle
                                )
                            )
                        }
                    }

                }
            }
        }
    }

    private val isSubscriptionPlanActive
        get() = MomensityBingoApp.preferencesManager?.getStringValue(Constants.SUBSCRIPTION_PLAN_STATUS)
            .equals(Constants.ACTIVE, true)


    private fun sendInvitation() {
        if (binding.firstNameET.trimmedText.isEmpty()) {
            showToast("Please enter first name.")
        } else if (binding.emailPhoneEt.trimmedText.isEmpty())
            showToast("Please enter number.")
        else {
            hideKeyboard(requireActivity())
            binding.loader.animateVisibility(View.VISIBLE)
            invitationViewModel.levelOneInvitation(
                "+".plus(binding.emailPhoneEt.trimmedText),
                firstName = binding.firstNameET.trimmedText,
                matchSource = binding.matchSourcesSpinner.selectedItem.toString(),
                invitationType = invitationType,
                groupLevel = groupLevel
            )
        }
    }

    private fun getInvitationText(): String {
        var invitationText = ""
        userProfileInfo?.invitationArray?.let {
            invitationText = if (invitationType.equals("level_1", true)) {
                it.level2.invitationSMSPtOne.plus(it.level2.invitationSMSPtTwo)
                    .replace("#########,", "+${binding.emailPhoneEt.trimmedText}", true)
            } else if (invitationType.equals("level_2", true)) {
                it.level3.invitationSMSPtOne.plus(it.level3.invitationSMSPtTwo)
                    .replace("#########,", "+${binding.emailPhoneEt.trimmedText}", true)
            } else {
                it.level1.invitationSMSPtOne.plus(it.level1.invitationSMSPtTwo)
                    .replace("#########,", "+${binding.emailPhoneEt.trimmedText}", true)
            }
        }
        return invitationText
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        useAdjustPan()
    }
}