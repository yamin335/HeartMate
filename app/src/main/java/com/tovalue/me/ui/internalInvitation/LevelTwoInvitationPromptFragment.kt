package com.tovalue.me.ui.internalInvitation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.tovalue.me.R
import com.tovalue.me.common.showToast
import com.tovalue.me.databinding.FragmentLevelTwoInvitationPromptBinding
import com.tovalue.me.ui.catalog.viewmodel.UpGradeToLevelViewModel
import com.tovalue.me.ui.dashboard.DashboardViewModel
import com.tovalue.me.ui.dashboard.navigation.NavigationActivity
import com.tovalue.me.util.Constants
import com.tovalue.me.util.Constants.GROUP_ID
import com.tovalue.me.util.Constants.SELECTEDTVMScore
import com.tovalue.me.util.Constants.SELECTED_AVATAR
import com.tovalue.me.util.Constants.SELECTED_NAME
import com.tovalue.me.util.Constants.SUBSCRIPTION
import com.tovalue.me.util.DialogUtils

class LevelTwoInvitationPromptFragment : Fragment() {
    private var _binding: FragmentLevelTwoInvitationPromptBinding? = null
    private val binding get() = _binding!!
    private val dashboardViewModel: DashboardViewModel by viewModels()
    private val upGradeToLevelViewModel: UpGradeToLevelViewModel by viewModels()
    private val groupId: Int by lazy { arguments?.getInt(GROUP_ID)!!.toInt() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLevelTwoInvitationPromptBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpViews()
        setUpClickListener()
        setUpObserver()
    }

    private fun setUpViews() {
        arguments?.let {
            val name = it.getString(SELECTED_NAME)
            binding.title.text = "Invite $name to Level 2"
            Glide.with(requireActivity()).load(it.getString(SELECTED_AVATAR))
                .into(binding.profileImg)
            binding.nameTv.text = name
            binding.datingNumberTv.text = it.getString(SELECTEDTVMScore)
            binding.experienceDetail.text =
                name.plus(", ").plus(resources.getString(R.string.level_two_invitation_quote))
        }
        binding.roundedDot.startSpin()
    }

    private fun setUpClickListener() {
        binding.upgradeLevel2Btn.setOnClickListener {
            checkActiveSubscription()
        }
        binding.cancelImg.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    private fun setUpObserver() {
        upGradeToLevelViewModel.journeyState.observe(viewLifecycleOwner) {
            when (it) {
                is UpGradeToLevelViewModel.UpGradeTiLevelEvent.Error -> {
                    DialogUtils.hideDialog()
                    it.errorMsg?.let { it1 -> showToast(it1) }
                }
                is UpGradeToLevelViewModel.UpGradeTiLevelEvent.DataResponse -> {
                    DialogUtils.hideDialog()
                    activity?.onBackPressed()
                    Toast.makeText(
                        requireContext(),
                        "Response send successfully",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun checkActiveSubscription() {
        if (dashboardViewModel.activeSubscription()) {
            when (dashboardViewModel.subscriptionType()) {
                Constants.LEVEL_1 -> goToPlanSubscriptionFragment()
                Constants.LEVEL_2 -> updateLevel()
                Constants.LEVEL_3 -> updateLevel()
            }
            return
        }

        if (!dashboardViewModel.activeSubscription()) goToPlanSubscriptionFragment()
    }

    private fun updateLevel() {
        DialogUtils.showDialog(requireActivity(), false)
        upGradeToLevelViewModel.UpGradeToLevel(
            groupId,
            level = 2,
        )
    }

    private fun goToPlanSubscriptionFragment() {
        val bundle = Bundle()
        bundle.putString(Constants.PASSED_PLAN_TYPE, Constants.LEVEL_1)
        bundle.putString(Constants.PASSED_PLAN_STATUS, "")
        startActivity(
            NavigationActivity.createIntent(requireContext(), SUBSCRIPTION, bundle)
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}