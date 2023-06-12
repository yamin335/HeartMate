package com.tovalue.me.ui.visionboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tovalue.me.network.ResultWrapper
import com.tovalue.me.ui.auth.AuthRepository
import com.tovalue.me.ui.dashboard.DashboardRepository
import com.tovalue.me.ui.visionboard.response.IceBreakerResponseModel
import com.tovalue.me.ui.visionboard.response.PartnerVisionBoardModel
import com.tovalue.me.ui.visionboard.response.ResponseModel
import com.tovalue.me.ui.visionboard.response.VisionBoardModel
import com.tovalue.me.util.extensions.runIO
import com.tovalue.me.util.livedata.Event

class VisionBoardViewModel : ViewModel() {
	private val boardRepo = VisionBoardRepository()
	private val dashboardRepo = DashboardRepository()
	
	// HUD Screen
	private val _boardState = MutableLiveData<VisionBoardState>()
	val boardState: LiveData<VisionBoardState> = _boardState
	
	// internalTabState -> 4 tabs data need to be completed to view partner board
	private val _internalTabState = MutableLiveData<Event<InternalTabEvent>>()
	val internalTabState: LiveData<Event<InternalTabEvent>> = _internalTabState
	
	// Partner Vision Board
	private val _partnerBoard = MutableLiveData<PartnerBoardState>()
	val partnerBoard: LiveData<PartnerBoardState> = _partnerBoard
	
	// IceBreaker
	private val _iceBreaker = MutableLiveData<IceBreakerState>()
	val iceBreaker: LiveData<IceBreakerState> = _iceBreaker
	
	// shareable data fun
	private val _sharedData = MutableLiveData<ResponseModel>()
	val sharedData: LiveData<ResponseModel> = _sharedData
	
	fun setClickedDataForObserve(response: ResponseModel) {
		_sharedData.value = response
	}
	
	fun getVisionBoardDetails() {
		runIO {
			when(val response = boardRepo.visionBoardDetail()) {
				is ResultWrapper.GenericError -> _boardState.postValue(VisionBoardState.Error(response.error))
				is ResultWrapper.Success -> {
					if (response.value.isSuccessful && response.value.body()?.status == "ok") {
						_boardState.postValue(VisionBoardState.Data(response.value.body()!!))
					} else {
						_boardState.postValue(VisionBoardState.Error(response.value.errorBody()?.string()))
					}
				}
			}
		}
	}
	
	fun submitMindsetData(msg: String) {
		runIO {
			when(val response = boardRepo.mindsetData(msg)) {
				is ResultWrapper.GenericError -> _internalTabState.postValue(Event(InternalTabEvent.Error(response.error)))
				is ResultWrapper.Success -> {
					if (response.value.isSuccessful && response.value.body()?.status == "ok") {
						_internalTabState.postValue(Event(InternalTabEvent.RedirectToNextScreen))
					} else {
						_internalTabState.postValue(Event(InternalTabEvent.Error(response.value.errorBody()?.string())))
					}
				}
			}
		}
	}
	
	fun submitSeasonData(groupOneMsg: String, groupTwoMsg: String) {
		runIO {
			when(val response = boardRepo.adultSeasonData(groupOneMsg, groupTwoMsg)) {
				is ResultWrapper.GenericError -> _internalTabState.postValue(Event(InternalTabEvent.Error(response.error)))
				is ResultWrapper.Success -> {
					if (response.value.isSuccessful && response.value.body()?.status == "ok") {
						_internalTabState.postValue(Event(InternalTabEvent.RedirectToNextScreen))
					} else {
						_internalTabState.postValue(Event(InternalTabEvent.Error(response.value.errorBody()?.string())))
					}
				}
			}
		}
	}
	
	fun submitBlissData(groupOneMsg: String, groupTwoMsg: String) {
		runIO {
			when(val response = boardRepo.blissData(groupOneMsg, groupTwoMsg)) {
				is ResultWrapper.GenericError -> _internalTabState.postValue(Event(InternalTabEvent.Error(response.error)))
				is ResultWrapper.Success -> {
					if (response.value.isSuccessful && response.value.body()?.status == "ok") {
						_internalTabState.postValue(Event(InternalTabEvent.RedirectToNextScreen))
					} else {
						_internalTabState.postValue(Event(InternalTabEvent.Error(response.value.errorBody()?.string())))
					}
				}
			}
		}
	}
	
	fun submitDatingFormData(msg1: String, msg2: String, msg3: String) {
		runIO {
			when(val response = boardRepo.dateStyleData(msg1, msg2, msg3)) {
				is ResultWrapper.GenericError -> _internalTabState.postValue(Event(InternalTabEvent.Error(response.error)))
				is ResultWrapper.Success -> {
					if (response.value.isSuccessful && response.value.body()?.status == "ok") {
						_internalTabState.postValue(Event(InternalTabEvent.RedirectToNextScreen))
					} else {
						_internalTabState.postValue(Event(InternalTabEvent.Error(response.value.errorBody()?.string())))
					}
				}
			}
		}
	}
	
	fun getPartnerVisionBoard() {
		runIO {
			when(val response = boardRepo.getPartnerVisionBoard()) {
				is ResultWrapper.GenericError -> _partnerBoard.postValue(PartnerBoardState.Error(response.error))
				is ResultWrapper.Success -> {
					if (response.value.isSuccessful && response.value.body()?.status == "ok") {
						_partnerBoard.postValue(PartnerBoardState.Data(response.value.body()!!))
					} else {
						_partnerBoard.postValue(PartnerBoardState.Error(response.value.errorBody()?.string()))
					}
				}
			}
		}
	}
	
	fun getIceBreakerBoard() {
		runIO {
			when(val response = boardRepo.getIceBreakerBoard()) {
				is ResultWrapper.GenericError -> _iceBreaker.postValue(IceBreakerState.Error(response.error))
				is ResultWrapper.Success -> {
					if (response.value.isSuccessful && response.value.body()?.status == "ok") {
						_iceBreaker.postValue(IceBreakerState.Data(response.value.body()!!))
					} else {
						_iceBreaker.postValue(IceBreakerState.Error(response.value.errorBody()?.string()))
					}
				}
			}
		}
	}
	
	fun getName() = dashboardRepo.getUserObj()?.displayname
	fun iceBreakerBackgroundImage() = dashboardRepo.getUserObj()?.iceBreakers?.backgroundImage
	
	sealed interface VisionBoardState {
		data class Data(val responseObj: VisionBoardModel): VisionBoardState
		data class Error(val errorMsg: String?): VisionBoardState
	}
	
	sealed interface InternalTabEvent {
		object RedirectToNextScreen: InternalTabEvent
		data class Error(val errorMsg: String?): InternalTabEvent
	}
	
	sealed interface PartnerBoardState {
		data class Data(val partnerObj: PartnerVisionBoardModel): PartnerBoardState
		data class Error(val errorMsg: String?): PartnerBoardState
	}
	
	sealed interface IceBreakerState {
		data class Data(val iceBreakerObj: IceBreakerResponseModel): IceBreakerState
		data class Error(val errorMsg: String?): IceBreakerState
	}
}