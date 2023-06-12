package com.tovalue.me.model.whatworksguide

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName

data class WhatWorksGuideRespose(
    @SerializedName("status") var status: String? = null,
    @SerializedName("guides") var guides: List<Guide>? = null,
    @SerializedName("error") val error: String,
)

class Guide(
    @SerializedName("inventory_card") var inventoryCard: CardData? = null,
    @SerializedName("invitations_card") var invitationsCard: CardData? = null,
    @SerializedName("leveling_card") var levelingCard: CardData? = null,
    @SerializedName("experience_card") var experienceCard: CardData? = null
)

class CardData(
    @SerializedName("guide_id") var guideId: Int? = null,
    @SerializedName("header_image") var headerImage: String? = null,
    @SerializedName("header_title") var headerTitle: String? = null,
    @SerializedName("excerpt") var excerpt: String? = null
)
