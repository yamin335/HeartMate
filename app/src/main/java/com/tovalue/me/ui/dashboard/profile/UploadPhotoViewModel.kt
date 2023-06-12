package com.tovalue.me.ui.dashboard.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tovalue.me.model.Avatar
import com.tovalue.me.network.ResultWrapper
import com.tovalue.me.ui.auth.AuthRepository
import com.tovalue.me.util.extensions.runIO
import okhttp3.MultipartBody

class UploadPhotoViewModel : ViewModel() {
	private val authRepo = AuthRepository()
	private val photoRepo = PhotoRepository()
	
	private val _avatar = MutableLiveData<AvatarState>()
	val avatar: LiveData<AvatarState> = _avatar
	
	fun uploadUserAvatar(body: MultipartBody.Part) {
		runIO {
			when(val response = photoRepo.uploadProfilePhoto(body)){
				is ResultWrapper.GenericError -> _avatar.postValue(AvatarState.Error(response.error))
				is ResultWrapper.Success -> {
					if (response.value.isSuccessful && response.value.body()!!.status == "ok") {
						authRepo.setAvatar(response.value.body()!!.thumb)
						_avatar.postValue(AvatarState.UploadedPhoto(response.value.body()!!))
					}
					else
						_avatar.postValue(AvatarState.Error(response.value.body()!!.error))
				}
			}
		}
	}
	
	sealed class AvatarState {
		data class UploadedPhoto(val avatar: Avatar): AvatarState()
		data class Error(val error: String?): AvatarState()
	}
}