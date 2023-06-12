package com.tovalue.me.ui.dashboard.profile

import com.tovalue.me.network.APIClient
import com.tovalue.me.network.safeApiCall
import okhttp3.MultipartBody

class PhotoRepository {
	suspend fun uploadProfilePhoto(body: MultipartBody.Part) = safeApiCall {
		APIClient.aPIClient.avatarUpload(file = body)
	}
}