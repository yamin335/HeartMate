package com.tovalue.me.ui.levelTwoInvitation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tovalue.me.network.ResultWrapper
import com.tovalue.me.ui.levelTwoInvitation.response.CommitmentOfferResponse
import com.tovalue.me.util.SingleLiveEvent
import com.tovalue.me.util.extensions.runIO

class LevelTwoInvitationViewModel : ViewModel() {

    private val levelTwoInvitationRepository = LevelTwoInvitationRepository()

    var commitmentOfferToPass = MutableLiveData<CommitmentOfferResponse>()

    private val _commitmentOfferEventState = SingleLiveEvent<CommitmentOfferEvent>()
    val commitmentOfferEventState: LiveData<CommitmentOfferEvent> = _commitmentOfferEventState

    private val _acceptCommitmentOfferEventState = SingleLiveEvent<AcceptCommitmentOfferEvent>()
    val acceptCommitmentOfferEventState: LiveData<AcceptCommitmentOfferEvent> =
        _acceptCommitmentOfferEventState

    private val _rejectCommitmentOfferEventState = SingleLiveEvent<RejectCommitmentOfferEvent>()
    val rejectCommitmentOfferEventState: LiveData<RejectCommitmentOfferEvent> =
        _rejectCommitmentOfferEventState

    private val _doubleConfirmationOfferEventState = SingleLiveEvent<DoubleConfirmationOfferEvent>()
    val doubleConfirmationOfferEventState: LiveData<DoubleConfirmationOfferEvent> =
        _doubleConfirmationOfferEventState

    fun getCommitmentOffer(
        offerPostId: Int,
    ) {
        runIO {
            when (val response = levelTwoInvitationRepository.getCommitmentOffer(
                offerPostId = offerPostId,
            )) {
                is ResultWrapper.GenericError -> {
                    _commitmentOfferEventState.postValue(
                        CommitmentOfferEvent.Error(
                            response.error
                        )
                    )
                }
                is ResultWrapper.Success -> {
                    if (response.value.isSuccessful && response.value.body()!!.status == "ok") {
                        commitmentOfferToPass.postValue(response.value.body())
                        _commitmentOfferEventState.postValue(
                            CommitmentOfferEvent.DataResponse(
                                response.value.body()!!
                            )
                        )

                    } else
                        _commitmentOfferEventState.postValue(
                            CommitmentOfferEvent.Error(
                                response.value.body()?.error
                            )
                        )
                }
            }
        }
    }

    fun acceptCommitmentOffer(
//        offerPostId: Int,
//        groupId: Int? = null,
        comfortResponse: String? = null,
        balanceResponse: String? = null,
        safetyResponse: String? = null,
    ) {
        runIO {
            when (val response = levelTwoInvitationRepository.acceptCommitmentOffer(
                offerPostId = commitmentOfferToPass.value?.offer_post_id ?: 0,
                groupId = commitmentOfferToPass.value?.group_id ?: 0,
                comfortResponse = comfortResponse,
                balanceResponse = balanceResponse,
                safetyResponse = safetyResponse
            )) {
                is ResultWrapper.GenericError -> {
                    _acceptCommitmentOfferEventState.postValue(
                        AcceptCommitmentOfferEvent.Error(
                            response.error
                        )
                    )
                }
                is ResultWrapper.Success -> {
                    if (response.value.isSuccessful && response.value.body()!!.status == "ok") {
                        _acceptCommitmentOfferEventState.postValue(
                            AcceptCommitmentOfferEvent.DataResponse(
                                response.value.body()!!
                            )
                        )

                    } else
                        _acceptCommitmentOfferEventState.postValue(
                            AcceptCommitmentOfferEvent.Error(
                                response.value.body()?.error
                            )
                        )
                }
            }
        }
    }

    fun rejectCommitmentOffer(
//        offerPostId: Int,
//        groupId: Int? ,
        decisionResponse: Int? = null
    ) {
        runIO {
            when (val response = levelTwoInvitationRepository.declineCommitmentOffer(
                offerPostId =  commitmentOfferToPass.value?.offer_post_id ?: 0,
                groupId =  commitmentOfferToPass.value?.group_id ?: 0,
                decisionResponse = decisionResponse,
            )) {
                is ResultWrapper.GenericError -> {
                    _rejectCommitmentOfferEventState.postValue(
                        RejectCommitmentOfferEvent.Error(
                            response.error
                        )
                    )
                }
                is ResultWrapper.Success -> {
                    if (response.value.isSuccessful && response.value.body()!!.status == "ok") {
                        _rejectCommitmentOfferEventState.postValue(
                            RejectCommitmentOfferEvent.DataResponse(
                                response.value.body()!!
                            )
                        )

                    } else
                        _rejectCommitmentOfferEventState.postValue(
                            RejectCommitmentOfferEvent.Error(
                                response.value.body()?.error
                            )
                        )
                }
            }
        }
    }

    fun commitmentDoubleConfirmation(
        acceptancePostId: Int? = null,
        rejectionMessage: String? = null,
        acceptanceStatus: Int? = null,
        groupId: Int? = null,
    ) {
        runIO {
            when (val response = levelTwoInvitationRepository.commitmentDoubleConfirmation(
                groupId = commitmentOfferToPass.value?.group_id ?: 0,
                acceptancePostId = commitmentOfferToPass.value?.offer_post_id,
                rejectionMessage = commitmentOfferToPass.value?.rejection_message ?: "",
                acceptanceStatus = commitmentOfferToPass.value?.acceptance_status ?: 0,
            )) {
                is ResultWrapper.GenericError -> {
                    _doubleConfirmationOfferEventState.postValue(
                        DoubleConfirmationOfferEvent.Error(
                            response.error
                        )
                    )
                }
                is ResultWrapper.Success -> {
                    if (response.value.isSuccessful && response.value.body()!!.status == "ok") {
                        _doubleConfirmationOfferEventState.postValue(
                            DoubleConfirmationOfferEvent.DataResponse(
                                response.value.body()!!
                            )
                        )

                    } else
                        _doubleConfirmationOfferEventState.postValue(
                            DoubleConfirmationOfferEvent.Error(
                                response.value.body()?.error
                            )
                        )
                }
            }
        }
    }


    sealed interface CommitmentOfferEvent {
        data class Error(val errorMsg: String?) : CommitmentOfferEvent
        data class DataResponse(val response: CommitmentOfferResponse) : CommitmentOfferEvent

    }

    sealed interface AcceptCommitmentOfferEvent {
        data class Error(val errorMsg: String?) : AcceptCommitmentOfferEvent
        data class DataResponse(val response: CommitmentOfferResponse) : AcceptCommitmentOfferEvent

    }

    sealed interface RejectCommitmentOfferEvent {
        data class Error(val errorMsg: String?) : RejectCommitmentOfferEvent
        data class DataResponse(val response: CommitmentOfferResponse) : RejectCommitmentOfferEvent

    }

    sealed interface DoubleConfirmationOfferEvent {
        data class Error(val errorMsg: String?) : DoubleConfirmationOfferEvent
        data class DataResponse(val response: CommitmentOfferResponse) :
            DoubleConfirmationOfferEvent

    }


}