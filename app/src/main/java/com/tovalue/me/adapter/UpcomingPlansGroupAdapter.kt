package com.tovalue.me.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tovalue.me.databinding.GroupUpcomingPlansRvItemBinding
import com.tovalue.me.model.upcomingplans.Groups


class UpcomingPlansGroupAdapter(
    val context: Context
) :
    RecyclerView.Adapter<UpcomingPlansGroupAdapter.UpcomingPlansGroupAdapterViewHolder>() {


    private var upcomingPlanGroupList = ArrayList<Groups>()

    fun setData(upcomingPlanGroupList: ArrayList<Groups>) {
        this.upcomingPlanGroupList.clear()
        this.upcomingPlanGroupList.addAll(upcomingPlanGroupList)
        notifyItemRangeInserted(0,this.upcomingPlanGroupList.size)
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): UpcomingPlansGroupAdapterViewHolder {
        return UpcomingPlansGroupAdapterViewHolder(
            GroupUpcomingPlansRvItemBinding.inflate(
                LayoutInflater.from(
                    context
                ), parent, false
            )
        )
    }


    override fun getItemCount(): Int = upcomingPlanGroupList.size


    inner class UpcomingPlansGroupAdapterViewHolder constructor(val vb: GroupUpcomingPlansRvItemBinding) :
        RecyclerView.ViewHolder(vb.root)

    override fun onBindViewHolder(holder: UpcomingPlansGroupAdapterViewHolder, position: Int) {
        setUpViewForItemView(holder, position)
    }


    private fun setUpViewForItemView(
        upcomingPlansAdapterViewHolder: UpcomingPlansGroupAdapterViewHolder,
        position: Int
    ) {
        upcomingPlansAdapterViewHolder.vb.cbParentView.setBackgroundColor(
            Color.parseColor(upcomingPlanGroupList[position].colorCode!!)
        )
        upcomingPlansAdapterViewHolder.vb.groupCheckbox.text =
            upcomingPlanGroupList[position].firstName

        upcomingPlansAdapterViewHolder.vb.groupCheckbox.isChecked =
            upcomingPlanGroupList[position].isChecked

        upcomingPlansAdapterViewHolder.vb.groupCheckbox.setOnCheckedChangeListener { compoundButton, b ->
            upcomingPlanGroupList[position].isChecked = b
        }
    }

    fun getListData() = upcomingPlanGroupList
}