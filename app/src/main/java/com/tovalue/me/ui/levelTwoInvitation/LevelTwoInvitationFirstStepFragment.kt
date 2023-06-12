package com.tovalue.me.ui.levelTwoInvitation

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.tovalue.me.R
import com.tovalue.me.common.showToast
import com.tovalue.me.databinding.FragmentLevelTwoInvitationFirstStepBinding

import com.tovalue.me.ui.dashboard.navigation.NavigationActivity

import com.tovalue.me.ui.levelTwoInvitation.response.CommitmentOfferResponse
import com.tovalue.me.util.Constants
import com.tovalue.me.util.DialogUtils
import com.tovalue.me.util.extensions.addFragmentSafely


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class LevelTwoInvitationFirstStepFragment : Fragment() {

    private val levelTwoInvitationViewModel: LevelTwoInvitationViewModel by activityViewModels()

    private var _binding: FragmentLevelTwoInvitationFirstStepBinding? = null
    private var offerPostId = 0

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentLevelTwoInvitationFirstStepBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments?.containsKey(LevelTwoInvitationActivity.ARGUMENT_PARAM1) == true) {
            offerPostId = arguments?.getInt(LevelTwoInvitationActivity.ARGUMENT_PARAM1) ?: 101
        }
    }

    private fun makeApiCall() {
        DialogUtils.showDialog(requireActivity(), false)
        levelTwoInvitationViewModel.getCommitmentOffer(offerPostId)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        makeApiCall()
        setUpObserver()

    }

    private fun setUpObserver() {
        levelTwoInvitationViewModel.commitmentOfferEventState.observe(viewLifecycleOwner) {
            when (it) {
                is LevelTwoInvitationViewModel.CommitmentOfferEvent.Error -> {
                    DialogUtils.hideDialog()
                    it.errorMsg?.let { it1 -> showToast(it1) }
                }
                is LevelTwoInvitationViewModel.CommitmentOfferEvent.DataResponse -> {
                    DialogUtils.hideDialog()
                    initViewWithData(it.response)
                }
            }
        }

        levelTwoInvitationViewModel.rejectCommitmentOfferEventState.observe(viewLifecycleOwner) {
            when (it) {
                is LevelTwoInvitationViewModel.RejectCommitmentOfferEvent.Error -> {
                    DialogUtils.hideDialog()
                    it.errorMsg?.let { it1 -> showToast(it1) }
                }
                is LevelTwoInvitationViewModel.RejectCommitmentOfferEvent.DataResponse -> {
                    DialogUtils.hideDialog()
                    requireActivity().finish()
                }
            }
        }

        levelTwoInvitationViewModel.doubleConfirmationOfferEventState.observe(viewLifecycleOwner) {
            when (it) {
                is LevelTwoInvitationViewModel.DoubleConfirmationOfferEvent.Error -> {
                    DialogUtils.hideDialog()
                    it.errorMsg?.let { it1 -> showToast(it1) }
                }
                is LevelTwoInvitationViewModel.DoubleConfirmationOfferEvent.DataResponse -> {
                    DialogUtils.hideDialog()
                    requireActivity().finish()


                    startActivity(
                        NavigationActivity.createIntent(
                            requireActivity(),
                            Constants.DATING_JOURNEY_HOME
                        )
                    )


                }
            }
        }
    }


    private fun initViewWithData(response: CommitmentOfferResponse) {

        Glide.with(requireContext()).load(response.inviter_avatar)
            .error(R.drawable.default_avatar)
            .into(binding.profileImg)

        binding.inviterNameTv.text = response.inviter_name
        binding.headerTv.text = response.header_text
        binding.footerTextTv.text = response.footer_text
        binding.descriptionTextTv.text = response.description_text

        binding.comfortableLevelTv.text = response.comfortable_label
        binding.balanceLabelTv.text = response.balance_label
        binding.safeLabelTv.text = response.safe_label

        binding.comfortableTv.text = response.comfortable_response
        binding.balanceTv.text = response.balance_response
        binding.safeTv.text = response.safe_response

        binding.submissionOptionsBtn.setOnClickListener {
            setDropDownList(it, response)
        }

    }

    private fun setDropDownList(view: View, commitmentOfferResponse: CommitmentOfferResponse) {
        val popup = PopupMenu(requireContext(), view)
        for (option in commitmentOfferResponse.submission_options) {
            popup.menu.add(option).title = option
        }
        popup.setOnMenuItemClickListener { item ->
            if (item.title.contains("Yes")) {
                if (commitmentOfferResponse.sender == 1 && commitmentOfferResponse.acceptance_status != 0) {
                    levelTwoInvitationViewModel.commitmentDoubleConfirmation()
                } else if (commitmentOfferResponse.sender == 0) {
                    addFragmentSafely(
                        containerViewId = R.id.fragment_container,
                        fragment = LevelTwoInvitationSecondStepFragment()
                    )
                } else {
                    showToast("Already Confirmed")
                }
            } else if (item.title.contains("No")) {
                if (commitmentOfferResponse.acceptance_status != 0)
                    levelTwoInvitationViewModel.rejectCommitmentOffer(2)
                else {
                    showToast("Already Rejected")
                }
            } else {
                if (commitmentOfferResponse.acceptance_status != 0)
                    levelTwoInvitationViewModel.rejectCommitmentOffer(1)
                else {
                    showToast("Already Rejected")
                }
            }

            false
        }
        popup.show()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(
            param1: Int,
        ): LevelTwoInvitationFirstStepFragment {
            val fragment = LevelTwoInvitationFirstStepFragment()
            val args = Bundle()
            args.putInt(LevelTwoInvitationActivity.ARGUMENT_PARAM1, param1)
            fragment.arguments = args
            return fragment
        }

    }
}