package com.tovalue.me.ui.dashboard.user

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.tovalue.me.R
import com.tovalue.me.common.showToast
import com.tovalue.me.databinding.FragmentUpdateUserBinding
import com.tovalue.me.model.ProfileInfo
import com.tovalue.me.ui.auth.AuthHostActivity
import com.tovalue.me.ui.dashboard.DashboardViewModel
import com.tovalue.me.util.Utils

class UpdateUserEmailDialog : DialogFragment() {
    private var _binding: FragmentUpdateUserBinding? = null
    private val binding get() = _binding!!
    private val viewModel: DashboardViewModel by activityViewModels()

    private lateinit var profileObj: ProfileInfo

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUpdateUserBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isCancelable = false

        setUpViews()
        setUpObserver()
        setUpClickListener()
    }

    override fun getTheme(): Int {
        return R.style.DialogTheme
    }

    private fun setUpObserver() {
        viewModel.updateState.observe((this) ) {
            when (it) {
                is DashboardViewModel.UiEventUpdate.Update -> {
                    viewModel.getProfileInfo(viewModel.getUserId())
                    showToast("Updated Successfully")
                    dismiss()
                }
                is DashboardViewModel.UiEventUpdate.COOKIEXPIRE -> {
                    viewModel.flushSavedData()
                    goToLoginScreen()
                }

                is DashboardViewModel.UiEventUpdate.Error -> {
                    showToast(it.errorMsg)
                }
            }
        }
    }


    private fun goToLoginScreen() {
        // if cookie expire goto starter screen
        // not good practice to use user creds to get the fresh cookie
        startActivity(
            AuthHostActivity.createAuthIntent(requireActivity()).addFlags(
                Intent.FLAG_ACTIVITY_CLEAR_TOP or
                        Intent.FLAG_ACTIVITY_NEW_TASK or
                        Intent.FLAG_ACTIVITY_CLEAR_TASK
            )
        )
        requireActivity().finish()
    }

    private fun setUpClickListener() {
        binding.saveButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            if (email.isEmpty() || !Utils.isValidEmail(email)) {
                showToast("Email is empty or invalid.")
                return@setOnClickListener
            }
            viewModel.verifyEmail(
                profileObj.firstname,
                profileObj.lastname,
                email,
                profileObj.displayname
            )
        }
        binding.cancelButton.setOnClickListener {
            dismiss()
        }
    }

    private fun setUpViews() {
        profileObj = viewModel.getUserObj()
        binding.emailEditText.setText(profileObj.email)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}