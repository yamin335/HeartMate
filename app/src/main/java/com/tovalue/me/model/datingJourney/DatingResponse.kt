package com.tovalue.me.model.datingJourney

import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName

data class DatingResponse(
    val status: String,
    val carousel: Journeys? = null,
    @SerializedName("dating_journey_tutorial")
    val datingJourneyTutorial: DatingJourneyTutorial?= null
)


data class Journeys(
    var journeys: ArrayList<Journey> = arrayListOf()
)

data class DatingJourneyTutorial(
    @SerializedName("bullets")
    val bullets: List<String?>?,
    @SerializedName("heade")
    val heade: String?,
    @SerializedName("title")
    val title: String?
)


data class Journey(
    var avatar: String,
    var dating_journey_id: Int = 0,
    var dating_partner_id: Int = 0,
    var discoveries: Discoveries? = null,
    var first_name: String = "",
    var last_name: String = "",
    var our_status: OurStatus? = null,
    var value_me_score: String = "",
    val group_id: Int,
    val level_2_commitment: LevelTwoCommitment
)

data class Discoveries(
    var partner:JsonObject?=null,
    var user: User? = null
)

data class OurStatus(
    var level: String? = null,
    var title: String? = null
)

data class Partner(
    var name: Int = 0

)

data class LevelTwoCommitment(
    val description: String,
    val header: String,
    val comfortable_text: String,
    val balance_text: String,
    val safe_text: String
)

data class User(
    var me: Int = 0
)
