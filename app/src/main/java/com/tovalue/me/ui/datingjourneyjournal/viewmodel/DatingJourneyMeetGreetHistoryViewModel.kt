package com.tovalue.me.ui.datingjourneyjournal.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tovalue.me.model.datingJourney.DatingMeetGreetHistoryResponse
import com.tovalue.me.model.datingJourney.DatingMeetGreetResponse
import com.tovalue.me.network.ResultWrapper
import com.tovalue.me.ui.auth.AuthRepository
import com.tovalue.me.util.extensions.runIO

class DatingJourneyMeetGreetHistoryViewModel : ViewModel() {

    private val dateJourneyRepository = DatingJourneyRepository()
    private val authRepo = AuthRepository()


    private val _meetGreetHistoryState = MutableLiveData<GetMGTestimonialHistory>()
    val meetGreetHistoryState: LiveData<GetMGTestimonialHistory> = _meetGreetHistoryState

    fun getMeetGreetHistory(groupId: Int) {
        runIO {
            when (val response = dateJourneyRepository.getMeetGreetHistory(
                groupId
            )) {
                is ResultWrapper.GenericError -> {
                    _meetGreetHistoryState.postValue(GetMGTestimonialHistory.Error(response.error))
                }
                is ResultWrapper.Success -> {
                    if (response.value.isSuccessful && response.value.body()!!.status == "ok") {
                        authRepo.removeGroupId()
                        _meetGreetHistoryState.postValue(GetMGTestimonialHistory.DataResponse(response.value.body()!!))
                    } else {
                        _meetGreetHistoryState.postValue(
                            GetMGTestimonialHistory.Error(
                                response.value.errorBody().toString()
                            )
                        )
                    }
                }
            }
        }
    }


    sealed interface GetMGTestimonialHistory {
        data class Error(val errorMsg: String?) : GetMGTestimonialHistory
        data class DataResponse(val response: DatingMeetGreetHistoryResponse) : GetMGTestimonialHistory
    }


}