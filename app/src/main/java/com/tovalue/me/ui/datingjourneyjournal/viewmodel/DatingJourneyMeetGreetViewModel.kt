package com.tovalue.me.ui.datingjourneyjournal.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tovalue.me.model.Error
import com.tovalue.me.model.datingJourney.DatingReviewCouplePlanResponse
import com.tovalue.me.model.datingJourney.DatingMeetGreetResponse
import com.tovalue.me.model.datingJourney.DatingSubmitMeetGreetResponse
import com.tovalue.me.network.ResultWrapper
import com.tovalue.me.ui.auth.AuthRepository
import com.tovalue.me.util.extensions.runIO

class DatingJourneyMeetGreetViewModel : ViewModel() {

    private val dateJourneyRepository = DatingJourneyRepository()
    private val authRepo = AuthRepository()


    private val _meetGreetQuestionState = MutableLiveData<GetMGTestimonial>()
    val meetGreetQuestionState: LiveData<GetMGTestimonial> = _meetGreetQuestionState

    private val _submitMGTestimonialState = MutableLiveData<SubmitMGTestimonialEvent>()
    val submitMGTestimonialState: LiveData<SubmitMGTestimonialEvent> = _submitMGTestimonialState

    private val _reviewCouplePlanState = MutableLiveData<GetReviewCouplePlanEvent>()
    val reviewCouplePlanState: LiveData<GetReviewCouplePlanEvent> = _reviewCouplePlanState

    fun getMeetGreetQuestions(groupId: Int) {
        runIO {
            when (val response = dateJourneyRepository.getMeetGreetQuestions(
                groupId
            )) {
                is ResultWrapper.GenericError -> {
                    _meetGreetQuestionState.postValue(GetMGTestimonial.Error(response.error))
                }
                is ResultWrapper.Success -> {
                    if (response.value.isSuccessful && response.value.body()!!.status == "ok") {
                        authRepo.removeGroupId()
                        _meetGreetQuestionState.postValue(GetMGTestimonial.DataResponse(response.value.body()!!))
                    } else {
                        _meetGreetQuestionState.postValue(
                            GetMGTestimonial.Error(
                                response.value.errorBody().toString()
                            )
                        )
                    }
                }
            }
        }
    }

    fun sendMGTestimonial(
        groupId: Int, forMonth: String,
        ofYear: String,
        experienceType: String,
        field1: String,
        field2: String,
        field3: String,
        inviationIncluded: Int,
        status: String
    ) {
        runIO {
            when (val response = dateJourneyRepository.sendMGTestimonial(groupId, forMonth, ofYear, experienceType, field1, field2, field3, inviationIncluded, status
            )) {
                is ResultWrapper.GenericError -> {
                    _submitMGTestimonialState.postValue(SubmitMGTestimonialEvent.Error(response.error))
                }

                is ResultWrapper.Success -> {
                    if (response.value.isSuccessful && response.value.body()!!.status == "ok") {
                        authRepo.removeGroupId()
                        _submitMGTestimonialState.postValue(SubmitMGTestimonialEvent.DataResponse(response.value.body()!!))
                    } else {
                        _submitMGTestimonialState.postValue(
                            SubmitMGTestimonialEvent.Error(
                                response.value.errorBody().toString()
                            )
                        )
                    }
                }
            }
        }
    }


    fun getReviewCouplePlan(testimonialId: Int) {
        runIO {
            when (val response = dateJourneyRepository.getReviewCouplePlan(
                testimonialId
            )) {
                is ResultWrapper.GenericError -> {
                    _reviewCouplePlanState.postValue(GetReviewCouplePlanEvent.Error(response.error))
                }
                is ResultWrapper.Success -> {
                    if (response.value.isSuccessful && response.value.body()!!.status == "ok") {
                        authRepo.removeGroupId()
                        _reviewCouplePlanState.postValue(GetReviewCouplePlanEvent.DataResponse(response.value.body()!!))
                    } else {
                        _reviewCouplePlanState.postValue(
                            GetReviewCouplePlanEvent.Error(
                                response.value.errorBody().toString()
                            )
                        )
                    }
                }
            }
        }
    }


    fun sendMGAckTestimonial(
        groupId: Int,testimonialId: Int, forMonth: String,
        ofYear: String,
        experienceType: String,
        field1: String,
        field2: String,
        field3: String,
        status: String
    ) {
        runIO {
            when (val response = dateJourneyRepository.sendMGACKTestimonial(groupId,testimonialId, forMonth, ofYear, experienceType, field1, field2, field3, status
            )) {
                is ResultWrapper.GenericError -> {
                    _submitMGTestimonialState.postValue(SubmitMGTestimonialEvent.Error(response.error))
                }
                is ResultWrapper.Success -> {
                    if (response.value.isSuccessful && response.value.body()!!.status == "ok") {
                        authRepo.removeGroupId()
                        _submitMGTestimonialState.postValue(SubmitMGTestimonialEvent.DataResponse(response.value.body()!!))
                    } else {
                        _submitMGTestimonialState.postValue(
                            SubmitMGTestimonialEvent.Error(
                                response.value.errorBody().toString()
                            )
                        )
                    }
                }
            }
        }
    }

    sealed interface GetMGTestimonial {
        data class Error(val errorMsg: String?) : GetMGTestimonial
        data class DataResponse(val response: DatingMeetGreetResponse) : GetMGTestimonial
    }

    sealed interface SubmitMGTestimonialEvent {
        data class Error(val errorMsg: String?) : SubmitMGTestimonialEvent
        data class DataResponse(val response: DatingSubmitMeetGreetResponse) : SubmitMGTestimonialEvent
    }

    sealed interface GetReviewCouplePlanEvent {
        data class Error(val errorMsg: String?) : GetReviewCouplePlanEvent
        data class DataResponse(val response: DatingReviewCouplePlanResponse) : GetReviewCouplePlanEvent
    }
}