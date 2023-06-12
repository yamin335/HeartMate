package com.tovalue.me.ui.dashboard.manageInvitation

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.tovalue.me.network.ResultWrapper
import com.tovalue.me.util.SingleLiveEvent
import com.tovalue.me.util.extensions.runIO

class InvitationViewModel : ViewModel() {

    private val invitationHistoryRepository = InvitationHistoryRepository()

    private val _invitationHistoryState = SingleLiveEvent<InvitationHistoryEvent>()
    val invitationHistoryState: LiveData<InvitationHistoryEvent> = _invitationHistoryState

    private val _deleteInvitationHistoryState = SingleLiveEvent<DeleteInvitationHistoryEvent>()
    val deleteInvitationHistoryState: LiveData<DeleteInvitationHistoryEvent> =
        _deleteInvitationHistoryState

    fun getInvitationHistory(
    ) {
        runIO {
            when (val response = invitationHistoryRepository.getInvitationHistory(

            )) {
                is ResultWrapper.GenericError -> {
                    _invitationHistoryState.postValue(InvitationHistoryEvent.Error(response.error))
                }
                is ResultWrapper.Success -> {
                    if (response.value.isSuccessful && response.value.body()!!.status == "ok") {
                        _invitationHistoryState.postValue(
                            InvitationHistoryEvent.DataResponse(
                                response.value.body()!!
                            )
                        )
                    } else
                        _invitationHistoryState.postValue(
                            InvitationHistoryEvent.Error(
                                response.value.body()?.error
                            )
                        )
                }
            }
        }
    }

    fun deleteInvitationHistory(
        invitationCode: String,
        groupId: Int
    ) {
        runIO {
            when (val response = invitationHistoryRepository.deleteInvitationHistory(
                invitationCode,
                groupId
            )) {
                is ResultWrapper.GenericError -> {
                    _deleteInvitationHistoryState.postValue(DeleteInvitationHistoryEvent.Error(response.error))
                }
                is ResultWrapper.Success -> {
                    if (response.value.isSuccessful && response.value.body()!!.status == "ok") {
                        _deleteInvitationHistoryState.postValue(
                            DeleteInvitationHistoryEvent.DataResponse(
                                response.value.body()!!
                            )
                        )
                    } else
                        _deleteInvitationHistoryState.postValue(
                            DeleteInvitationHistoryEvent.Error(
                                response.value.body()?.error
                            )
                        )
                }
            }
        }
    }


    sealed interface InvitationHistoryEvent {
        data class Error(val errorMsg: String?) : InvitationHistoryEvent
        data class DataResponse(val response: InvitationsHistory) :
            InvitationHistoryEvent
    }

    sealed interface DeleteInvitationHistoryEvent {
        data class Error(val errorMsg: String?) : DeleteInvitationHistoryEvent
        data class DataResponse(val response: DeleteInvitation) :
            DeleteInvitationHistoryEvent
    }
}