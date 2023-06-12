package com.tovalue.me.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tovalue.me.databinding.ItemInventoryCategoryBinding
import com.tovalue.me.databinding.ItemSidesBinding
import com.tovalue.me.databinding.ItemWhatWorksBinding
import com.tovalue.me.model.whatworksguide.CardData
import com.tovalue.me.model.whatworksguide.WhatWorksGuideRespose
import com.tovalue.me.ui.auth.primeriii.SideCategory
import com.tovalue.me.ui.auth.primeriii.sides.LifeInventoryFragment.Companion.VIEW_TYPE_OPTION_SIDES
import com.tovalue.me.ui.whatworkguide.WhatWorksGuideActivity
import com.tovalue.me.ui.whatworkguide.WorkWordsDataActivity

class WhatWorksListAdapter(private val context: Context, private val data: MutableList<CardData>) :
    RecyclerView.Adapter<WhatWorksListAdapter.WhatWorksHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WhatWorksHolder {
        val inflator = LayoutInflater.from(parent.context)

        return WhatWorksHolder(ItemWhatWorksBinding.inflate(inflator, parent, false))

    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: WhatWorksHolder, position: Int) {

        holder.bind(data[position], position)
        holder.itemView.setOnClickListener {
            context.startActivity(
                Intent(context, WorkWordsDataActivity::class.java)
                    .putExtra("guideID", data[position].guideId)
            )
        }
    }


    class WhatWorksHolder(private val binding: ItemWhatWorksBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: CardData, position: Int) {
            binding.tvHeader.text = item.headerTitle
            binding.tvExcerpt.text = item.excerpt
            Glide.with(binding.root.context).load(item.headerImage).into(binding.ivHeaderImage)
        }
    }

}