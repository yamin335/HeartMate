package com.tovalue.me.ui.dashboard.manageInvitation

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.tovalue.me.common.showToast
import com.tovalue.me.databinding.ActivityManageInvitationHistoryBinding
import com.tovalue.me.util.DialogUtils

class InvitationHistoryActivity : AppCompatActivity() {


    private val invitationViewModel: InvitationViewModel by viewModels()
    private lateinit var invitationHistoryBinding: ActivityManageInvitationHistoryBinding

    private lateinit var invitationHistoryAdapter: InvitationHistoryAdapter
    private var positionT0Remove = -1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        invitationHistoryBinding = ActivityManageInvitationHistoryBinding.inflate(layoutInflater)
        setContentView(invitationHistoryBinding.root)

        DialogUtils.showDialog(this, false)
        invitationViewModel.getInvitationHistory()

        setUpObserver()

        invitationHistoryAdapter = InvitationHistoryAdapter(
            this,
            itemClick = { invitationData: InvitationData, position: Int ->
                positionT0Remove = position
                DialogUtils.showDialog(this, false)
                invitationViewModel.deleteInvitationHistory(
                    invitationData.invitation_code,
                    invitationData.group_id
                )
            })

        invitationHistoryBinding.invitationHistoryRV.adapter = invitationHistoryAdapter

        invitationHistoryBinding.backBtn.setOnClickListener {
            finish()
        }
    }


    private fun setUpObserver() {
        invitationViewModel.invitationHistoryState.observe(this) {
            when (it) {
                is InvitationViewModel.InvitationHistoryEvent.Error -> {
                    DialogUtils.hideDialog()
                    it.errorMsg?.let { it1 -> showToast(it1) }
                    toggleVisibility(true)
                }
                is InvitationViewModel.InvitationHistoryEvent.DataResponse -> {
                    DialogUtils.hideDialog()
                    if (it.response.InvitationsHistory.isNullOrEmpty().not()) {
                        invitationHistoryAdapter.setData(it.response.InvitationsHistory)
                        toggleVisibility(false)
                    } else {
                        toggleVisibility(true)
                    }

                }
            }
        }

        invitationViewModel.deleteInvitationHistoryState.observe(this) { deleteResposneData ->
            when (deleteResposneData) {
                is InvitationViewModel.DeleteInvitationHistoryEvent.Error -> {
                    DialogUtils.hideDialog()
                    deleteResposneData.errorMsg?.let { it -> showToast(it) }
                }
                is InvitationViewModel.DeleteInvitationHistoryEvent.DataResponse -> {
                    DialogUtils.hideDialog()
                    if (positionT0Remove != -1)
                        invitationHistoryAdapter.removeItem(positionT0Remove)

                    toggleVisibility(invitationHistoryAdapter.getInvitationList().isEmpty())


                }
            }
        }
    }

    private fun toggleVisibility(showEmptyView: Boolean) {
        if (showEmptyView) {
            invitationHistoryBinding.emptyViewText.visibility = View.VISIBLE
            invitationHistoryBinding.invitationHistoryRV.visibility = View.GONE
        } else {
            invitationHistoryBinding.emptyViewText.visibility = View.GONE
            invitationHistoryBinding.invitationHistoryRV.visibility = View.VISIBLE
        }
    }

}