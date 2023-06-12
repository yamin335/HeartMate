package com.tovalue.me.ui.dashboard.manageInvitation

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tovalue.me.databinding.ItemManageInvitationHistoryBinding

class InvitationHistoryAdapter(
    val context: Context,
    private val itemClick: (InvitationData,Int) -> Unit
) : RecyclerView.Adapter<InvitationHistoryAdapter.ManageInvitationHistoryAdapterViewHolder>() {

    private var invitationsHistoryData = ArrayList<InvitationData>()

    fun setData(invitationsHistoryData: ArrayList<InvitationData>) {
        this.invitationsHistoryData.clear()
        this.invitationsHistoryData.addAll(invitationsHistoryData)
        notifyItemRangeInserted(0, invitationsHistoryData.size)
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ManageInvitationHistoryAdapterViewHolder {
        return ManageInvitationHistoryAdapterViewHolder(
            ItemManageInvitationHistoryBinding.inflate(
                LayoutInflater.from(
                    context
                ), parent, false
            )
        )
    }


    override fun getItemCount(): Int = invitationsHistoryData.size


    inner class ManageInvitationHistoryAdapterViewHolder constructor(val vb: ItemManageInvitationHistoryBinding) :
        RecyclerView.ViewHolder(vb.root)

    override fun onBindViewHolder(holder: ManageInvitationHistoryAdapterViewHolder, position: Int) {
        setUpViewForItemView(holder, position)
        holder.vb.deleteInvitationBtn.setOnClickListener {
            itemClick.invoke(invitationsHistoryData[position],position)
        }
    }

    private fun setUpViewForItemView(
        invitationsHistoryAdapterViewHolder: ManageInvitationHistoryAdapterViewHolder,
        position: Int
    ) {
        invitationsHistoryAdapterViewHolder.vb.phoneNumberTv.text =
            if (invitationsHistoryData[position].invitee_number.isEmpty()) "N/A" else invitationsHistoryData[position].invitee_number
        invitationsHistoryAdapterViewHolder.vb.daysCountTv.text =
            invitationsHistoryData[position].days_pending.toString()

    }

    fun removeItem(position: Int){
        invitationsHistoryData.removeAt(position)
        notifyDataSetChanged()
    }


    fun getInvitationList()=invitationsHistoryData


}