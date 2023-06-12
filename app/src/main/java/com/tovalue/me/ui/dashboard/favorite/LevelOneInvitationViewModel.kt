package com.tovalue.me.ui.dashboard.favorite

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tovalue.me.model.ProfileInfo
import com.tovalue.me.network.ResultWrapper
import com.tovalue.me.ui.auth.AuthRepository
import com.tovalue.me.ui.dashboard.DashboardRepository
import com.tovalue.me.ui.dashboard.DashboardViewModel
import com.tovalue.me.util.Utils
import com.tovalue.me.util.extensions.runIO
import com.tovalue.me.util.livedata.Event

class LevelOneInvitationViewModel : ViewModel() {
    private val repo = DashboardRepository()
    private val authRepo = AuthRepository()

    private val _invitation = MutableLiveData<Invitation>()
    val invitation: LiveData<Invitation> = _invitation


    fun getUserObj(): ProfileInfo? = repo.getUserObj()

    fun levelOneInvitation(number: String, firstName: String, matchSource: String,invitationType:String,groupLevel:Int) {
        runIO {
            when (val response =
                repo.levelOneInvitation(
                    authRepo.getUserId(),
                    number,
                    firstName = firstName,
                    matchSource = matchSource,
                    invitationType = invitationType,
                    groupLevel = groupLevel
                )) {
                is ResultWrapper.GenericError -> _invitation.postValue(Invitation.Error(response.error))
                is ResultWrapper.Success -> {
                    if (response.value.isSuccessful && response.value.body()?.status == "ok") {
                        _invitation.postValue(Invitation.OpenInvitationDialog)
                    } else {
                        _invitation.postValue(Invitation.Error(response.value.body()?.error))
                    }
                }
            }
        }
    }

    fun getProfileInfo() {
        runIO {
            Log.i("TAG-->", "getRandomString:" + Utils.getRandomStr())
            when (val response = repo.getProfile(Utils.getRandomStr(), authRepo.getUserId())) {
                is ResultWrapper.GenericError -> _profileResponse.postValue(
                    Event(
                        DashboardViewModel.UiEventProfile.Error(
                            response.error
                        )
                    )
                )
                is ResultWrapper.Success -> {
                    if (response.value.isSuccessful && response.value.body()!!.status == "ok") {
                        authRepo.setSpectrumMusicUrl(response.value.body()!!.spectrum.audio_file)
                        repo.setUserObj(response.value.body()!!)
                        repo.setUserMetaValues(response.value.body()!!) // [@refactor]
                        _profileResponse.postValue(
                            Event(
                                DashboardViewModel.UiEventProfile.Profile(
                                    response.value.body()!!
                                )
                            )
                        )
                    } else {
                        if (response.value.body()!!.error.contains("generate_auth_cookie"))
                            _profileResponse.postValue(Event(DashboardViewModel.UiEventProfile.COOKIEXPIRE))
                        else
                            _profileResponse.postValue(
                                Event(
                                    DashboardViewModel.UiEventProfile.Error(
                                        response.value.body()!!.error
                                    )
                                )
                            )
                    }
                }
            }
        }
    }


    private val _profileResponse = MutableLiveData<Event<DashboardViewModel.UiEventProfile>>()
    val profileResponse: LiveData<Event<DashboardViewModel.UiEventProfile>> = _profileResponse

    sealed class Invitation {
        data class Error(val msg: String?) : Invitation()
        object OpenInvitationDialog : Invitation()
    }
}