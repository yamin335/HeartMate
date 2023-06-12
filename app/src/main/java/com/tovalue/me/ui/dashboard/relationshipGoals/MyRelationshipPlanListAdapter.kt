package com.tovalue.me.ui.dashboard.relationshipGoals

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tovalue.me.databinding.ListItemMyRelationshipPlanBinding
import com.tovalue.me.model.MyRelationshipPlanResponseData

class MyRelationshipPlanListAdapter constructor(
	private val assignCallBack: (MyRelationshipPlanResponseData) -> Unit,
	private val deadlineCallBack: (MyRelationshipPlanResponseData) -> Unit
) : RecyclerView.Adapter<MyRelationshipPlanListAdapter.ViewHolder>() {
	private val planList = mutableListOf<MyRelationshipPlanResponseData>()
	
	fun submitData(history: List<MyRelationshipPlanResponseData>) {
		planList.clear()
		planList.addAll(history)
		notifyDataSetChanged()
	}
	
	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
		return ViewHolder(
			ListItemMyRelationshipPlanBinding.inflate(LayoutInflater.from(parent.context), parent, false)
		)
	}
	
	override fun onBindViewHolder(holder: ViewHolder, position: Int) {
		holder.bind(position)
	}
	
	override fun getItemCount(): Int = planList.size
	
	inner class ViewHolder(val binding: ListItemMyRelationshipPlanBinding) : RecyclerView.ViewHolder(binding.root) {
		fun bind(position: Int) {
			val item = planList[position]
			binding.planId.text = (position + 1).toString()
			binding.title.text = item.plan_title
			binding.description.text = item.percentage_complete
			binding.assignTo.text = if (item.partner_name.isNullOrBlank()) "No One Yet" else item.partner_name
			binding.deadline.text = if (item.deadline_date_setting.isNullOrBlank()) "No Deadline Set" else item.deadline_date_setting
			binding.linearAssignTo.setOnClickListener {
				assignCallBack(item)
			}
			binding.linearDeadline.setOnClickListener {
				deadlineCallBack(item)
			}
			binding.lineSeparator3.visibility = if (position == planList.size - 1) View.VISIBLE else View.GONE
		}
	}
}