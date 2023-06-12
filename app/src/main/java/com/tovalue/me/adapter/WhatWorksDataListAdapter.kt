package com.tovalue.me.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tovalue.me.databinding.ItemWhatWorksDetailBinding
import com.tovalue.me.model.whatworksguide.WhatWorksGuideDataRespose

class WhatWorksDataListAdapter(private val context: Context, private val data: MutableList<WhatWorksGuideDataRespose.TipData>) :
    RecyclerView.Adapter<WhatWorksDataListAdapter.WhatWorksHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WhatWorksHolder {
        val inflator = LayoutInflater.from(parent.context)

        return WhatWorksHolder(ItemWhatWorksDetailBinding.inflate(inflator, parent, false))

    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: WhatWorksHolder, position: Int) {

        holder.bind(data[position], position)

    }

    class WhatWorksHolder(private val binding: ItemWhatWorksDetailBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: WhatWorksGuideDataRespose.TipData, position: Int) {
            binding.tvTip.text = item.tipTitle
            binding.tvHeader.text = item.title
            binding.tvExcerpt.text = item.description
            Glide.with(binding.root.context).load(item.image).into(binding.ivHeaderImage)
        }
    }

}