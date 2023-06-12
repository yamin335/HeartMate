package com.tovalue.me.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tovalue.me.databinding.GroupUpcomingPlansRvItemBinding
import com.tovalue.me.model.datingJourney.Groups


class DatingOurCalendarGroupAdapter(
    val context: Context
) :
    RecyclerView.Adapter<DatingOurCalendarGroupAdapter.DatingOurCalendarGroupAdapterViewHolder>() {

    private var datingOurCalendarGroupList = ArrayList<Groups>()

    fun setData(upcomingPlanGroupList: ArrayList<Groups>) {
        this.datingOurCalendarGroupList.clear()
        this.datingOurCalendarGroupList.addAll(upcomingPlanGroupList)
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DatingOurCalendarGroupAdapterViewHolder {
        return DatingOurCalendarGroupAdapterViewHolder(
            GroupUpcomingPlansRvItemBinding.inflate(
                LayoutInflater.from(
                    context
                ), parent, false
            )
        )
    }

    override fun getItemCount(): Int = datingOurCalendarGroupList.size

    inner class DatingOurCalendarGroupAdapterViewHolder constructor(val vb: GroupUpcomingPlansRvItemBinding) :
        RecyclerView.ViewHolder(vb.root)

    override fun onBindViewHolder(holder: DatingOurCalendarGroupAdapterViewHolder, position: Int) {
        setUpViewForItemView(holder, position)
    }

    private fun setUpViewForItemView(upcomingPlansAdapterViewHolder: DatingOurCalendarGroupAdapterViewHolder, position: Int) {

        upcomingPlansAdapterViewHolder.vb.groupCheckbox.setBackgroundColor(
            Color.parseColor(datingOurCalendarGroupList[position].colorCode!!)
        )
        upcomingPlansAdapterViewHolder.vb.groupCheckbox.text =
            datingOurCalendarGroupList[position].firstName

        upcomingPlansAdapterViewHolder.vb.groupCheckbox.isChecked= datingOurCalendarGroupList[position].isChecked

        upcomingPlansAdapterViewHolder.vb.groupCheckbox.setOnCheckedChangeListener { compoundButton, b ->
            datingOurCalendarGroupList[position].isChecked = b
        }
    }

    fun getListData() = datingOurCalendarGroupList
}