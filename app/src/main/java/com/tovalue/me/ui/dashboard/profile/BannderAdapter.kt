package com.tovalue.me.ui.dashboard.profile

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tovalue.me.databinding.ItemTopBannerBinding
import com.tovalue.me.model.Panel
import com.tovalue.me.ui.whatworkguide.WhatWorksGuideActivity

class BannerAdapter(context: Context, bannerData: List<Panel>) :
	RecyclerView.Adapter<BannerAdapter.BannerViewHolder>() {
	private val data: List<Panel> = bannerData
	private val context: Context = context
	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BannerViewHolder {
		return BannerViewHolder(
			ItemTopBannerBinding.inflate(
				LayoutInflater.from(parent.context), parent, false)
		)
	}

	override fun getItemCount() = data.size

	override fun onBindViewHolder(holder: BannerViewHolder, position: Int) {
		holder.bind(context,data[position])
	}

	class BannerViewHolder(private val binding: ItemTopBannerBinding) : RecyclerView.ViewHolder(binding.root) {
		fun bind(context: Context,panel: Panel) {
			Glide.with(binding.root.context).load(panel.icon).into(binding.bannerImg)
			binding.bannerTitleTv.text = panel.title
			binding.bannerDetailTv.text = panel.description

			binding.checkoutBtn.setOnClickListener {
				context.startActivity(Intent(context, WhatWorksGuideActivity::class.java))

			}
		}

	}
}
