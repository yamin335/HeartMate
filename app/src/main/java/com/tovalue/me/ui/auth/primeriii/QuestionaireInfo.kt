package com.tovalue.me.ui.auth.primeriii


import com.google.gson.annotations.SerializedName

data class QuestionaireInfo(
    @SerializedName("append")
    val append: String,
    @SerializedName("class")
    val classX: String,
    @SerializedName("conditional_logic")
    val conditionalLogic: Int,
    @SerializedName("default_value")
    val defaultValue: String,
    @SerializedName("ID")
    val iD: Int,
    @SerializedName("id")
    val id: String,
    @SerializedName("instructions")
    val instructions: String,
    @SerializedName("key")
    val key: String,
    @SerializedName("label")
    val label: String,
    @SerializedName("max")
    val max: String,
    @SerializedName("menu_order")
    val menuOrder: Int,
    @SerializedName("min")
    val min: String,
//    @SerializedName("name")
//    val name: String,
    @SerializedName("_name")
    val name: String,
    @SerializedName("parent")
    val parent: Int,
    @SerializedName("placeholder")
    val placeholder: String,
    @SerializedName("prefix")
    val prefix: String,
    @SerializedName("prepend")
    val prepend: String,
    @SerializedName("required")
    val required: Int,
    @SerializedName("step")
    val step: String,
    @SerializedName("type")
    val type: String,
    @SerializedName("_valid")
    val valid: Int,
    @SerializedName("value")
    val value: Any?
)