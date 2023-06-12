package com.tovalue.me.ui.datingjourneyjournal.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tovalue.me.model.DateNightOfferResponse
import com.tovalue.me.model.SendOfferDateNight
import com.tovalue.me.model.datingJourney.DatingOurCalenderResponse
import com.tovalue.me.model.upcomingplans.UpcomingPlansResponse
import com.tovalue.me.network.ResultWrapper
import com.tovalue.me.ui.datenightcatalog.viewmodel.DateNightCatalogViewModel
import com.tovalue.me.util.SingleLiveEvent
import com.tovalue.me.util.extensions.runIO

class DatingPartnerOurCalenderViewModel : ViewModel() {

    private val datingJourneyRepository = DatingJourneyRepository()

    private val _datingPartnerOurCalenderState = SingleLiveEvent<DatingPartnerOurCalender>()
    val datingPartnerOurCalenderState: LiveData<DatingPartnerOurCalender> = _datingPartnerOurCalenderState


    sealed interface DatingPartnerOurCalender {
        data class Error(val errorMsg: String?) : DatingPartnerOurCalender
        data class DataResponse(val response: DatingOurCalenderResponse) : DatingPartnerOurCalender
    }

    private val _sendDateNightOffer = MutableLiveData<UIEventSendDateNightOffer>()

    sealed class UIEventSendDateNightOffer {
        data class Error(val errorMsg: String?) : UIEventSendDateNightOffer()
        data class Data(val data: DateNightOfferResponse) : UIEventSendDateNightOffer()
    }

    fun getMasterPlannerForCalender(
        fromDate: String,
        toDate: String,
        groupId: String
    ) {
        runIO {
            when (val response = datingJourneyRepository.getMasterPlannerForCalender(
                fromDate = fromDate,
                toDate = toDate,
                groupId = groupId

            )) {
                is ResultWrapper.GenericError -> {
                    _datingPartnerOurCalenderState.postValue(DatingPartnerOurCalender.Error(response.error))
                }
                is ResultWrapper.Success -> {
                    if (response.value.isSuccessful && response.value.body()!!.status == "ok") {
                        _datingPartnerOurCalenderState.postValue(DatingPartnerOurCalender.DataResponse(response.value.body()!!))
                    } else
                        _datingPartnerOurCalenderState.postValue(
                            DatingPartnerOurCalender.Error(
                                response.value.body()?.error
                            )
                        )
                }
            }
        }
    }

    fun sendDateNightOffer(sendOfferDateNight: SendOfferDateNight) {

        runIO {

            when (val response = datingJourneyRepository.sendDateNightOffer(
                startTime = sendOfferDateNight.startTime ?: "",
                startDate = sendOfferDateNight.startDate ?: "",
                endTime = sendOfferDateNight.endTime ?: "",
                endDate = sendOfferDateNight.endDate ?: "",
                description = sendOfferDateNight.description ?: "",
                destination = sendOfferDateNight.destination ?: "",
                url = sendOfferDateNight.url ?: "",
                title = sendOfferDateNight.title ?: "",
                dateWeekId = sendOfferDateNight.dateWeekId ?: 0,
                inventoryTopic = sendOfferDateNight.inventoryTopic ?: "",
                groupId = sendOfferDateNight.groupId,
                partnerUserId = sendOfferDateNight.partnerUserId

            )) {
                is ResultWrapper.GenericError -> response.error
                is ResultWrapper.Success -> {
                    if (response.value.isSuccessful) {

                        if (response.value.body()!!.status == "ok")
                            _sendDateNightOffer.postValue(
                                UIEventSendDateNightOffer.Data(
                                    response.value.body()!!
                                )
                            )
                        else
                            _sendDateNightOffer.postValue(
                                UIEventSendDateNightOffer.Error(
                                    response.value.errorBody().toString()
                                )
                            )


                    }
                }
            }


        }

    }


    fun getDateNightCatalogOfferResponseLiveData(): LiveData<UIEventSendDateNightOffer> {
        return _sendDateNightOffer
    }

}