package com.tovalue.me.model.moodRingModels

data class MoodRingHistoryResponse(val status: String?, val entries: List<MoodRingHistory>?,
                                   val page_number: Int?, val page_count: Int?, val error: String?)

data class MoodRingHistory(val id: Int?, val date: String?, val icon: String?, val summary: String?)