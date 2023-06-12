package com.tovalue.me.ui.whatworkguide.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tovalue.me.model.whatworksguide.VoteResponse
import com.tovalue.me.network.ResultWrapper
import com.tovalue.me.ui.whatworkguide.WhatWorksGuideRepository
import com.tovalue.me.util.extensions.runIO
import com.tovalue.me.util.livedata.Event

class VoteViewModel : ViewModel() {

    private val repo = WhatWorksGuideRepository()

    private val _sendVoteResponse = MutableLiveData<Event<EventVote>>()
    val sendVoteResponse: LiveData<Event<EventVote>> = _sendVoteResponse


    fun sendVote(guideId:Int,vote:Int) {
        runIO {
            when (val response = repo.sendVote(guideId,vote)) {
                is ResultWrapper.GenericError -> _sendVoteResponse.postValue(
                    Event(
                        EventVote.Error(
                            response.error
                        )
                    )
                )
                is ResultWrapper.Success -> {
                    if (response.value.isSuccessful && response.value.body()!!.status == "ok")
                        _sendVoteResponse.postValue(Event(EventVote.voteData(response.value.body()!!)))
                    else
                        _sendVoteResponse.postValue(Event(EventVote.Error(response.value.body()?.error ?:"")))
                }
            }
        }
    }

    sealed class EventVote {
        data class voteData(val response: VoteResponse): EventVote()
        data class Error(val errorMsg: String): EventVote()
    }
}