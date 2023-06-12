package com.tovalue.me.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.tovalue.me.R
import com.tovalue.me.databinding.DatenightCatalogWeekRvItemBinding
import com.tovalue.me.databinding.PremiumExperiencesRvLayoutBinding
import com.tovalue.me.databinding.WeekCardRvItemBinding

import com.tovalue.me.model.datenightcatalog.HolderData


enum class DataNightWeekConst {
    WEEK_TAP,
    PROMOTED_TAP,
    PROMOTED_SEE_MORE_TAP
}

enum class ViewType {
    PREMIUM_EXPERIENCE,
    WEEK_VIEW
}

class DateNightCatalogWeeksAdapter(
    val context: Context,
    val callback: (DataNightWeekConst, String?, String?, Int?, Int?) -> Unit
) :
    RecyclerView.Adapter<DateNightCatalogWeeksAdapter.DataNightCatalogViewHolder>() {

    private var days: MutableList<HolderData> = mutableListOf()

    fun addPremiumPromoted(data: HolderData) {
        if (days.size >= 1) {

            days.add(1, data)
            notifyDataSetChanged()

        }else if(days.isEmpty()){
            days.add(data)
            notifyDataSetChanged()
        }
    }

    fun getWholeList(): MutableList<HolderData> {
        return days
    }

    fun setWeeksData(data: List<HolderData>){
        this.days.clear()
        this.days.addAll(data)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataNightCatalogViewHolder {
        return when (viewType) {
            R.layout.datenight_catalog_week_rv_item -> {
                DataNightCatalogViewHolder.DateNightCatalogWeeksViewHolder(
                    DatenightCatalogWeekRvItemBinding.inflate(
                        LayoutInflater.from(context),
                        parent,
                        false
                    )
                )
            }
            R.layout.premium_experiences_rv_layout -> {
                DataNightCatalogViewHolder.PremiumPromotedDataNightViewHolder(
                    PremiumExperiencesRvLayoutBinding.inflate(
                        LayoutInflater.from(context),
                        parent,
                        false
                    )
                )
            }

            else -> throw IllegalThreadStateException("View Not Found")
        }
    }

    override fun onBindViewHolder(holder: DataNightCatalogViewHolder, position: Int) {

        val holderData = days[position]
        when (holder) {
            is DataNightCatalogViewHolder.DateNightCatalogWeeksViewHolder -> {
                holder.bind(holderData as HolderData.DateNightCatalog, callback)
            }
            is DataNightCatalogViewHolder.PremiumPromotedDataNightViewHolder -> {
                holder.bind(holderData as HolderData.PromotedEventResponse, callback)
            }


        }

    }


    //    override fun onBindViewHolder(holder: DateNightCatalogWeeksViewHolder, position: Int) {
//
//        val week = days[position] as HolderData.DateNightCatalog

////        if (position == 0) {
////            setAllPromotedEvent(holder)
//
////            holder.vb.PromotedLL.visibility = View.VISIBLE
////        } else {
////            holder.vb.PromotedLL.visibility = View.GONE
////
////        }
//    }

//    private fun setAllPromotedEvent(holder: DateNightCatalogWeeksViewHolder) {
//

//
//    }

    override fun getItemViewType(position: Int): Int {
        return when (days[position]) {
            is HolderData.PromotedEventResponse -> {
                return R.layout.premium_experiences_rv_layout
            }
            else -> {
                return R.layout.datenight_catalog_week_rv_item
            }
        }

    }

    override fun getItemCount(): Int = days.size


    sealed class DataNightCatalogViewHolder(binding: ViewBinding) :
        RecyclerView.ViewHolder(binding.root) {


        /** Date Night Catalog Weeks */
        class DateNightCatalogWeeksViewHolder
        constructor(private val vb: DatenightCatalogWeekRvItemBinding) :
            DataNightCatalogViewHolder(vb) {
            fun bind(
                eventsResponse: HolderData.DateNightCatalog,
                callback: (DataNightWeekConst, String?, String?, Int?, Int?) -> Unit
            ) {
                vb.weekTitle.text = eventsResponse.week_number

                vb.weeksRV.apply {
                    adapter = DateNightCatalogIdeasWeekAdapter(
                        context,
                        eventsResponse.dates as ArrayList<HolderData.DateIdea>,
                        eventsResponse.week_number.split(" ")[1].toInt(), callback
                    )
                }
            }
        }


        /** Premium Promoted Date */
        class PremiumPromotedDataNightViewHolder constructor(private val vb: PremiumExperiencesRvLayoutBinding) :
            DataNightCatalogViewHolder(vb) {

            fun bind(
                dataNightCatalog: HolderData.PromotedEventResponse,
                callback: (DataNightWeekConst, String?, String?, Int?, Int?) -> Unit
            ) {


                vb.allPromotedRv.apply {
                    adapter = PromotedEventAdapter(context, dataNightCatalog.events) { postion ->
                        callback.invoke(DataNightWeekConst.PROMOTED_TAP, null, null, postion, null)
                    }
                    layoutManager =
                        LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                }

                vb.promotedSeeMore.setOnClickListener {
                    callback.invoke(DataNightWeekConst.PROMOTED_SEE_MORE_TAP, null, null,absoluteAdapterPosition, null)
                }

            }

        }


    }


}


/** -------------------------------------- Single Week Ideas List ------------------------------------------------------------ */
class DateNightCatalogIdeasWeekAdapter(
    val context: Context,
    val datingNightCatalogList: ArrayList<HolderData.DateIdea>,
    val weekNumber: Int,
    val callback: (DataNightWeekConst, String?, String?, Int?, Int?) -> Unit
) :
    RecyclerView.Adapter<DateNightCatalogIdeasWeekAdapter.DateNightCatalogViewHolder>() {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DateNightCatalogViewHolder {
        return DateNightCatalogViewHolder(
            WeekCardRvItemBinding.inflate(
                LayoutInflater.from(context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: DateNightCatalogViewHolder, position: Int) {

        val data = datingNightCatalogList.get(position)

        Glide.with(context).load(data.image_url)
            .error(R.drawable.default_avatar)
            .into(holder.vb.locationImg)

        holder.vb.routeBtn.setOnClickListener {
            callback.invoke(
                DataNightWeekConst.WEEK_TAP,
                data.date_night_id.toString(), data.title, -1, weekNumber
            )
        }
        holder.vb.root.setOnClickListener {
            callback.invoke(
                DataNightWeekConst.WEEK_TAP,
                data.date_night_id.toString(),
                data.title,
                -1,
                weekNumber
            )
        }



        holder.vb.dateTitleTV.text = data.title
        holder.vb.weekDefTV.text = data.activity
        holder.vb.aspectOfMeTV.text =
            context.getString(R.string.aspectsOfMe, data.total_aspects.toString())

    }

    override fun getItemCount(): Int = datingNightCatalogList.size

    inner class DateNightCatalogViewHolder constructor(val vb: WeekCardRvItemBinding) :
        RecyclerView.ViewHolder(vb.root)
}
