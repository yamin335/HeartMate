package com.tovalue.me.ui.auth.primeriii


import com.google.gson.annotations.SerializedName

data class SideCategory(
    @SerializedName("category")
    val category: String,
    @SerializedName("category_id")
    val categoryId: Int,
    @SerializedName("group_key")
    val groupKey: String,
    @SerializedName("cover_image")
    val coverImage: String,
    @SerializedName("sub_title")
    val subTitle: String,
    @SerializedName("more_info")
    val moreInfo: String
)