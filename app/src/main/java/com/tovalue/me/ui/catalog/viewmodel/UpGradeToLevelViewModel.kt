package com.tovalue.me.ui.catalog.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tovalue.me.model.UpgradeToLevelModel
import com.tovalue.me.network.ResultWrapper
import com.tovalue.me.ui.auth.AuthRepository
import com.tovalue.me.ui.catalog.UpGradeToLevelRepository
import com.tovalue.me.ui.dashboard.DashboardRepository
import com.tovalue.me.util.extensions.runIO

class UpGradeToLevelViewModel :ViewModel() {

    private val upGradeToLevelRepository = UpGradeToLevelRepository()
    private val authRepo = AuthRepository()
    private val repo = DashboardRepository()


    private val _journeyState = MutableLiveData<UpGradeTiLevelEvent>()
    val journeyState: MutableLiveData<UpGradeTiLevelEvent> = _journeyState
    
    
    fun UpGradeToLevel(
        groupId: Int,
        level: Int,
        balanceResponse: String? = null,
        comfortResponse: String? = null,
        safetyResponse: String? = null
    ) {
        runIO {
            when (val response = upGradeToLevelRepository.upGradeToLevel(
                groupId,
                level,
                balanceResponse,
                comfortResponse,
                safetyResponse
            )) {
                is ResultWrapper.GenericError -> {
                  _journeyState.postValue(UpGradeTiLevelEvent.Error(response.error))
                }
                is ResultWrapper.Success -> {
                     if (response.value.isSuccessful && response.value.body()!!.status == "ok") {
                         Log.i("TAG->", "UpGradeToLevel: ${response.value.body()}")
                         _journeyState.postValue(UpGradeTiLevelEvent.DataResponse(response.value.body()!!))
                    }
                    else
                        _journeyState.postValue(UpGradeTiLevelEvent.Error(response.value.errorBody().toString()))
                }
                }
            }
        }
    
    fun subscriptionType() = repo.getSubscriptionType()
    fun activeSubscription() = repo.isSubscriptionActive()

    sealed interface UpGradeTiLevelEvent {
        data class Error(val errorMsg: String?): UpGradeTiLevelEvent
        data class DataResponse(val response: UpgradeToLevelModel): UpGradeTiLevelEvent
    }
}