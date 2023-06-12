package com.tovalue.me.ui.datingjourneyjournal.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tovalue.me.model.datingJourney.JourneyHomeResponse
import com.tovalue.me.network.ResultWrapper
import com.tovalue.me.ui.auth.AuthRepository
import com.tovalue.me.util.extensions.runIO

class DatingJourneyViewModel :ViewModel() {

    private val dateJourneyRepository = DatingJourneyRepository()
    private val authRepo = AuthRepository()


    private val _journeyState = MutableLiveData<DatingJourneyEvent>()
    val journeyState: LiveData<DatingJourneyEvent> = _journeyState

    /*    Deprecated*/
    fun getDateJourney(){
        runIO {
            dateJourneyRepository.getDatingJournal()

        }
    }

    fun getDatingHomeJourney(groupId: Int) {
        runIO {
            when(val response = dateJourneyRepository.getDatingHomeJourney(
                /* authRepo.getGroupId()*/groupId
            )) {
                is ResultWrapper.GenericError -> {
                    _journeyState.postValue(DatingJourneyEvent.Error(response.error))
                }
                is ResultWrapper.Success -> {
                    if (response.value.isSuccessful && response.value.body()!!.status == "ok") {
                        authRepo.removeGroupId()
                        _journeyState.postValue(DatingJourneyEvent.DataResponse(response.value.body()!!))
                    }
                    else
                        _journeyState.postValue(DatingJourneyEvent.Error(response.value.body()?.error))
                }
            }
        }
    }

    sealed interface DatingJourneyEvent {
        data class Error(val errorMsg: String?): DatingJourneyEvent
        data class DataResponse(val response: JourneyHomeResponse): DatingJourneyEvent
    }
}