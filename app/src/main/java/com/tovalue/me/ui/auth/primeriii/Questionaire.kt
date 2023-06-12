package com.tovalue.me.ui.auth.primeriii


import com.google.gson.annotations.SerializedName

data class Questionaire(
    @SerializedName("category_id")
    val categoryId: Int,
    @SerializedName("topic_id")
    val topicId: Int,
    @SerializedName("topic")
    val topic: String,
    @SerializedName("label")
    val label: String,
    @SerializedName("example")
    val example: String,
    @SerializedName("field_name")
    val fieldName: String,
    @SerializedName("max")
    val max: Int,
    @SerializedName("min")
    val min: Int,
    @SerializedName("question")
    val question: String,
    @SerializedName("question_id")
    val questionId: Int,
    @SerializedName("question_key")
    val questionKey: String,
    
) {
    var isProgressValueEnabled: Boolean = false
    var answerValues: Int = 0
}