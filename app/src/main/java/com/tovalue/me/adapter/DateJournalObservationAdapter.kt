package com.tovalue.me.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.tovalue.me.databinding.ObservationsRvItemBinding
import com.tovalue.me.databinding.SubObservationRvItemBinding

class DateJournalObservationAdapter(val context: Context) :
    RecyclerView.Adapter<DateJournalObservationAdapter.DateJournalObservationViewHolder>() {

    private var currentSelected: Int = -1

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DateJournalObservationViewHolder {
        return DateJournalObservationViewHolder(
            ObservationsRvItemBinding.inflate(
                LayoutInflater.from(
                    context
                ), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: DateJournalObservationViewHolder,  position: Int) {

        if (currentSelected == position) {

            holder.vb.subObservationRv.visibility = View.VISIBLE
            holder.vb.subObservationRv.apply {

                adapter = SubObservationRVAdapter()
                hasFixedSize()
            }

        } else {
            holder.vb.subObservationRv.visibility = View.GONE
            holder.vb.subObservationRv.adapter = null
        }

        holder.vb.root.setOnClickListener {


            if(holder.vb.subObservationRv.isVisible){
                currentSelected = -1
            }else{
                currentSelected = holder.absoluteAdapterPosition
            }

            notifyDataSetChanged()
        }


    }

    override fun getItemCount(): Int = 10

    inner class DateJournalObservationViewHolder constructor(val vb: ObservationsRvItemBinding) :
        RecyclerView.ViewHolder(vb.root)


    /* Sub Recyclerview */

    inner class SubObservationRVAdapter() :
        RecyclerView.Adapter<SubObservationRVAdapter.SubObservationViewHolder>() {

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): SubObservationViewHolder {
            return SubObservationViewHolder(
                SubObservationRvItemBinding.inflate(
                    LayoutInflater.from(
                        context
                    ), parent, false
                )
            )
        }

        override fun onBindViewHolder(holder: SubObservationViewHolder, position: Int) {

        }

        override fun getItemCount(): Int = 3

        inner class SubObservationViewHolder constructor(val vbb: SubObservationRvItemBinding) :
            RecyclerView.ViewHolder(vbb.root)


    }


}