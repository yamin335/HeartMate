package com.tovalue.me.model.notification

import com.tovalue.me.enums.NotificationComponentTypes
data class NotificationListResponse(
    val status:String,
    val error:String,
    val component_types :HashMap<String,String>,
    val notifications:List<NotificationModel>?,
    val next_page:Int?
) {
}