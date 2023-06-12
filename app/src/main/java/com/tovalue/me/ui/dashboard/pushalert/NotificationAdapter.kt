package com.tovalue.me.ui.dashboard.pushalert

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tovalue.me.databinding.ItemNotificationBinding
import com.tovalue.me.model.notification.NotificationModel
import com.tovalue.me.util.extensions.loadImage

class NotificationAdapter(val itemClickListener: (notificationModel: NotificationModel) -> Unit) :
    RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>() {

    var notificationList: MutableList<NotificationModel> = ArrayList()
//    private lateinit var itemClickListener: (notificationModel: NotificationModell) -> Unit

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val binding =
            ItemNotificationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NotificationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        holder.bind(notificationList[position])
    }

    override fun getItemCount(): Int = notificationList.size

    fun updateList(newList: List<NotificationModel>?, notificationType: Boolean?) {
        if (notificationType!!) {
            notificationList.clear()
            notificationList = (newList ?: emptyList()).toMutableList()

            notifyDataSetChanged()
        } else {
            notificationList.addAll(newList ?: emptyList())
        }


    }

    inner class NotificationViewHolder(private val binding: ItemNotificationBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: NotificationModel) {
            binding.apply {
                imageViewSender.loadImage(item.sender_avatar)
                textViewName.text = item.sender_name
                textViewDesc.text = item.component_action
                notificationDateTv.text = item.time_since
            }

            binding.root.setOnClickListener {
                itemClickListener.invoke(item)
            }
        }
    }
}