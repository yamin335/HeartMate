package com.tovalue.me.ui.dashboard.relationshipGoals

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tovalue.me.R
import com.tovalue.me.databinding.ListItemRelationshipIntervalBinding
import com.tovalue.me.model.MyRelationshipPlanChecklistItem

class RelationshipIntervalListAdapter constructor(
	private var intervalList: List<MyRelationshipPlanChecklistItem>
) : RecyclerView.Adapter<RelationshipIntervalListAdapter.ViewHolder>() {
	
	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
		return ViewHolder(
			ListItemRelationshipIntervalBinding.inflate(LayoutInflater.from(parent.context), parent, false)
		)
	}
	
	override fun onBindViewHolder(holder: ViewHolder, position: Int) {
		holder.bind(position)
	}
	
	override fun getItemCount(): Int = intervalList.size
	
	inner class ViewHolder(val binding: ListItemRelationshipIntervalBinding) : RecyclerView.ViewHolder(binding.root) {
		fun bind(position: Int) {
			val item = intervalList[position]
			binding.interval.text = item.interval

			val planListAdapter = RelationshipPlanTaskListAdapter(intervalList[position].task)

			binding.recyclerTask.apply {
				adapter = planListAdapter
				setHasFixedSize(true)
			}
		}
	}
}