package com.tovalue.me.ui.whatworkguide.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tovalue.me.model.whatworksguide.WhatWorksGuideDataRespose
import com.tovalue.me.model.whatworksguide.WhatWorksGuideRespose
import com.tovalue.me.network.ResultWrapper
import com.tovalue.me.ui.whatworkguide.WhatWorksGuideRepository
import com.tovalue.me.util.extensions.runIO
import com.tovalue.me.util.livedata.Event

class WhatWorksGuideDataViewModel : ViewModel() {

    private val repo = WhatWorksGuideRepository()

    private val _worksGuideDataResponse = MutableLiveData<Event<EventWhatWordsGuideData>>()
    val worksGuideDataResponse: LiveData<Event<EventWhatWordsGuideData>> = _worksGuideDataResponse


    fun getWhatWorksData(guideId: Int) {
        runIO {
            when (val response = repo.getWhatWorksGuideData(guideId)) {
                is ResultWrapper.GenericError -> _worksGuideDataResponse.postValue(
                    Event(
                        EventWhatWordsGuideData.Error(
                            response.error
                        )
                    )
                )
                is ResultWrapper.Success -> {
                    if (response.value.isSuccessful && response.value.body()!!.status == "ok")
                        _worksGuideDataResponse.postValue(Event(EventWhatWordsGuideData.GuideData(
                            response.value.body()!!
                        )))
                    else
                        _worksGuideDataResponse.postValue(Event(EventWhatWordsGuideData.Error(response.value.body()?.error ?:"")))
                }
            }
        }
    }

    sealed class EventWhatWordsGuideData {
        data class GuideData(val response: WhatWorksGuideDataRespose): EventWhatWordsGuideData()
        data class Error(val errorMsg: String): EventWhatWordsGuideData()
    }
}