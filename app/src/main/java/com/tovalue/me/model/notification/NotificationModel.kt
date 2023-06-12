package com.tovalue.me.model.notification

data class NotificationModel(
    val id: Int,
    val user_id: Int,
    val item_id: Int,
    val secondary_item_id: Int,
    val component_name: String,
    val component_action: String,
    val sender_name: String,
    val sender_avatar: String,
    val date_notified: String,
    val time_since: String,
    val is_new: Int
)
