package com.tovalue.me.ui.whatworkguide.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tovalue.me.model.whatworksguide.WhatWorksGuideRespose
import com.tovalue.me.network.ResultWrapper
import com.tovalue.me.ui.whatworkguide.WhatWorksGuideRepository
import com.tovalue.me.util.extensions.runIO
import com.tovalue.me.util.livedata.Event

class WhatWorksGuideViewModel : ViewModel() {

    private val repo = WhatWorksGuideRepository()

    private val _worksGuideResponse = MutableLiveData<Event<EventWhatWordsGuide>>()
    val worksGuideResponse: LiveData<Event<EventWhatWordsGuide>> = _worksGuideResponse


    fun getWhatWorksData() {
        runIO {
            when (val response = repo.getWhatWorksGuide()) {
                is ResultWrapper.GenericError -> _worksGuideResponse.postValue(
                    Event(
                        EventWhatWordsGuide.Error(
                            response.error
                        )
                    )
                )
                is ResultWrapper.Success -> {
                    if (response.value.isSuccessful && response.value.body()!!.status == "ok")
                        _worksGuideResponse.postValue(Event(EventWhatWordsGuide.GuideData(response.value.body()!!)))
                    else
                        _worksGuideResponse.postValue(Event(EventWhatWordsGuide.Error(response.value.body()?.error ?:"")))
                }
            }
        }
    }

    sealed class EventWhatWordsGuide {
        data class GuideData(val response: WhatWorksGuideRespose): EventWhatWordsGuide()
        data class Error(val errorMsg: String): EventWhatWordsGuide()
    }
}