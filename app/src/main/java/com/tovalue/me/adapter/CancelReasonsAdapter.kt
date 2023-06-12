package com.tovalue.me.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tovalue.me.databinding.CancelReasonRvItemBinding
import com.tovalue.me.ui.dashboard.upcomingplans.models.CancelReason


class CancelReasonsAdapter(
    val context: Context,
    val callBack: (Long) -> Unit
) :
    RecyclerView.Adapter<CancelReasonsAdapter.CancelReasonViewHolder>() {


    private var cancelReasonList = listOf<CancelReason>()

    fun setData(cancelReasonList: List<CancelReason>) {
        this.cancelReasonList = cancelReasonList
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CancelReasonViewHolder {
        return CancelReasonViewHolder(
            CancelReasonRvItemBinding.inflate(
                LayoutInflater.from(
                    context
                ), parent, false
            )
        )
    }


    override fun getItemCount(): Int = cancelReasonList.size


    inner class CancelReasonViewHolder constructor(val vb: CancelReasonRvItemBinding) :
        RecyclerView.ViewHolder(vb.root)

    override fun onBindViewHolder(holder: CancelReasonViewHolder, position: Int) {
        setUpViewForItemView(holder, position)
    }


    private fun setUpViewForItemView(
        viewHolder: CancelReasonViewHolder,
        position: Int
    ) {

        viewHolder.vb.cancelReason.text =
            cancelReasonList[position].reasonText

        viewHolder.itemView.setOnClickListener {
            callBack.invoke(cancelReasonList[position].id)
        }


    }

}