package com.tovalue.me.model.datingJourney

import com.google.gson.annotations.SerializedName

data class JourneyHomeResponse (
	val status: String,
	val error: String,
	@SerializedName("journey_home")
	val homeJourney: ArrayList<HomeJourney>
)

data class HomeJourney(
	val group_id: Int,
	val level: String,
	val status: String,
	val goal_description: String,
	val week_id: Int,
	val partner_avatar: String,
	val partner: String,
	val partner_id: Int,
	val topic_id: Int,
	val my_avatar: String,
	val my_first_name: String
) {
	val error: String = ""
}