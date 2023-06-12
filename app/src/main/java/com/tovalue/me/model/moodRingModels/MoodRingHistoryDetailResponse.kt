package com.tovalue.me.model.moodRingModels

data class MoodRingHistoryDetailResponse(val status: String?, val success: Boolean?,
                                         val entry: MoodRingHistoryDetail?, val error: String?)

data class MoodRingHistoryDetail(val spiritual: Int?, val emotional: Int?, val mental: Int?,
                                val timestamp: String?, val communal: Int?, val physical: Int?,
                                val professional: Int?, val spiritual_explanation: String?,
                                val professional_explanation: String?, val emotional_explanation: String?,
                                val mental_explanation: String?, val physical_explanation: String?,
                                val communal_explanation: String?, val id: Int?, val first_name: String?,
                                 val summary: String?, val date_heading: String?)