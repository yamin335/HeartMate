package com.tovalue.me.ui.levelTwoInvitation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.tovalue.me.R
import com.tovalue.me.common.showToast
import com.tovalue.me.databinding.FragmentLevelTwoInvitationSecondStepBinding
import com.tovalue.me.ui.levelTwoInvitation.response.CommitmentOfferResponse
import com.tovalue.me.util.DialogUtils

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class LevelTwoInvitationSecondStepFragment : Fragment() {

    private var _binding: FragmentLevelTwoInvitationSecondStepBinding? = null

    private val levelTwoInvitationViewModel: LevelTwoInvitationViewModel by activityViewModels()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentLevelTwoInvitationSecondStepBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpObserver()
        binding.readyBtn.setOnClickListener {
            val comfortResponse = binding.invitationComfortableET.text.toString()
            val balanceResponse = binding.invitationBalanceET.text.toString()
            val safetyResponse = binding.invitationSafeET.text.toString()
            if (isDataValid(comfortResponse, balanceResponse, safetyResponse)) {
                DialogUtils.showDialog(requireContext(), false)
                levelTwoInvitationViewModel.acceptCommitmentOffer(
                    comfortResponse,
                    balanceResponse,
                    safetyResponse
                )
            } else
                showToast("Please enter the values in Input fields.")
        }

    }

    private fun isDataValid(
        comfortResponse: String,
        balanceResponse: String,
        safetyResponse: String
    ): Boolean {
        if (comfortResponse.isEmpty()) {
            return false
        } else if (balanceResponse.isEmpty()) {
            return false
        } else if (safetyResponse.isEmpty()) {
            return false
        }

        return true
    }

    private fun setUpObserver() {
        levelTwoInvitationViewModel.commitmentOfferToPass.observe(viewLifecycleOwner) {
            initViewsWithData(it)
            if (it.acceptance_status != 0)
                disableButton()
        }

        levelTwoInvitationViewModel.acceptCommitmentOfferEventState.observe(viewLifecycleOwner) {
            when (it) {
                is LevelTwoInvitationViewModel.AcceptCommitmentOfferEvent.Error -> {
                    DialogUtils.hideDialog()
                    it.errorMsg?.let { it1 -> showToast(it1) }
                }
                is LevelTwoInvitationViewModel.AcceptCommitmentOfferEvent.DataResponse -> {
                    DialogUtils.hideDialog()
                    requireActivity().finish()
                }
            }
        }
    }

    private fun initViewsWithData(commitmentOfferResponse: CommitmentOfferResponse) {

        binding.invitationDescriptionTextTv.text =
            commitmentOfferResponse.level_2_invitation.description
        binding.headerTv.text = commitmentOfferResponse.level_2_invitation.header

        binding.invitationComfortableTv.text = getString(
            R.string.string_with_pint,
            commitmentOfferResponse.level_2_invitation.comfortable_text
        )
        binding.invitationBalanceTv.text = getString(
            R.string.string_with_pint, commitmentOfferResponse.level_2_invitation.balance_text
        )
        binding.invitationSafeTv.text = getString(
            R.string.string_with_pint, commitmentOfferResponse.level_2_invitation.safe_text
        )

    }

    private fun disableButton() {
        binding.readyBtn.isClickable = false
        binding.readyBtn.isEnabled = false

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}