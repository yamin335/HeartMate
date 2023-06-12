package com.tovalue.me.ui.datenightcatalog

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tovalue.me.R
import com.tovalue.me.databinding.ItemDateNightCatalogHistoryBinding
import com.tovalue.me.databinding.ListItemMoodRingHistoryBinding
import com.tovalue.me.model.moodRingModels.MoodRingHistory
import com.tovalue.me.ui.dashboard.myMoodRing.MoodRingHistoryAdapter

class CatalogIdeaListDetailsHistoryAdapter  constructor(
    private val itemViewCallBack: (MoodRingHistory) -> Unit
) : RecyclerView.Adapter<CatalogIdeaListDetailsHistoryAdapter.ViewHolder>() {
    private val historyList = mutableListOf<MoodRingHistory>()

    fun submitData(history: List<MoodRingHistory>) {
        historyList.clear()
        historyList.addAll(history)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemDateNightCatalogHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = historyList[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = historyList.size

    inner class ViewHolder(val binding: ItemDateNightCatalogHistoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: MoodRingHistory) {
            binding.date.text = item.date
            binding.mood.text = item.summary
            Glide.with(binding.root.context)
                //.load(item.icon)
                .load(R.drawable.notificationdummy)
                .error(R.drawable.notificationdummy)
                .into(binding.image)
            binding.btnView.setOnClickListener {
                itemViewCallBack(item)
            }
        }
    }
}