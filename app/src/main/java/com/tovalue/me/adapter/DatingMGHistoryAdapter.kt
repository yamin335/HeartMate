package com.tovalue.me.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.tovalue.me.R
import com.tovalue.me.databinding.ItemMeetGreetHistoryBinding
import com.tovalue.me.databinding.ItemWhatWorksBinding
import com.tovalue.me.model.datingJourney.Response
import com.tovalue.me.model.whatworksguide.CardData
import com.tovalue.me.ui.datingjourneyjournal.DatingPartnerMeetGreetFragment
import com.tovalue.me.ui.whatworkguide.WorkWordsDataActivity
import com.tovalue.me.util.extensions.replaceFragmentSafely

class DatingMGHistoryAdapter(private val context: Context, private val data: List<Response>?, val btnlistener: BtnClickListener) :
    RecyclerView.Adapter<DatingMGHistoryAdapter.HistoryViewHolder>() {

    companion object {
        var mClickListener: BtnClickListener? = null
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val inflator = LayoutInflater.from(parent.context)

        return HistoryViewHolder(ItemMeetGreetHistoryBinding.inflate(inflator, parent, false))
    }

    override fun getItemCount() = data!!.size

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        mClickListener = btnlistener
        holder.bind(data!![position], position)

    }

    class HistoryViewHolder(val binding: ItemMeetGreetHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Response, position: Int) {
            binding.testimonialDateTv.text = item.partnerTestimonial!!.date
            binding.experienceTypeTv.text = item.partnerTestimonial!!.title

            binding.partnerTestimonialTv.text = "${item.partnerTestimonial!!.firstName}'s Testimonial"
            binding.myTestimonialTv.text = "My Testimonial"

            binding.myTestimonialTv.setOnClickListener{
                mClickListener?.onBtnClick(item.myTestmonial!!.testimonialId!!)
            }

            binding.partnerTestimonialTv.setOnClickListener{

                mClickListener?.onBtnClick(item.partnerTestimonial!!.testimonialId!!)
            }
        }
    }

    open interface BtnClickListener {
        fun onBtnClick(testimonialID: Int)
    }

}