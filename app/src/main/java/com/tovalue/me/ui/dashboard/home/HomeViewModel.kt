package com.tovalue.me.ui.dashboard.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.tovalue.me.model.DateNightOffer

import com.tovalue.me.model.datenightcatalog.HolderData
import com.tovalue.me.model.datingJourney.DatingResponse
import com.tovalue.me.network.ResultWrapper
import com.tovalue.me.ui.auth.AuthViewModel
import com.tovalue.me.ui.dashboard.DashboardRepository
import com.tovalue.me.ui.datenightcatalog.viewmodel.DateNightCatalogViewModel
import com.tovalue.me.util.SingleLiveEvent
import com.tovalue.me.util.extensions.runIO
import okhttp3.MultipartBody


class HomeViewModel(savedState: SavedStateHandle) : ViewModel() {

    private val dashBoardRepo = DashboardRepository()


    /* get DateNight Catalog */
    private val _dateNightCatalogData = MutableLiveData<UiEventDateNightCatalogData>()
    sealed class UiEventDateNightCatalogData {
        data class Error(val errorMsg: String?) : UiEventDateNightCatalogData()
        data class Data(val data:  HolderData.DateNightCatalogResponse) : UiEventDateNightCatalogData()
    }

//    get DataJourneys
    private val _dataJourneys = MutableLiveData<UiEventDateJourneys>()
    val dataJourney:LiveData<UiEventDateJourneys>  = _dataJourneys


    sealed class  UiEventDateJourneys{
        data class Error(val errorMsg: String?):UiEventDateJourneys()
        data class Data(val data: DatingResponse):UiEventDateJourneys()
    }

    //offer date night
    private val _dateNightOffer = SingleLiveEvent<DateNightOfferEvent>()
    val dateNightOffer: LiveData<DateNightOfferEvent> = _dateNightOffer

    sealed interface DateNightOfferEvent {
        data class Error(val errorMsg: String?) : DateNightOfferEvent
        data class DataResponse(val response: DateNightOffer) : DateNightOfferEvent
    }


    fun getDatingJourney() {

        runIO {
            when(val response = dashBoardRepo.getDatingJourney()){


                is ResultWrapper.GenericError ->{

                    response.error
                }
                is ResultWrapper.Success ->{

                    if(response.value.isSuccessful){

                        if(response.value.body()!!.status == "ok")
                         _dataJourneys.postValue(UiEventDateJourneys.Data(response.value.body()!!))
                        else
                            _dataJourneys.postValue(UiEventDateJourneys.Error(response.value.errorBody().toString()))

                    }
                }

            }

        }

    }


    fun getDateNightCatalog(partnerUserId:Int,groupId:Int) {

        runIO {

            when (val response = dashBoardRepo.getDataNightCatalog(
                partnerUserId = partnerUserId,
                groupId = groupId
            )) {

                is ResultWrapper.GenericError -> {
                    response.error
                }
                is ResultWrapper.Success -> {
                    if (response.value.isSuccessful) {

                        if (response.value.body()!!.status?.lowercase() == "ok")
                            _dateNightCatalogData.postValue(
                                UiEventDateNightCatalogData.Data(
                                    response.value.body()!!
                                )
                            )
                        else
                            _dateNightCatalogData.postValue(
                                UiEventDateNightCatalogData.Error(
                                    response.value.errorBody().toString()
                                )
                            )

                    }
                }
            }
        }
    }


    fun getDateNightCatalogLiveData(): LiveData<UiEventDateNightCatalogData> {
        return _dateNightCatalogData
    }


    fun createOfferDateNight(
        eventDescription: String,
        dateNightId: Int,
        dateWeekId: Int,
        inventoryTopic: String,
        partnerId: Int,
        title: String,
        startDate: String,
        startTime: String,
        endDate: String,
        endTime: String,
        eventAddress: String,
        groupId: Int,
        url: String,
        attachment: MultipartBody.Part
    ) {
        runIO {
            when (val response = dashBoardRepo.createOfferDateNight(
                eventDescription = eventDescription,
                dateNightId = dateNightId,
                dateWeekId = dateWeekId,
                inventoryTopic = inventoryTopic,
                partnerId = partnerId,
                title = title,
                startDate = startDate,
                startTime = startTime,
                endDate = endDate,
                endTime = endTime,
                eventAddress = eventAddress,
                groupId = groupId,
                url = url,
                attachment=attachment
            )) {
                is ResultWrapper.GenericError -> {
                    _dateNightOffer.postValue(DateNightOfferEvent.Error(response.error))
                }
                is ResultWrapper.Success -> {
                    if (response.value.isSuccessful && response.value.body()!!.status == "ok") {

                        _dateNightOffer.postValue(DateNightOfferEvent.DataResponse(response.value.body()!!))
                    } else
                        _dateNightOffer.postValue(
                            DateNightOfferEvent.Error(
                                response.value.errorBody().toString()
                            )
                        )
                }
            }
        }
    }


}