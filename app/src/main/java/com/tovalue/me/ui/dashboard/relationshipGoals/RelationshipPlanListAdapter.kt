package com.tovalue.me.ui.dashboard.relationshipGoals

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tovalue.me.databinding.ListItemRelationshipPlanBinding
import com.tovalue.me.model.RelationshipPlan

class RelationshipPlanListAdapter constructor(
	private val callBack: (RelationshipPlan) -> Unit,
) : RecyclerView.Adapter<RelationshipPlanListAdapter.ViewHolder>() {
	private val planList = mutableListOf<RelationshipPlan>()
	
	fun submitData(history: List<RelationshipPlan>) {
		planList.clear()
		planList.addAll(history)
		notifyDataSetChanged()
	}
	
	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
		return ViewHolder(
			ListItemRelationshipPlanBinding.inflate(LayoutInflater.from(parent.context), parent, false)
		)
	}
	
	override fun onBindViewHolder(holder: ViewHolder, position: Int) {
		holder.bind(position)
	}
	
	override fun getItemCount(): Int = planList.size
	
	inner class ViewHolder(val binding: ListItemRelationshipPlanBinding) : RecyclerView.ViewHolder(binding.root) {
		fun bind(position: Int) {
			val item = planList[position]
			binding.planId.text = (position + 1).toString()
			binding.title.text = if (item.plan_title.isNullOrBlank()) "Unknown Plan" else item.plan_title
			binding.description.text = if (item.plan_description.isNullOrBlank()) "No description provided" else item.plan_description
			binding.price.text = "$0"
			binding.lineSeparator3.visibility = if (position == planList.size - 1) View.VISIBLE else View.GONE
		}
	}
}