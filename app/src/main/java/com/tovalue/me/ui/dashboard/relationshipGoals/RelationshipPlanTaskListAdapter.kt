package com.tovalue.me.ui.dashboard.relationshipGoals

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tovalue.me.R
import com.tovalue.me.databinding.ListItemRelationshipTaskBinding
import com.tovalue.me.model.MyRelationshipPlanTask

class RelationshipPlanTaskListAdapter constructor(
	private var taskList: List<MyRelationshipPlanTask>
) : RecyclerView.Adapter<RelationshipPlanTaskListAdapter.ViewHolder>() {
	
	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
		return ViewHolder(
			ListItemRelationshipTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
		)
	}
	
	override fun onBindViewHolder(holder: ViewHolder, position: Int) {
		holder.bind(position)
	}
	
	override fun getItemCount(): Int = taskList.size
	
	inner class ViewHolder(val binding: ListItemRelationshipTaskBinding) : RecyclerView.ViewHolder(binding.root) {
		fun bind(position: Int) {
			val item = taskList[position]
			binding.task.text = item.task
			if (item.isChecked) {
				binding.checkBox.setImageResource(R.drawable.ic_sharp_check_box_24)
			} else {
				binding.checkBox.setImageResource(R.drawable.ic_sharp_check_box_outline_blank_24)
			}

			binding.checkBox.setOnClickListener {
				taskList[position].isChecked = !taskList[position].isChecked
				notifyItemChanged(position)
			}
		}
	}
}