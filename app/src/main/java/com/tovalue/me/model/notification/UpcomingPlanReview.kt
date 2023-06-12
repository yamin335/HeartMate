package com.tovalue.me.model.notification

data class UpcomingPlanReview(
    val status: String,
    val error: String,
    val event_id: Int,
    val title: String,
    val group_id: String,
    val author_id: Int,
    val event_start_date: String
)