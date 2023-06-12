package com.tovalue.me.ui.visionboard

import com.tovalue.me.network.APIClient
import com.tovalue.me.network.safeApiCall

class VisionBoardRepository {
	suspend fun visionBoardDetail() = safeApiCall { APIClient.aPIClient.getVisionBoard() }
	suspend fun mindsetData(txtMsg: String) = safeApiCall { APIClient.aPIClient.mindsetData(msg = txtMsg) }
	suspend fun adultSeasonData(
		groupOneMsg: String,
		groupTwoMsg: String
	) = safeApiCall {
		APIClient.aPIClient.adultSeasonData(
			primaryMsg = groupOneMsg, secondaryMsg = groupTwoMsg
		)
	}
	suspend fun blissData(groupOneMsg: String, groupTwoMsg: String) = safeApiCall { APIClient.aPIClient.blissData(
		primaryMsg = groupOneMsg,
		secondaryMsg = groupTwoMsg
	) }
	suspend fun dateStyleData(msg1: String, msg2: String, msg3: String) = safeApiCall {
		APIClient.aPIClient.dateStyleData(entryOne = msg1, entryTwo = msg2, entryThree = msg3)
	}
	suspend fun getPartnerVisionBoard() = safeApiCall { APIClient.aPIClient.getPartnerVisionBoard() }
	suspend fun getIceBreakerBoard() = safeApiCall { APIClient.aPIClient.getIceBreakers() }
}