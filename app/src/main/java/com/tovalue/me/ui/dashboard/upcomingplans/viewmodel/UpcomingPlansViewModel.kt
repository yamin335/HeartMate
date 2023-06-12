package com.tovalue.me.ui.dashboard.upcomingplans.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tovalue.me.model.Generic
import com.tovalue.me.model.upcomingplans.UpcomingPlansResponse
import com.tovalue.me.network.ResultWrapper
import com.tovalue.me.ui.auth.AuthRepository
import com.tovalue.me.ui.dashboard.upcomingplans.models.*
import com.tovalue.me.util.SingleLiveEvent
import com.tovalue.me.util.extensions.runIO

class UpcomingPlansViewModel : ViewModel() {

    private val upcomingPlansRepository = UpcomingPlansRepository()
    private val authRepo = AuthRepository()

    private val _upcomingPlansState = SingleLiveEvent<UpcomingPlansEvent>()
    val upcomingPlansState: LiveData<UpcomingPlansEvent> = _upcomingPlansState

    private val _scheduleMeTime = SingleLiveEvent<ScheduleMeTimeEvent>()
    val scheduleMeTime: LiveData<ScheduleMeTimeEvent> = _scheduleMeTime

    private val _upcomingPlanDetail = SingleLiveEvent<UpcomingPlanEventDetailEvent>()
    val upcomingPlanDetail: LiveData<UpcomingPlanEventDetailEvent> = _upcomingPlanDetail

    private val _cancelReasonState = SingleLiveEvent<CancelEventReasonEvent>()
    val cancelReasonState: LiveData<CancelEventReasonEvent> = _cancelReasonState

    private val _cancelMeTimeState = SingleLiveEvent<CancelMeTimeEvent>()
    val cancelMeTimeState: LiveData<CancelMeTimeEvent> = _cancelMeTimeState


    private val _submitDateNightOfferDecisionState = SingleLiveEvent<SubmitDateNightOfferDecision>()
    val submitDateNightOfferDecisionState: LiveData<SubmitDateNightOfferDecision> =
        _submitDateNightOfferDecisionState

    private val _cancelDateNightEvent = SingleLiveEvent<CancelDateNightEvent>()
    val cancelDateNightEvent: LiveData<CancelDateNightEvent> = _cancelDateNightEvent


    private val _rescheduleEvent = SingleLiveEvent<RescheduleEvent>()
    val rescheduleEvent: LiveData<RescheduleEvent> = _rescheduleEvent

    private val _acceptRescheduleDateNightEvent = SingleLiveEvent<AcceptRescheduleDateNightEvent>()
    val acceptRescheduleDateNightEvent: LiveData<AcceptRescheduleDateNightEvent> = _acceptRescheduleDateNightEvent

    private val _submitDateNightOfferDecisionForReSchedulingState = SingleLiveEvent<SubmitDateNightOfferDecisionForRescheduling>()
    val submitDateNightOfferDecisionForReSchedulingState: LiveData<SubmitDateNightOfferDecisionForRescheduling> =
        _submitDateNightOfferDecisionForReSchedulingState

    fun getMasterPlanner(
        fromDate: String,
        toDate: String,
        groupId: String
    ) {
        runIO {
            when (val response = upcomingPlansRepository.getMasterPlanner(
                fromDate = fromDate,
                toDate = toDate,
                groupId = groupId

            )) {
                is ResultWrapper.GenericError -> {
                    _upcomingPlansState.postValue(UpcomingPlansEvent.Error(response.error))
                }
                is ResultWrapper.Success -> {
                    if (response.value.isSuccessful && response.value.body()!!.status == "ok") {
                        _upcomingPlansState.postValue(UpcomingPlansEvent.DataResponse(response.value.body()!!))
                    } else
                        _upcomingPlansState.postValue(
                            UpcomingPlansEvent.Error(
                                response.value.body()?.error
                            )
                        )
                }
            }
        }
    }

    fun scheduleMeTime(
        startTime: String,
        startDate: String,
        endTime: String,
        endDate: String
    ) {
        runIO {
            when (val response = upcomingPlansRepository.scheduleMeTime(
                startDate = startDate,
                startTime = startTime,
                endDate = endDate,
                endTime = endTime
            )) {
                is ResultWrapper.GenericError -> {
                    _scheduleMeTime.postValue(ScheduleMeTimeEvent.Error(response.error))
                }
                is ResultWrapper.Success -> {
                    if (response.value.isSuccessful && response.value.body()!!.status == "ok") {

                        _scheduleMeTime.postValue(ScheduleMeTimeEvent.DataResponse(response.value.body()!!))
                    } else
                        _scheduleMeTime.postValue(
                            ScheduleMeTimeEvent.Error(
                                response.value.errorBody().toString()
                            )
                        )
                }
            }
        }
    }

    fun getEventDetail(
        eventId: Int
    ) {
        runIO {
            when (val response = upcomingPlansRepository.getEventDetail(
                eventId = eventId
            )) {
                is ResultWrapper.GenericError -> {
                    _upcomingPlanDetail.postValue(UpcomingPlanEventDetailEvent.Error(response.error))
                }
                is ResultWrapper.Success -> {
                    if (response.value.isSuccessful && response.value.body()!!.status == "ok") {

                        _upcomingPlanDetail.postValue(
                            UpcomingPlanEventDetailEvent.DataResponse(
                                response.value.body()!!
                            )
                        )
                    } else
                        _upcomingPlanDetail.postValue(
                            UpcomingPlanEventDetailEvent.Error(
                                response.value.errorBody().toString()
                            )
                        )
                }
            }
        }
    }

    fun getReScheduledEventDetail(
        eventId: Int
    ) {
        runIO {
            when (val response = upcomingPlansRepository.getReScheduledEventDetail(
                eventId = eventId
            )) {
                is ResultWrapper.GenericError -> {
                    _upcomingPlanDetail.postValue(UpcomingPlanEventDetailEvent.Error(response.error))
                }
                is ResultWrapper.Success -> {
                    if (response.value.isSuccessful && response.value.body()!!.status == "ok") {

                        _upcomingPlanDetail.postValue(
                            UpcomingPlanEventDetailEvent.DataResponse(
                                response.value.body()!!
                            )
                        )
                    } else
                        _upcomingPlanDetail.postValue(
                            UpcomingPlanEventDetailEvent.Error(
                                response.value.errorBody().toString()
                            )
                        )
                }
            }
        }
    }

    fun submitDateNightOfferDecision(
        dateNightOfferId: Int,
        decision: Int,
        startTime: String = "",
        startDate: String = "",
        endTime: String = "",
        endDate: String = ""
    ) {
        runIO {
            when (val response = upcomingPlansRepository.submitDateNightOfferDecision(
                dateNightOfferId = dateNightOfferId,
                decision = decision,
                startDate = startDate,
                startTime = startTime,
                endDate = endDate,
                endTime = endTime
            )) {
                is ResultWrapper.GenericError -> {
                    _submitDateNightOfferDecisionState.postValue(
                        SubmitDateNightOfferDecision.Error(
                            response.error
                        )
                    )
                }
                is ResultWrapper.Success -> {
                    if (response.value.isSuccessful && response.value.body()!!.status == "ok") {

                        _submitDateNightOfferDecisionState.postValue(
                            SubmitDateNightOfferDecision.DataResponse(
                                response.value.body()!!
                            )
                        )
                    } else
                        _submitDateNightOfferDecisionState.postValue(
                            SubmitDateNightOfferDecision.Error(
                                response.value.body()?.error
                            )
                        )
                }
            }
        }
    }

    fun submitDateNightOfferDecisionForRescheduling(
        dateNightOfferId: Int,
        decision: Int,
        startTime: String = "",
        startDate: String = "",
        endTime: String = "",
        endDate: String = ""
    ) {
        runIO {
            when (val response = upcomingPlansRepository.submitDateNightOfferDecision(
                dateNightOfferId = dateNightOfferId,
                decision = decision,
                startDate = startDate,
                startTime = startTime,
                endDate = endDate,
                endTime = endTime
            )) {
                is ResultWrapper.GenericError -> {
                    _submitDateNightOfferDecisionForReSchedulingState.postValue(
                        SubmitDateNightOfferDecisionForRescheduling.Error(
                            response.error
                        )
                    )
                }
                is ResultWrapper.Success -> {
                    if (response.value.isSuccessful && response.value.body()!!.status == "ok") {

                        _submitDateNightOfferDecisionForReSchedulingState.postValue(
                            SubmitDateNightOfferDecisionForRescheduling.DataResponse(
                                response.value.body()!!
                            )
                        )
                    } else
                        _submitDateNightOfferDecisionForReSchedulingState.postValue(
                            SubmitDateNightOfferDecisionForRescheduling.Error(
                                response.value.body()?.error
                            )
                        )
                }
            }
        }
    }


    fun offerRescheduleDateNightEvent(
        eventId: Int,
        startTime: String = "",
        startDate: String = "",
        endTime: String = "",
        endDate: String = ""
    ) {
        runIO {
            when (val response = upcomingPlansRepository.offerRescheduleDateNightEvent(
                eventId = eventId,
                startDate = startDate,
                startTime = startTime,
                endDate = endDate,
                endTime = endTime
            )) {
                is ResultWrapper.GenericError -> {
                    _rescheduleEvent.postValue(
                        RescheduleEvent.Error(
                            response.error
                        )
                    )
                }
                is ResultWrapper.Success -> {
                    if (response.value.isSuccessful && response.value.body()!!.status == "ok") {

                        _rescheduleEvent.postValue(
                            RescheduleEvent.DataResponse(
                                response.value.body()!!
                            )
                        )
                    } else
                        _rescheduleEvent.postValue(
                            RescheduleEvent.Error(
                                response.value.body()?.error
                            )
                        )
                }
            }
        }
    }

    fun acceptRescheduleDateNightEvent(
        eventId: Int,
        rescheduleOfferId: Int,
        startDate: String,
        startTime: String,
    ) {
        runIO {
            when (val response = upcomingPlansRepository.acceptRescheduleDateNightEvent(
                eventId = eventId,
                rescheduleOfferId=rescheduleOfferId,
                startDate,
                startTime,
            )) {
                is ResultWrapper.GenericError -> {
                    _acceptRescheduleDateNightEvent.postValue(
                        AcceptRescheduleDateNightEvent.Error(
                            response.error
                        )
                    )
                }
                is ResultWrapper.Success -> {
                    if (response.value.isSuccessful && response.value.body()!!.status == "ok") {

                        _acceptRescheduleDateNightEvent.postValue(
                            AcceptRescheduleDateNightEvent.DataResponse(
                                response.value.body()!!
                            )
                        )
                    } else
                        _acceptRescheduleDateNightEvent.postValue(
                            AcceptRescheduleDateNightEvent.Error(
                                response.value.body()?.error
                            )
                        )
                }
            }
        }
    }

    fun getDateNightOffer(
        dateNightOfferId: Int
    ) {
        runIO {
            when (val response = upcomingPlansRepository.getDayNightOffer(
                dateNightOfferId = dateNightOfferId
            )) {
                is ResultWrapper.GenericError -> {
                    _upcomingPlanDetail.postValue(UpcomingPlanEventDetailEvent.Error(response.error))
                }
                is ResultWrapper.Success -> {
                    if (response.value.isSuccessful && response.value.body()!!.status == "ok") {

                        _upcomingPlanDetail.postValue(
                            UpcomingPlanEventDetailEvent.DataResponse(
                                response.value.body()!!
                            )
                        )
                    } else
                        _upcomingPlanDetail.postValue(
                            UpcomingPlanEventDetailEvent.Error(
                                response.value.body()?.error
                            )
                        )
                }
            }
        }
    }

    fun getCancelReason(eventId: Int) {
        runIO {
            when (val response = upcomingPlansRepository.getCancelReasons(
                eventId = eventId
            )) {
                is ResultWrapper.GenericError -> {
                    _cancelReasonState.postValue(CancelEventReasonEvent.Error(response.error))
                }
                is ResultWrapper.Success -> {
                    if (response.value.isSuccessful && response.value.body()!!.status == "ok") {

                        _cancelReasonState.postValue(
                            CancelEventReasonEvent.DataResponse(
                                response.value.body()!!
                            )
                        )
                    } else
                        _cancelReasonState.postValue(
                            CancelEventReasonEvent.Error(
                                response.value.body()?.error
                            )
                        )
                }
            }
        }
    }

    fun cancelDateNightEvent(
        eventId: Int,
        cancelReasonId: Long,

        ) {
        runIO {
            when (val response = upcomingPlansRepository.cancelDateNightEvent(
                eventId = eventId,
                cancelReasonId = cancelReasonId
            )) {
                is ResultWrapper.GenericError -> {
                    _cancelDateNightEvent.postValue(
                        CancelDateNightEvent.Error(
                            response.error
                        )
                    )
                }
                is ResultWrapper.Success -> {
                    if (response.value.isSuccessful && response.value.body()!!.status == "ok") {

                        _cancelDateNightEvent.postValue(
                            CancelDateNightEvent.DataResponse(
                                response.value.body()!!
                            )
                        )
                    } else
                        _cancelDateNightEvent.postValue(
                            CancelDateNightEvent.Error(
                                response.value.body()?.error
                            )
                        )
                }
            }
        }
    }

    fun cancelMeTIme(
        eventId: Int
    ) {
        runIO {
            when (val response = upcomingPlansRepository.cancelMeTime(
                eventId = eventId,
            )) {
                is ResultWrapper.GenericError -> {
                    _cancelMeTimeState.postValue(
                        CancelMeTimeEvent.Error(
                            response.error
                        )
                    )
                }
                is ResultWrapper.Success -> {
                    if (response.value.isSuccessful && response.value.body()!!.status == "ok") {
                        _cancelMeTimeState.postValue(
                            CancelMeTimeEvent.DataResponse(
                                response.value.body()!!
                            )
                        )
                    } else
                        _cancelMeTimeState.postValue(
                            CancelMeTimeEvent.Error(
                                response.value.body()?.error
                            )
                        )
                }
            }
        }
    }


    sealed interface UpcomingPlansEvent {
        data class Error(val errorMsg: String?) : UpcomingPlansEvent
        data class DataResponse(val response: UpcomingPlansResponse) : UpcomingPlansEvent
    }

    sealed interface ScheduleMeTimeEvent {
        data class Error(val errorMsg: String?) : ScheduleMeTimeEvent
        data class DataResponse(val response: Generic) : ScheduleMeTimeEvent
    }

    sealed interface UpcomingPlanEventDetailEvent {
        data class Error(val errorMsg: String?) : UpcomingPlanEventDetailEvent
        data class DataResponse(val response: UpcomingPlanDetailResponse) :
            UpcomingPlanEventDetailEvent
    }


    sealed interface RescheduleEvent {
        data class Error(val errorMsg: String?) : RescheduleEvent
        data class DataResponse(val response: RescheduleOrCancelEventResponse) :
            RescheduleEvent
    }

    sealed interface CancelEventReasonEvent {
        data class Error(val errorMsg: String?) : CancelEventReasonEvent
        data class DataResponse(val response: CancelReasonResponse) :
            CancelEventReasonEvent
    }

    sealed interface CancelMeTimeEvent {
        data class Error(val errorMsg: String?) : CancelMeTimeEvent
        data class DataResponse(val response: Generic) :
            CancelMeTimeEvent
    }

    sealed interface SubmitDateNightOfferDecision {
        data class Error(val errorMsg: String?) : SubmitDateNightOfferDecision
        data class DataResponse(val response: DateNightOfferDecisionResponse) :
            SubmitDateNightOfferDecision
    }

    sealed interface SubmitDateNightOfferDecisionForRescheduling {
        data class Error(val errorMsg: String?) : SubmitDateNightOfferDecisionForRescheduling
        data class DataResponse(val response: DateNightOfferDecisionResponse) :
            SubmitDateNightOfferDecisionForRescheduling
    }

    sealed interface CancelDateNightEvent {
        data class Error(val errorMsg: String?) : CancelDateNightEvent
        data class DataResponse(val response: RescheduleOrCancelEventResponse) :
            CancelDateNightEvent
    }

    sealed interface AcceptRescheduleDateNightEvent {
        data class Error(val errorMsg: String?) : AcceptRescheduleDateNightEvent
        data class DataResponse(val response: AcceptRescheduleOrCancelEventResponse) :
            AcceptRescheduleDateNightEvent
    }

}