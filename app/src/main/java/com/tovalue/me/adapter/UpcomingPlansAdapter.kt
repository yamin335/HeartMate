package com.tovalue.me.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.ExpandableListView
import com.bumptech.glide.Glide
import com.tovalue.me.R
import com.tovalue.me.databinding.HeaderUpcomingPlansRvItemBinding
import com.tovalue.me.databinding.UpcomingPlansRvItemBinding
import com.tovalue.me.model.upcomingplans.Partners
import com.tovalue.me.model.upcomingplans.Plans


class UpcomingPlansAdapter internal constructor(
    private val context: Context
) : BaseExpandableListAdapter() {

    private var planDetailList = HashMap<String?, List<Partners>>()
    private var groupTitleList = arrayListOf<String?>()


    fun setData(planDetailList: Map<String?, List<Partners>>, groupTitleList: List<String?>) {
        this.planDetailList.clear()
        this.planDetailList.putAll(planDetailList)
        this.groupTitleList.clear()
        this.groupTitleList.addAll(groupTitleList)
        notifyDataSetChanged()
    }

    override fun getChild(listPosition: Int, expandedListPosition: Int): Plans {
        return planDetailList[groupTitleList[listPosition]]?.get(expandedListPosition)?.plans?.firstOrNull() ?: Plans()
    }

    override fun getChildId(listPosition: Int, expandedListPosition: Int): Long {
        return expandedListPosition.toLong()
    }

    override fun getChildView(
        listPosition: Int,
        expandedListPosition: Int,
        isLastChild: Boolean,
        convertView: View?,
        parent: ViewGroup
    ): View {
        var convertView: UpcomingPlansRvItemBinding? = null
        val expandedList = getChild(listPosition, expandedListPosition)
        if (convertView == null) {
            convertView = UpcomingPlansRvItemBinding.inflate(
                LayoutInflater.from(
                    context
                ), parent, false
            )
        }
        convertView.timeTv.text = expandedList.startTime
        convertView.titleTv.text = expandedList.excerpt

        Glide.with(context).load(expandedList.avatar)
            .error(R.drawable.default_avatar)
            .into(convertView.profileImg)

        if (expandedList?.colorCode != null)
            convertView.titleTv.setTextColor(Color.parseColor(expandedList.colorCode!!))


        return convertView.root
    }


    override fun getChildrenCount(listPosition: Int): Int {
        return planDetailList[groupTitleList[listPosition]]?.size ?: 0
    }

    override fun getGroup(listPosition: Int): String? {
        return groupTitleList[listPosition]
    }

    override fun getGroupCount(): Int {
        return groupTitleList.size
    }

    override fun getGroupId(listPosition: Int): Long {
        return listPosition.toLong()
    }

    override fun getGroupView(
        listPosition: Int,
        isExpanded: Boolean,
        convertView: View?,
        parent: ViewGroup
    ): View {
        var convertView: HeaderUpcomingPlansRvItemBinding? = null
        val listTitle = getGroup(listPosition) as String
        if (convertView == null) {
            convertView = HeaderUpcomingPlansRvItemBinding.inflate(
                LayoutInflater.from(
                    context
                ), parent, false
            )
        }

        val listView = parent as ExpandableListView
        listView.expandGroup(listPosition)
        convertView.headerTv.text = listTitle

        convertView.root.setOnClickListener(null)

        return convertView.root
    }

    override fun hasStableIds(): Boolean {
        return false
    }

    override fun isChildSelectable(listPosition: Int, expandedListPosition: Int): Boolean {
        return true
    }


}
