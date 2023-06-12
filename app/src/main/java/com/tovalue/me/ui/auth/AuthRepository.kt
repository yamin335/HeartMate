package com.tovalue.me.ui.auth

import com.google.gson.Gson
import com.tovalue.me.helper.MomensityBingoApp
import com.tovalue.me.model.Oauth
import com.tovalue.me.model.ProfileInfo
import com.tovalue.me.network.APIClient
import com.tovalue.me.network.safeApiCall
import com.tovalue.me.util.Constants
import com.tovalue.me.util.Constants.AVATAR
import com.tovalue.me.util.Constants.BIRTH_KEY
import com.tovalue.me.util.Constants.DEVICE_TOKEN
import com.tovalue.me.util.Constants.EMAIL_KEY
import com.tovalue.me.util.Constants.GROUP_ID
import com.tovalue.me.util.Constants.IS_FRESH_TOKEN_NEEDED
import com.tovalue.me.util.Constants.IS_INVITATION_VISITED
import com.tovalue.me.util.Constants.IS_USER_LOGGED_IN_CODE_KEY
import com.tovalue.me.util.Constants.LIFE_INVENTORY_ID_CODE_KEY
import com.tovalue.me.util.Constants.LOGIN_DATA_KEY
import com.tovalue.me.util.Constants.NAME_KEY
import com.tovalue.me.util.Constants.PASS_CODE_KEY
import com.tovalue.me.util.Constants.JWT_CODE_KEY
import com.tovalue.me.util.Constants.SPECTRUM_DEMO
import com.tovalue.me.util.Constants.USER_ID_CODE_KEY
import com.tovalue.me.util.Constants.USER_NAME_CODE_KEY

class AuthRepository {


	suspend fun getCookie(number: String) = safeApiCall {
		APIClient.aPIClient.
		getCookie(number = number)
	}

	suspend fun getNonce(controllerName: String, controllerMethod: String) = safeApiCall {
		APIClient.aPIClient.getNonceToken(controllerName, controllerMethod)
	}

	suspend fun createUser(
		email: String,
		password: String,
		username: String,
		key: String,
		nonceValue: String
	) = safeApiCall {
		APIClient.aPIClient.createUser(
			email = email,
			userName = username,
			password = password,
			nonceValue = nonceValue
		)
	}

	suspend fun getCorporateLinks() = safeApiCall {
		APIClient.aPIClient.getCorporateInfo()
	}

	suspend fun updateUser(
		firstName: String,
		lastName: String,
		email: String,
		displayName: String
	) = safeApiCall {
		APIClient.aPIClient.
		updateUser(
			firstName = firstName,
			lastName = lastName,
			email = email,
			displayName = displayName
		)
	}

	suspend fun updateNotificationSetting(enable: Boolean, birthDate: String) = safeApiCall {
		APIClient.aPIClient.updateUserMetaValues(
			notification = enable,
			discovery = enable,
			invitation = enable,
			newMoodRings = enable,
			promotion = enable,
			announcement = enable,
			birthday = birthDate
		)
	}

	suspend fun updateDatingPreference(maxDistance: String,location: String,dating_availability:String) = safeApiCall {
		APIClient.aPIClient.updateUserMetaValues(
			eventMaxDistance = maxDistance,
			registeredLocation = location,
			dating_availability = dating_availability
		)
	}

	// sending lat lng in string form now according to the api
	suspend fun updateAvailability(availability: String?) = safeApiCall {
		APIClient.aPIClient.updateAvailability(
			metaValue = availability
		)
	}

	suspend fun getInventoryData() =  safeApiCall {
	APIClient.aPIClient.getInventorySides(userId = getUserId())
	}

	suspend fun getInventoryQuestion(groupKey: Int) = safeApiCall {
		APIClient.aPIClient.getInventoryQuestion(groupKey)
	}

	suspend fun emailExists(email: String) = safeApiCall {
		APIClient.aPIClient.emailExist(email)
	}

	suspend fun updateInventoryValues(
		categoryId: Int,
		serializedJson: String,
		categoryTitle: String
	) = safeApiCall {
		APIClient.aPIClient.updateInventoryValues(
			categoryId = categoryId,
			answerValues = serializedJson,
			userId = getUserId(),
			title = categoryTitle
		)
	}

	suspend fun getPersonalityFacts() = safeApiCall {
		APIClient.aPIClient.getTVMReport()
	}

	suspend fun getDayNightCateLog() =
		safeApiCall { APIClient.aPIClient.getGuideCatalog() }

	suspend fun acceptLevelOneInvitation(invitationCode: String, id: Int) =
		safeApiCall {
			APIClient.aPIClient.acceptLevelOneInvitation(
				code = invitationCode,
				userId = id
			)
		}

	suspend fun declineLevelOneInvitation(invitationCode: String, id: Int) =
		safeApiCall {
			APIClient.aPIClient.declineLevelOneInvitation(
				code = invitationCode,
				userId = id
			)
		}


	suspend fun getOnBoardingVideo() = safeApiCall {
		APIClient.aPIClient.getCorporateInfo()
	}


	suspend fun updateUserLocation(lat: Double, lng: Double, location: String) = safeApiCall {
		APIClient.aPIClient.updateUserMetaValues(
			lat = lat,
			lng = lng,
			registeredLocation = location
		)
	}

	// preference Data
	fun saveRegistrationProgress(stage: String) =
		MomensityBingoApp.preferencesManager!!.setStringValue(STAGE, stage)

	// hardcode the cookie | will be removed later
	// passing cookie in headers now, as cookie still required to pass in parameter so for this using dummy value
	fun saveToken(token: String) = MomensityBingoApp.preferencesManager!!.setStringValue(
		JWT_CODE_KEY,
		token
	)

	fun getToken(): String =
		MomensityBingoApp.preferencesManager!!.getStringValue(JWT_CODE_KEY).toString()

	fun saveName(name: String) = MomensityBingoApp.preferencesManager!!.setStringValue(
		NAME_KEY, name
	)

	fun saveEmail(email: String) = MomensityBingoApp.preferencesManager!!.setStringValue(
		EMAIL_KEY, email
	)

	fun getSavedEmail(): String =
		MomensityBingoApp.preferencesManager!!.getStringValue(EMAIL_KEY).toString()

	fun getSavedName(): String =
		MomensityBingoApp.preferencesManager!!.getStringValue(NAME_KEY).toString()

	fun saveBirthDate(date: String) =
		MomensityBingoApp.preferencesManager!!.setStringValue(BIRTH_KEY, date)

	fun getBirthDate(): String =
		MomensityBingoApp.preferencesManager!!.getStringValue(BIRTH_KEY).toString()

	fun saveUserId(id: Int) =
		MomensityBingoApp.preferencesManager!!.setIntValue(USER_ID_CODE_KEY, id)

	fun getUserId(): Int = MomensityBingoApp.preferencesManager!!.getIntValue(
		USER_ID_CODE_KEY
	)

	fun saveInventoryId(lifeInventoryId: Int) =
		MomensityBingoApp.preferencesManager!!.setIntValue(LIFE_INVENTORY_ID_CODE_KEY, lifeInventoryId)

	fun getInventoryId(): Int = MomensityBingoApp.preferencesManager!!.getIntValue(
		LIFE_INVENTORY_ID_CODE_KEY
	)

	fun setUserLoggedIn(enable: Boolean) =
		MomensityBingoApp.preferencesManager!!.setBooleanValue(IS_USER_LOGGED_IN_CODE_KEY, enable)

	fun isUserLoggedIn(): Boolean = MomensityBingoApp.preferencesManager!!.getBooleanValue(
		IS_USER_LOGGED_IN_CODE_KEY)

	fun setUserName(username: String) =
		MomensityBingoApp.preferencesManager!!.setStringValue(USER_NAME_CODE_KEY, username)

	fun getUserName(): String = MomensityBingoApp.preferencesManager!!.getStringValue(
		USER_NAME_CODE_KEY).toString()

	fun setPass(pass: String) =
		MomensityBingoApp.preferencesManager!!.setStringValue(PASS_CODE_KEY, pass)

	fun getPass(): String = MomensityBingoApp.preferencesManager!!.getStringValue(PASS_CODE_KEY).toString()

	// removed later [ saving image locally ]
	fun setAvatar(url: String) {
		MomensityBingoApp.preferencesManager!!.setStringValue(AVATAR, url)
	}

	fun getAvatar(): String = MomensityBingoApp.preferencesManager!!.getStringValue(AVATAR).toString()

	fun setLoginData(loginResponse: Oauth) = MomensityBingoApp.preferencesManager!!.setStringValue(
		LOGIN_DATA_KEY, Gson().toJson(loginResponse))
	fun getLoginData(): Oauth? = Gson().fromJson(MomensityBingoApp.preferencesManager!!.getStringValue(
		LOGIN_DATA_KEY), Oauth::class.java)

	fun setGroupId(groupId: Int) = MomensityBingoApp.preferencesManager!!.setIntValue(GROUP_ID, groupId)
	fun getGroupId(): Int = MomensityBingoApp.preferencesManager!!.getIntValue(GROUP_ID)
	fun removeGroupId() = MomensityBingoApp.preferencesManager!!.remove(GROUP_ID)

	fun setSpectrumMusicUrl(demoUrl: String) = MomensityBingoApp.preferencesManager!!.setStringValue(
		SPECTRUM_DEMO, demoUrl)
	fun getSpectrumMusicUrl(): String = MomensityBingoApp.preferencesManager!!.getStringValue(
		SPECTRUM_DEMO).toString()

	fun redirectToInvitationLaunch(enabled: Boolean) = MomensityBingoApp.preferencesManager!!.setBooleanValue(IS_INVITATION_VISITED, enabled)
	fun getInvitationRedirection(): Boolean = MomensityBingoApp.preferencesManager!!.getBooleanValue(IS_INVITATION_VISITED)
	fun removeInvitationRedirection() = MomensityBingoApp.preferencesManager!!.remove(IS_INVITATION_VISITED)
	fun setDeviceToken(devieToken: String) = MomensityBingoApp.preferencesManager!!.setStringValue(
		DEVICE_TOKEN, devieToken)
	fun getDeviceToken() = MomensityBingoApp.preferencesManager!!.getStringValue(DEVICE_TOKEN).toString()
	fun setFreshTokenNeeded(isNeeded: Boolean) = MomensityBingoApp.preferencesManager!!.setBooleanValue(IS_FRESH_TOKEN_NEEDED, isNeeded)
	fun isFreshTokenNeeded(): Boolean = MomensityBingoApp.preferencesManager!!.getBooleanValue(
		IS_FRESH_TOKEN_NEEDED)

	fun getSubscriptionType(): String =
		MomensityBingoApp.preferencesManager?.getStringValue(Constants.SUBSCRIPTION_PLAN_TYPE).toString()

	fun isSubscriptionActive(): Boolean =
		MomensityBingoApp.preferencesManager?.getStringValue(Constants.SUBSCRIPTION_PLAN_STATUS).equals(
			Constants.ACTIVE)

	// save only those values which reuired
	// [@refactor model]
	fun getUserObj(): ProfileInfo? {
		var profileInfo: ProfileInfo? = null
		MomensityBingoApp.preferencesManager?.let {
			profileInfo = Gson().fromJson(
				it.getStringValue(Constants.USER_DATA_CODE_KEY),
				ProfileInfo::class.java
			)
		}

		return profileInfo
	}
}