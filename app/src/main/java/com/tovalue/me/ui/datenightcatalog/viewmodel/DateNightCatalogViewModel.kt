package com.tovalue.me.ui.datenightcatalog.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tovalue.me.model.DateNightOfferResponse
import com.tovalue.me.model.SendOfferDateNight
import com.tovalue.me.model.datenightcatalog.*
import com.tovalue.me.model.datenightcatalog.DateNightCatalogDetailResponse
import com.tovalue.me.model.datenightcatalog.HolderData

import com.tovalue.me.network.ResultWrapper
import com.tovalue.me.ui.auth.AuthRepository
import com.tovalue.me.ui.dashboard.DashboardViewModel
import com.tovalue.me.ui.dashboard.home.HomeViewModel
import com.tovalue.me.ui.datenightcatalog.DateNightCatalogRepository
import com.tovalue.me.util.extensions.runIO
import com.tovalue.me.util.livedata.Event

class DateNightCatalogViewModel : ViewModel() {

    private val dataNightCatalogRepository = DateNightCatalogRepository()
    private val authRepo = AuthRepository()
    /*get userid*/
    fun getUserId() = authRepo.getUserId()

    fun getUserObj()=authRepo.getUserObj()

    /* get DateNight Catalog */
    private val _dateNightCatalogData = MutableLiveData<UiEventDateNightCatalogData>()
    sealed class UiEventDateNightCatalogData {
        data class Error(val errorMsg: String?) : UiEventDateNightCatalogData()
        data class Data(val data: HolderData.DateNightCatalogResponse) :
            UiEventDateNightCatalogData()
    }


    /* get DateNight Catalog Detail */
    private val _dateNightCatalogDetail = MutableLiveData<UiEventDateNightCatalogDataDetail>()

    sealed class UiEventDateNightCatalogDataDetail {
        data class Error(val errorMsg: String?) : UiEventDateNightCatalogDataDetail()
        data class Data(val data: DateNightCatalogDetailResponse) :
            UiEventDateNightCatalogDataDetail()
    }

    /* send DateNight Offer */
    private val _sendDateNightOffer = MutableLiveData<UIEventSendDateNightOffer>()

    sealed class UIEventSendDateNightOffer {
        data class Error(val errorMsg: String?) : UIEventSendDateNightOffer()
        data class Data(val data: DateNightOfferResponse) : UIEventSendDateNightOffer()
    }

    /* Get DateNight Idea */
    private val _dateNightIdeaListResponse = MutableLiveData<Event<UIEventDateNightIdea>>()

    sealed class UIEventDateNightIdea {
        data class Error(val errorMsg: String?) : UIEventDateNightIdea()
        data class Data(val data: HolderData.DateNightCatalogResponse?) : UIEventDateNightIdea()
    }

    val dateNightIdeaListResponse: LiveData<Event<UIEventDateNightIdea>> =
        _dateNightIdeaListResponse

    /* Get DateNight Idea Details*/
    private val _dateNightIdeaDetailResponse = MutableLiveData<Event<UIEventDateNightIdeaDetails>>()

    sealed class UIEventDateNightIdeaDetails {
        data class Error(val errorMsg: String?) : UIEventDateNightIdeaDetails()
        data class Data(val data: DateNightCatalogDetailResponse?) : UIEventDateNightIdeaDetails()
    }

    val dateNightIdeaDetailResponse: LiveData<Event<UIEventDateNightIdeaDetails>> =
        _dateNightIdeaDetailResponse


    fun subscriptionType() = authRepo.getSubscriptionType()
    fun activeSubscription() = authRepo.isSubscriptionActive()

    fun getDateNightCatalog(partnerUserId: Int, groupId: Int) {

        runIO {

            when (val response = dataNightCatalogRepository.getDataNightCatalog(
                partnerUserId = partnerUserId,
                groupId
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


    /* get All Promoted Event  */
    private val _getAllPromotedData = MutableLiveData<UiEventAllPromotedData>()
    val getAllPromotedData: LiveData<UiEventAllPromotedData>
        get() = _getAllPromotedData

    sealed class UiEventAllPromotedData {
        data class Error(val errorMsg: String?) : UiEventAllPromotedData()
        data class Data(val data: HolderData.PromotedEventResponse) : UiEventAllPromotedData()
    }


    fun getAllPromotedEvent(partnerUserId: Int, groupId: Int) {

        runIO {
            when (val response = dataNightCatalogRepository.getPromotedEvent(
                partnerUserId = partnerUserId,
                groupId
            )) {

                is ResultWrapper.GenericError -> {
                    response.error
                }
                is ResultWrapper.Success -> {
                    if (response.value.isSuccessful) {

                        if (response.value.body()!!.status.lowercase() == "ok")
                            _getAllPromotedData.postValue(UiEventAllPromotedData.Data(response.value.body()!!))
                        else
                            _getAllPromotedData.postValue(
                                UiEventAllPromotedData.Error(
                                    response.value.errorBody().toString()
                                )
                            )


                    }
                }
            }
        }

    }


    /*************** Get Date Night catalog Detail **********************/
    fun getDateNightCatalogDetail(dateNightId: String) {

        runIO {

            when (val response =
                dataNightCatalogRepository.getDateNightCatalogDetail(dateNightId)) {
                is ResultWrapper.GenericError -> response.error
                is ResultWrapper.Success -> {
                    if (response.value.isSuccessful) {

                        if (response.value.body()!!.status == "ok")
                            _dateNightCatalogDetail.postValue(
                                UiEventDateNightCatalogDataDetail.Data(
                                    response.value.body()!!
                                )
                            )
                        else
                            _dateNightCatalogDetail.postValue(
                                UiEventDateNightCatalogDataDetail.Error(
                                    response.value.errorBody().toString()
                                )
                            )


                    }
                }
            }
        }
    }

    fun getDateNightCatalogDetailLiveData(): LiveData<UiEventDateNightCatalogDataDetail> {
        return _dateNightCatalogDetail
    }


    /******************************** Send Date night offer ****************************/
    fun sendDateNightOffer(sendOfferDateNight: SendOfferDateNight) {

        runIO {


            when (val response = dataNightCatalogRepository.sendDateNightOffer(
                startTime = sendOfferDateNight.startTime ?: "",
                endTime = sendOfferDateNight.endTime ?: "",
                description = sendOfferDateNight.description ?: "",
                dateNightId = sendOfferDateNight.dateNightId ?: "",
                dateWeekId = (sendOfferDateNight.dateWeekId ?:0).toString(),
                inventoryTopic = sendOfferDateNight.inventoryTopic ?: "",
                groupId = sendOfferDateNight.groupId,
                partnerUserId = sendOfferDateNight.partnerUserId,
                title = "This is Title ",
                event_address = sendOfferDateNight.event_address,
                event_end_date = sendOfferDateNight.event_end_date,
                event_start_date = sendOfferDateNight.event_start_date,
                url = sendOfferDateNight.url,
                eventImage = sendOfferDateNight.eventImg!!
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


    fun getDateNightIdeaDetails(dateNightId: Int, isPartner: Int) {


        runIO {
            when (val response = dataNightCatalogRepository.getDateNightIdeaDetails(
                dateNightId = dateNightId,


                isPartner = isPartner
            )) {

                is ResultWrapper.GenericError -> {
                    response.error
                }
                is ResultWrapper.Success -> {
                    if (response.value.isSuccessful) {

                        if (response.value.body()?.status?.lowercase() == "ok")
                            _dateNightIdeaDetailResponse.postValue(
                                Event(
                                    UIEventDateNightIdeaDetails.Data(
                                        response.value.body()
                                    )
                                )
                            )
                        else
                            _dateNightIdeaDetailResponse.postValue(
                                Event(
                                    UIEventDateNightIdeaDetails.Error(
                                        response.value.errorBody().toString()
                                    )
                                )
                            )
                    }
                }
            }
        }
    }


    fun getDateNightIdeas(page: Int, groupId: Int, userId: Int) {



        runIO {



            when (val response = dataNightCatalogRepository.getDateNightIdeas(
                page = page,
                groupId = groupId,
                userId = userId
            )) {
                is ResultWrapper.GenericError -> {
                    Log.e("FinderLogData", "getDateNightIdeas: ${response.error}")
                    response.error
                }
                is ResultWrapper.Success -> {
                    if (response.value.isSuccessful) {
                        Log.e("FinderLogData", "getDateNightIdeas: " + response.value.body())
                        if (response.value.body()?.status?.lowercase() == "ok")
                            _dateNightIdeaListResponse.postValue(
                                Event(
                                    UIEventDateNightIdea.Data(
                                        response.value.body()
                                    )
                                )
                            )
                        else
                            _dateNightIdeaListResponse.postValue(
                                Event(
                                    UIEventDateNightIdea.Error(
                                        response.value.body()?.error
                                    )
                                )
                            )
                    }
                }
            }
        }
    }


}