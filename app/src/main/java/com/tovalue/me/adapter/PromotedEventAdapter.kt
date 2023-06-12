package com.tovalue.me.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tovalue.me.R
import com.tovalue.me.databinding.PromotedEventRvItemBinding
import com.tovalue.me.model.datenightcatalog.HolderData


class PromotedEventAdapter(
    val context: Context,
    val events: List<HolderData.Event>,
    val callback: (Int) -> Unit
) : RecyclerView.Adapter<PromotedEventAdapter.PromotedEventViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PromotedEventViewHolder {
        return PromotedEventViewHolder(
            PromotedEventRvItemBinding.inflate(
                LayoutInflater.from(
                    context
                ), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: PromotedEventViewHolder, position: Int) {

        val event = events[position]

        Glide.with(context).load(event.images[0].url)
            .error(R.drawable.default_avatar)
            .into(holder.vb.promotedBgIV)

        holder.vb.eventTitleTV.setText(event.name)
        holder.vb.venueTV.setText(event.location.name)

        holder.vb.root.setOnClickListener {
            callback.invoke(position)
        }

    }

    override fun getItemCount(): Int {
        if (events.size >= 5) {
            return 5
        } else {
            return events.size
        }

    }

    inner class PromotedEventViewHolder constructor(val vb: PromotedEventRvItemBinding) :
        RecyclerView.ViewHolder(vb.root)


}