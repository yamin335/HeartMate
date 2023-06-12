package com.tovalue.me.ui.catalog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.tovalue.me.R
import com.tovalue.me.common.showToast
import com.tovalue.me.databinding.FragmentUpGradeToLevelBinding
import com.tovalue.me.ui.catalog.viewmodel.UpGradeToLevelViewModel
import com.tovalue.me.ui.dashboard.navigation.NavigationActivity
import com.tovalue.me.util.Constants
import com.tovalue.me.util.DialogUtils


class UpGradeToLevelFragment : Fragment() {

    private var _binding: FragmentUpGradeToLevelBinding? = null
    private val binding get() = _binding!!
    private var args: Bundle? = null
    private val upGradeToLevelViewModel: UpGradeToLevelViewModel by activityViewModels()
    var groupId = 0
    var question1 = ""
    var question2 = ""
    var question3 = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUpGradeToLevelBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        args = arguments
        setViews()
        sendData()
        setUpObserver()
    }

    private fun sendData() {
        binding.nextStepBtn.setOnClickListener {
            if (upGradeToLevelViewModel.activeSubscription()) {
                when (upGradeToLevelViewModel.subscriptionType()) {
                    Constants.LEVEL_1 -> goToPlanSubscriptionFragment(Constants.LEVEL_1)
                    Constants.LEVEL_3 -> updateData()
                    Constants.LEVEL_2 -> goToPlanSubscriptionFragment(Constants.LEVEL_2)
                }
            } else {
                goToPlanSubscriptionFragment(Constants.LEVEL_3)
            }
        }
    }

    private fun goToPlanSubscriptionFragment(planType: String) {
        val bundle = Bundle()
        bundle.putString(Constants.PASSED_PLAN_TYPE, planType)
        bundle.putString(Constants.PASSED_PLAN_STATUS, "")
        startActivity(
            NavigationActivity.createIntent(requireContext(), Constants.SUBSCRIPTION, bundle)
        )
    }

    private fun updateData() {
        question1 = binding.edtQuestion1.text.toString()
        question2 = binding.edtQuestion2.text.toString()
        question3 = binding.edtQuestion3.text.toString()

        DialogUtils.showDialog(requireActivity(), false)
        upGradeToLevelViewModel.UpGradeToLevel(
            groupId,
            level = 3,
            question1,
            question2,
            question3
        )
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


    private fun setViews() {

        args?.let {

            val name = it.get(Constants.SELECTED_NAME) as String
            val level = it.get(Constants.LEVEL_COUNT) as String
            groupId = it.get(Constants.GROUP_ID) as Int


            binding.subHeading.text = HtmlCompat.fromHtml(
                resources
                    .getString(R.string.levelInvitationSubHeading, name, level),
                HtmlCompat.FROM_HTML_MODE_LEGACY
            )

            binding.question1.text =
                HtmlCompat.fromHtml(
                    resources
                        .getString(R.string.levelInvitationQ1, name),
                    HtmlCompat.FROM_HTML_MODE_LEGACY
                )


            binding.question2.text =
                HtmlCompat.fromHtml(
                    resources
                        .getString(R.string.levelInvitationQ2, name),
                    HtmlCompat.FROM_HTML_MODE_LEGACY
                )



            binding.question3.text =
                HtmlCompat.fromHtml(
                    resources
                        .getString(R.string.levelInvitationQ3, name, name),
                    HtmlCompat.FROM_HTML_MODE_LEGACY
                )


        }

    }
}