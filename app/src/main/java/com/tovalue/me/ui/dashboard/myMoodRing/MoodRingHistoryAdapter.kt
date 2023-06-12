package com.tovalue.me.ui.dashboard.myMoodRing

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.slider.Slider
import com.tovalue.me.R
import com.tovalue.me.databinding.ItemQuestionaireBinding
import com.tovalue.me.databinding.ListItemMoodRingHistoryBinding
import com.tovalue.me.model.moodRingModels.MoodRingHistory

class MoodRingHistoryAdapter constructor(
	private val itemViewCallBack: (MoodRingHistory) -> Unit
) : RecyclerView.Adapter<MoodRingHistoryAdapter.ViewHolder>() {
	private val historyList = mutableListOf<MoodRingHistory>()
	
	fun submitData(history: List<MoodRingHistory>) {
		historyList.clear()
		historyList.addAll(history)
		notifyDataSetChanged()
	}
	
	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
		return ViewHolder(
			ListItemMoodRingHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
		)
	}
	
	override fun onBindViewHolder(holder: ViewHolder, position: Int) {
		val item = historyList[position]
		holder.bind(item)
	}
	
	override fun getItemCount(): Int = historyList.size
	
	inner class ViewHolder(val binding: ListItemMoodRingHistoryBinding) : RecyclerView.ViewHolder(binding.root) {
		fun bind(item: MoodRingHistory) {
			binding.date.text = item.date
			binding.mood.text = item.summary
			Glide.with(binding.root.context).load(item.icon)
				.error(R.drawable.homebtn)
				.into(binding.image)
			binding.btnView.setOnClickListener {
				itemViewCallBack(item)
			}
		}
	}
}