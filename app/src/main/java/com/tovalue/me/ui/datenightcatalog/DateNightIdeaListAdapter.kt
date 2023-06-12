package com.tovalue.me.ui.datenightcatalog

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tovalue.me.R
import com.tovalue.me.databinding.ListItemDateNightIdeaBinding
import com.tovalue.me.model.datenightcatalog.HolderData


class DateNightIdeaListAdapter constructor(
    private val itemViewCallBack: (HolderData.DateIdea) -> Unit
) : RecyclerView.Adapter<DateNightIdeaListAdapter.ViewHolder>() {

    private val dateNights = mutableListOf<HolderData.DateIdea>()

    fun submitData(history: List<HolderData.DateIdea>) {
        dateNights.clear()
        dateNights.addAll(history)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ListItemDateNightIdeaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = dateNights[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = dateNights.size

    inner class ViewHolder(val binding: ListItemDateNightIdeaBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: HolderData.DateIdea) {
            binding.title.text = item.title
            binding.experience.text = item.experience.toString()
            binding.aspects.text = "${item.total_aspects} aspects of me"


            Glide.with(binding.root.context)
                .load(item.image_url)
                .error(R.drawable.date_night_catalog_cover)
                .into(binding.image)

            binding.btnGuideMe.setOnClickListener {
                itemViewCallBack(item)
            }
        }
    }
}