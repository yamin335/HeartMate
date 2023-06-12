package com.tovalue.me.model

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName
import com.tovalue.me.model.datingJourney.HomeJourney


data class UpgradeToLevelModel (
    val status: String,
    val success: Boolean,
    val offer_post_id: Int
)