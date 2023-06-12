package com.tovalue.me.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.tovalue.me.databinding.DatingJournalDateRvItemBinding


class DatingJournalDateAdapter constructor(val context:Context):RecyclerView.Adapter<DatingJournalDateAdapter.DatingJournalDateViewHolder>() {

    val size:Int = 5

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DatingJournalDateViewHolder {
        return DatingJournalDateViewHolder(DatingJournalDateRvItemBinding.inflate(LayoutInflater.from(context),parent,false))
    }

    override fun onBindViewHolder(holder: DatingJournalDateViewHolder, position: Int) {

        setIndicator(holder,position)

    }

    private fun setIndicator(holder: DatingJournalDateViewHolder, position: Int) {

        holder.vb.dotedIndicatorV.isVisible = position==size-1

        if(position==0){
            holder.vb.strick.isVisible = false

        }else{


            holder.vb.strick.isVisible = true

        }

    }

    override fun getItemCount(): Int =size

    inner  class  DatingJournalDateViewHolder constructor(val vb:DatingJournalDateRvItemBinding):RecyclerView.ViewHolder(vb.root)

}