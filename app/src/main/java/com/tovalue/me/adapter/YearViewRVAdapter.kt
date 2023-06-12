package com.tovalue.me.adapter

import android.content.Context
import android.graphics.Typeface
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.tovalue.me.R
import com.tovalue.me.databinding.MonthListRvItemBinding

class YearViewRVAdapter(val context: Context, val yearList: Array<String>) :
    RecyclerView.Adapter<YearViewRVAdapter.MonthHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MonthHolder {
        return MonthHolder(
            MonthListRvItemBinding.inflate(
                LayoutInflater.from(context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MonthHolder, position: Int) {

        val month = yearList.get(position)

        holder.vb.monthTV.text = month


    }

    override fun getItemCount(): Int = yearList.size

    inner class MonthHolder constructor(val vb: MonthListRvItemBinding) :
        RecyclerView.ViewHolder(vb.root) {

        @RequiresApi(Build.VERSION_CODES.O)
        fun changeFocusImage() {

            vb.monthTV.typeface = context.resources.getFont(R.font.inter_bold)
          vb.calIV.setImageDrawable(context.getDrawable(R.drawable.cal))

        }
        @RequiresApi(Build.VERSION_CODES.O)
        fun changeImageUnFocus(){
            vb.monthTV.typeface = context.resources.getFont(R.font.inter_medium)
            vb.calIV.setImageDrawable(context.getDrawable(R.drawable.cal_light_black))
        }

    }

}