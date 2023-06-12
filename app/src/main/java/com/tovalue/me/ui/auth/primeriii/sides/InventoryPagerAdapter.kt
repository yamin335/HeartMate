package com.tovalue.me.ui.auth.primeriii.sides

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tovalue.me.databinding.ItemInventoryCategoryBinding
import com.tovalue.me.databinding.ItemSidesBinding
import com.tovalue.me.ui.auth.primeriii.SideCategory
import com.tovalue.me.ui.auth.primeriii.sides.LifeInventoryFragment.Companion.VIEW_TYPE_OPTION_SIDES

class InventoryPagerAdapter(
	private val type: String? = "",
	private val data: List<SideCategory>,
	val itemClickListner: (Int) -> Unit
) :
	RecyclerView.Adapter<RecyclerView.ViewHolder>() {
	
	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
		val inflator = LayoutInflater.from(parent.context)
		return if (type == VIEW_TYPE_OPTION_SIDES) {
			InventoryViewHolder(ItemSidesBinding.inflate(inflator, parent, false))
		} else {
			CategoryVieHolder(ItemInventoryCategoryBinding.inflate(inflator, parent, false))
		}
	}
	
	override fun getItemCount() = data.size
	
	override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
		if (holder is InventoryViewHolder) {
			holder.bind(data[position], position)
			holder.itemView.setOnClickListener{}
		}
		else if (holder is CategoryVieHolder) {
			holder.bind(data[position])
			holder.itemView.setOnClickListener{
				itemClickListner(position)
			}
		}
	}
	
	
	class InventoryViewHolder(private val binding: ItemSidesBinding) : RecyclerView.ViewHolder(binding.root) {
		fun bind(item: SideCategory, position: Int) {
			binding.titleTv.text = item.category
			binding.sideCountTv.text = (position + 1).toString().plus("of 8 sides")
//			Glide.with(binding.root.context).load(item.coverImage).into(binding.sideImg)
		}
	}
	
	class CategoryVieHolder(private val binding: ItemInventoryCategoryBinding): RecyclerView.ViewHolder(binding.root) {
		fun bind(category: SideCategory) {
			binding.categoryTitleTv.text = category.category
		}
		
	}
}