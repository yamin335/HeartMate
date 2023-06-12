package com.tovalue.me.ui.datenightcatalog

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tovalue.me.databinding.ListItemDateNightWeeklyIdeaBinding
import com.tovalue.me.model.datenightcatalog.HolderData


class DateNightIdeaWeekListAdapter constructor(
    private val itemViewCallBack: (HolderData.DateIdea) -> Unit
) : RecyclerView.Adapter<DateNightIdeaWeekListAdapter.ViewHolder>() {

    private val weekList = mutableListOf<HolderData.DateNightCatalog>()
    private lateinit var dateNightIdeListAdapter: DateNightIdeaListAdapter

    fun submitData(history: List<HolderData.DateNightCatalog>) {
        weekList.clear()
        weekList.addAll(history)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ListItemDateNightWeeklyIdeaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = weekList[position]
        holder.bind(item, position)
    }

    override fun getItemCount(): Int = weekList.size

    inner class ViewHolder(val binding: ListItemDateNightWeeklyIdeaBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: HolderData.DateNightCatalog, position: Int) {
            binding.weekNumber.text = item.week_number
            setDateNightIdeaRecyclerView(item.dates)

            binding.blurView.visibility = if (position != 0) View.VISIBLE else View.GONE
            //binding.mood.text = item.summary
            /*Glide.with(binding.root.context).load(item.icon)
                .error(R.drawable.homebtn)
                .into(binding.image)
            binding.btnView.setOnClickListener {
                itemViewCallBack(item)
            }*/
        }

        /* set Views */
        private fun setDateNightIdeaRecyclerView(data: List<HolderData.DateIdea>?) {
            dateNightIdeListAdapter = DateNightIdeaListAdapter {
                //context.startActivity(NavigationActivity.createIntent(requireActivity(), Constants.DATE_NIGHT_CATALOG_IDEAS_DETAILS))
                itemViewCallBack(it)
            }
            data?.let { dateNightIdeListAdapter.submitData(it) }
            binding.recyclerIdea.apply {
                adapter = dateNightIdeListAdapter
                setHasFixedSize(true)
            }
        }
    }
}