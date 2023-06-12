package com.tovalue.me.ui.auth.primeriv

import android.text.Html
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.RecyclerView
import com.tovalue.me.databinding.ItemGuideBinding

class GuideAdapter(private val data: List<String>) :
	RecyclerView.Adapter<GuideAdapter.GuideViewHolder>() {
	
	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GuideViewHolder {
		return GuideViewHolder(
			ItemGuideBinding.inflate(
				LayoutInflater.from(parent.context), parent, false)
			)
	}
	
	override fun getItemCount() = data.size
	
	override fun onBindViewHolder(holder: GuideViewHolder, position: Int) {
		holder.bind(data[position])
	}
	
	
	class GuideViewHolder(private val binding: ItemGuideBinding) : RecyclerView.ViewHolder(binding.root) {
		fun bind(txt: String) {
			binding.guideTv.text = HtmlCompat.fromHtml(txt, HtmlCompat.FROM_HTML_MODE_LEGACY)
		}
		
	}
}