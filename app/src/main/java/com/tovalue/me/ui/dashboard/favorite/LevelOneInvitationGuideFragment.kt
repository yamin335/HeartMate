package com.tovalue.me.ui.dashboard.favorite

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.tovalue.me.R
import com.tovalue.me.common.showToast
import com.tovalue.me.common.trimmedText
import com.tovalue.me.databinding.FragmentLevelOneInvitationBinding
import com.tovalue.me.databinding.FragmentLevelOneInvitationGuidelineBinding
import com.tovalue.me.helper.MomensityBingoApp
import com.tovalue.me.model.ProfileInfo
import com.tovalue.me.ui.auth.AuthViewModel
import com.tovalue.me.ui.dashboard.DashboardActivity
import com.tovalue.me.ui.dashboard.DashboardViewModel
import com.tovalue.me.ui.dashboard.manageInvitation.InvitationHistoryActivity
import com.tovalue.me.ui.dashboard.navigation.NavigationActivity
import com.tovalue.me.util.Constants
import com.tovalue.me.util.Utils.hideKeyboard
import com.tovalue.me.util.Utils.sendMessage
import com.tovalue.me.util.extensions.animateVisibility
import com.tovalue.me.util.extensions.replaceFragmentSafely
import com.tovalue.me.util.extensions.useAdjustPan
import com.tovalue.me.util.livedata.EventObserver


class LevelOneInvitationGuideFragment : Fragment() {
    private var _binding: FragmentLevelOneInvitationGuidelineBinding? = null
    private val binding get() = _binding!!
    private val authViewModel: AuthViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLevelOneInvitationGuidelineBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpClickListener()

    }


    private fun setUpClickListener() {

        binding.yesBtn.setOnClickListener {
            replaceFragmentSafely(
                LevelOneInvitationFragment(),
                containerViewId = R.id.host_root
            )
        }

        binding.notYetBtn.setOnClickListener {
            goToDashboard()
        }


    }
    private fun goToDashboard() {
        // need to show Date night catalog for fresh registration for once
        authViewModel.redirectToInvitation(true)
        authViewModel.setUserLoggedIn(true)
        authViewModel.setBookMarkProgress("")
        context?.startActivity(Intent(context, DashboardActivity::class.java))
        requireActivity().finish()
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