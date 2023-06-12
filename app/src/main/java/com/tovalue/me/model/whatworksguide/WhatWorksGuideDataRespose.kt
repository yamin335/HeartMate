package com.tovalue.me.model.whatworksguide

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName

class WhatWorksGuideDataRespose {
    @SerializedName("status")
    @Expose
    var status: String? = null

    @SerializedName("guide")
    @Expose
    var guide: List<Guide>? = null

    @SerializedName("error")
    @Expose
    var error: String? = null

    class Guide {
        @SerializedName("guide_id")
        @Expose
        var guideId: Int? = null

        @SerializedName("header_image")
        @Expose
        var headerImage: String? = null

        @SerializedName("header_title")
        @Expose
        var headerTitle: String? = null

        @SerializedName("summary")
        @Expose
        var summary: String? = null

        @SerializedName("tip_one")
        @Expose
        var tipOne: TipData? = null

        @SerializedName("tip_two")
        @Expose
        var tipTwo: TipData? = null

        @SerializedName("tip_three")
        @Expose
        var tipThree: TipData? = null
        @SerializedName("tip_four")
        @Expose
        var tipFour: TipData? = null
    }

    class TipData {
        @SerializedName("tip_title")
        var tipTitle: String? = null

        @SerializedName("title")
        var title: String? = null

        @SerializedName("description")
        var description: String? = null

        @SerializedName("image")
        var image: String? = null
    }

}

