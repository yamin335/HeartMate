package com.tovalue.me.model


import com.google.gson.annotations.SerializedName

data class TvmReport(
    @SerializedName("aesthetic")
    val aesthetic: Int,
    @SerializedName("emotional")
    val emotional: Int,
    @SerializedName("entertainment")
    val entertainment: Int,
    @SerializedName("first_name")
    val firstName: String,
    @SerializedName("intellectual")
    val intellectual: Int,
    @SerializedName("professional")
    val professional: Int,
    @SerializedName("sexual")
    val sexual: Int,
    @SerializedName("spiritual")
    val spiritual: Int,
    @SerializedName("status")
    val status: String,
    @SerializedName("summary")
    val summary: List<String>,
    @SerializedName("total_personality_facets")
    val totalPersonalityFacets: Int,
    @SerializedName("village")
    val village: Int,
    @SerializedName("facet_description")   /*Day Night Catalog*/
    val facetDescription: String,
    @SerializedName("ideas_tab")
    val dayNightTab: List<String>,
    @SerializedName("possibilities_tab")
    val possibilityTab: List<String>
)