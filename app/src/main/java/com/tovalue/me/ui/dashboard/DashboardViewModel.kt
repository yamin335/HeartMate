package com.tovalue.me.ui.dashboard

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tovalue.me.model.*
import com.tovalue.me.model.moodRingModels.MoodRingHistory
import com.tovalue.me.model.moodRingModels.MoodRingHistoryDetail
import com.tovalue.me.model.moodRingModels.MoodRingStoreResponse
import com.tovalue.me.model.notification.UnreadCountModel
import com.tovalue.me.network.ResultWrapper
import com.tovalue.me.ui.auth.AuthRepository
import com.tovalue.me.ui.auth.AuthViewModel
import com.tovalue.me.util.SingleLiveEvent
import com.tovalue.me.util.Utils
import com.tovalue.me.util.extensions.runIO
import com.tovalue.me.util.livedata.Event
import kotlinx.coroutines.CompletableDeferred

class DashboardViewModel : ViewModel() {
    private val repo = DashboardRepository()
    private val authRepo = AuthRepository()

    val moodRingStoreResponse: MutableLiveData<MoodRingStoreResponse> by lazy {
        MutableLiveData<MoodRingStoreResponse>()
    }

    private val _state = MutableLiveData<Event<UiEventAccount>>()
    val state: LiveData<Event<UiEventAccount>> = _state

    private val _profileResponse = MutableLiveData<Event<UiEventProfile>>()
    val profileResponse: LiveData<Event<UiEventProfile>> = _profileResponse

    private val _corporateInfoResponse = MutableLiveData<Event<UiEventCorporate>>()
    val corporateInfoResponse: LiveData<Event<UiEventCorporate>> = _corporateInfoResponse

    // user data info update state
    private val _updateState = SingleLiveEvent<UiEventUpdate>()
    val updateState = SingleLiveEvent<UiEventUpdate>()

    private val _unreadNotificationCountResponse =
        MutableLiveData<Event<UiEventUnreadNotificationCount>>()
    val unreadNotificationCountResponse: LiveData<Event<UiEventUnreadNotificationCount>> =
        _unreadNotificationCountResponse

    private val _moodHistory = MutableLiveData<List<MoodRingHistory>>()
    val moodHistory: LiveData<List<MoodRingHistory>> = _moodHistory

    private val _historyDetailResponse = MutableLiveData<Event<UiEventHistoryDetails>>()
    val historyDetailResponse: LiveData<Event<UiEventHistoryDetails>> = _historyDetailResponse

    private val _memberShipState = SingleLiveEvent<MemberShipResponseEvent>()
    val memberShipState: LiveData<MemberShipResponseEvent> = _memberShipState

    private val _myPlanResponse = MutableLiveData<Event<UiEventMyRelationshipPlan>>()
    val myPlanResponse: LiveData<Event<UiEventMyRelationshipPlan>> = _myPlanResponse

    private val _myPlanDetailResponse = MutableLiveData<Event<UiEventMyRelationshipPlanDetails>>()
    val myPlanDetailResponse: LiveData<Event<UiEventMyRelationshipPlanDetails>> = _myPlanDetailResponse

    private val _relationshipPlanResponse = MutableLiveData<Event<UiEventRelationshipPlan>>()
    val relationshipPlanResponse: LiveData<Event<UiEventRelationshipPlan>> = _relationshipPlanResponse

    private val _planUpdateResponse = MutableLiveData<Event<UiEventDefaultResponse>>()
    val planUpdateResponse: LiveData<Event<UiEventDefaultResponse>> = _planUpdateResponse

    fun getCorporateInfo() {
        runIO {
            when (val response = repo.getCorporateLinks()) {
                is ResultWrapper.GenericError -> _corporateInfoResponse.postValue(
                    Event(
                        UiEventCorporate.Error(
                            response.error
                        )
                    )
                )
                is ResultWrapper.Success -> {
                    if (response.value.isSuccessful && response.value.body()!!.status == "ok") {
                        _corporateInfoResponse.postValue(
                            Event(
                                UiEventCorporate.CorporateLinks(
                                    response.value.body()!!
                                )
                            )
                        )
                    } else {
                        if (response.value.body()!!.error.contains("generate_auth_cookie"))
                            _corporateInfoResponse.postValue(Event(UiEventCorporate.COOKIEXPIRE))
                        else
                            _corporateInfoResponse.postValue(Event(UiEventCorporate.Error(response.value.body()!!.error)))
                    }
                }
            }
        }
    }


    fun verifyEmail(firstName: String, lastName: String, email: String, displayName: String) {
        runIO {
            when (val response = authRepo.emailExists(email)) {
                is ResultWrapper.GenericError -> _updateState.postValue(
                    UiEventUpdate.Error(
                        response.error
                    )
                )
                is ResultWrapper.Success -> {
                    if (response.value.isSuccessful && response.value.body()!!.status == "ok") {
                        updateState.postValue(UiEventUpdate.Update)
                        updateUser(
                            firstName,
                            lastName,
                            email,
                            displayName
                        )
                    } else {
                        if (response.value.body()!!.error.contains("generate_auth_cookie"))
                            updateState.postValue(UiEventUpdate.COOKIEXPIRE)
                        else
                            updateState.postValue(
                                UiEventUpdate.Error(
                                    response.value.body()?.error ?: ""
                                )
                            )
                    }
                }
            }
        }
    }

    fun updateUser(firstName: String, lastName: String, email: String, displayName: String) {
        runIO {

            when (val response = authRepo.updateUser(
                firstName,
                lastName,
                email,
                displayName
            )) {
                is ResultWrapper.GenericError -> _updateState.postValue(
                    UiEventUpdate.Error(
                        response.error
                    )
                )
                is ResultWrapper.Success -> {
                    if (response.value.isSuccessful && response.value.body()!!.status == "ok") {
                        _updateState.postValue(UiEventUpdate.Update)
                    } else {
                        if (response.value.body()!!.error.contains("generate_auth_cookie"))
                            _updateState.postValue(UiEventUpdate.COOKIEXPIRE)
                        else
                            _updateState.postValue(
                                UiEventUpdate.Error(
                                    response.value.body()?.error ?: ""
                                )
                            )
                    }
                }

            }
        }
    }

    fun getProfileInfo(userId: Int) {
        runIO {
            Log.i("TAG-->", "getRandomString:" + Utils.getRandomStr())
            when (val response = repo.getProfile(Utils.getRandomStr(),userId)) {
                is ResultWrapper.GenericError -> _profileResponse.postValue(
                    Event(
                        UiEventProfile.Error(
                            response.error
                        )
                    )
                )
                is ResultWrapper.Success -> {
                    if (response.value.isSuccessful && response.value.body()!!.status == "ok") {
						authRepo.setSpectrumMusicUrl(response.value.body()!!.spectrum.audio_file)
                        repo.setUserObj(response.value.body()!!)
                        repo.setUserMetaValues(response.value.body()!!) // [@refactor]
                        _profileResponse.postValue(Event(UiEventProfile.Profile(response.value.body()!!)))
                    } else {
                        if (response.value.body()!!.error.contains("generate_auth_cookie"))
                            _profileResponse.postValue(Event(UiEventProfile.COOKIEXPIRE))
                        else
                            _profileResponse.postValue(Event(UiEventProfile.Error(response.value.body()!!.error)))
                    }
                }
            }
        }
    }

    fun saveMoodRing(
        emotionally: Int, mentally: Int,
        physically: Int, communally: Int,
        professionally: Int, spiritually: Int,
        emotionally_explanation: String,
        mentally_explanation: String,
        spiritually_explanation: String,
        communally_explanation: String,
        physically_explanation: String,
        professionally_explanation: String
    ) {
        runIO {
            when (val response = repo.saveMoodRing(
                emotionally, mentally,
                physically, communally,
                professionally, spiritually,
                emotionally_explanation,
                mentally_explanation,
                spiritually_explanation,
                communally_explanation,
                physically_explanation,
                professionally_explanation
            )) {
                is ResultWrapper.GenericError -> {
                    print("Error saving mood details!")
                }
                is ResultWrapper.Success -> {
                    if (response.value.isSuccessful && response.value.body()?.status == "ok") {
                        moodRingStoreResponse.postValue(response.value.body())
                    }
                }
            }
        }
    }

    fun getMoodRingHistory(forWeekEnding: String) {
        runIO {
            when (val response =
                repo.getMoodRingHistory(pageNumber = 1, forWeekEnding = forWeekEnding)) {
                is ResultWrapper.GenericError -> {
                    print("Error loading history!")
                }
                is ResultWrapper.Success -> {
                    if (response.value.isSuccessful && response.value.body()?.status == "ok") {
                        _moodHistory.postValue(response.value.body()?.entries ?: ArrayList())
                    } else {
                        _moodHistory.postValue(ArrayList())
                    }
                }
            }
        }
    }

	fun getMoodRingHistoryDetails(moodRingId: Int) {
		runIO {
			when (val response = repo.getMoodRingHistoryDetails(moodRingId = moodRingId)) {
				is ResultWrapper.GenericError -> _historyDetailResponse.postValue(
					Event(UiEventHistoryDetails.Error(
						response.error
					))
				)
				is ResultWrapper.Success -> {
					if (response.value.isSuccessful && response.value.body()?.status == "ok" && response.value.body()?.entry != null) {
						_historyDetailResponse.postValue(Event(UiEventHistoryDetails.HistoryDetails(response.value.body()?.entry!!)))
					} else {
						if (response.value.body()?.error?.contains("generate_auth_cookie") == true)
							_historyDetailResponse.postValue(Event(UiEventHistoryDetails.COOKIEXPIRE))
						else
							_historyDetailResponse.postValue(Event(UiEventHistoryDetails.Error(response.value.body()?.error ?: "Unexpected server error occurred!")))
					}
				}
			}
		}
	}
	
	private suspend fun getDeleteNonce() : String {
		val nonce = CompletableDeferred<String>()
		runIO {
			when(val response = repo.getNonce()) {
				is ResultWrapper.GenericError -> {}
				is ResultWrapper.Success -> {
					nonce.complete(response.value.body()!!.nonce)
				}
			}
		}
		return nonce.await()
	}
	
	fun deactivateAccount() {
		runIO {
			val result =  getDeleteNonce()
			terminateAccount(result)
		}
	}
	
	fun terminateAccount(nonceResult: String) {
		runIO {
			when (val response = repo.deactivateAccount(nonceResult)) {
				is ResultWrapper.GenericError -> _state.postValue(
					Event(
						UiEventAccount.Error(
							response.error
						)
					)
				)
				is ResultWrapper.Success -> {
					if (response.value.isSuccessful && response.value.body()!!.deleted)
						_state.postValue(Event(UiEventAccount.Exit(AppExitType.DEACTIVATE)))
					else
						_state.postValue(Event(UiEventAccount.Error(response.value.body()!!.deleted.toString())))
				}
			}
		}
	}

	fun logOut() {
		runIO {
			when (val response = repo.logout(authRepo.getToken())) {
				is ResultWrapper.GenericError -> _state.postValue(
					Event(
						UiEventAccount.Error(
							response.error
						)
					)
				)
				is ResultWrapper.Success -> {
					if (response.value.isSuccessful && response.value.body()!!.status == "ok")
						_state.postValue(Event(UiEventAccount.Exit(AppExitType.LOGOUT)))
					else
						_state.postValue(Event(UiEventAccount.Error(response.value.body()!!.error)))
				}
			}
		}
	}

    fun getUnreadNotificationCount() {
        runIO {
            when (val response = repo.getUnreadNotificationCount()) {
                is ResultWrapper.GenericError -> _unreadNotificationCountResponse.postValue(
                    Event(
                        UiEventUnreadNotificationCount.Error(
                            response.error
                        )
                    )
                )
                is ResultWrapper.Success -> {
                    if (response.value.isSuccessful && response.value.body()!!.status == "ok")
                        _unreadNotificationCountResponse.postValue(
                            Event(
                                UiEventUnreadNotificationCount.UiEventUnreadCount(response.value.body()!!)
                            )
                        )
                    else
                        _unreadNotificationCountResponse.postValue(
                            Event(
                                UiEventUnreadNotificationCount.Error(
                                    response.value.body()?.error ?: ""
                                )
                            )
                        )
                }
            }
        }
    }

    fun getMyRelationshipPlans() {
        runIO {
            when (val response = repo.getMyRelationshipPlans()) {
                is ResultWrapper.GenericError -> _myPlanResponse.postValue(
                    Event(
                        UiEventMyRelationshipPlan.Error(
                            response.error
                        )
                    )
                )
                is ResultWrapper.Success -> {
                    if (response.value.isSuccessful && response.value.body()?.status == "ok") {
                        _myPlanResponse.postValue(
                            Event(
                                UiEventMyRelationshipPlan.MyRelationshipPlanData(
                                    response.value.body()!!
                                )
                            )
                        )
                    } else {
                        if (response.value.body()?.error?.contains("generate_auth_cookie") == true)
                            _myPlanResponse.postValue(Event(UiEventMyRelationshipPlan.COOKIEXPIRE))
                        else
                            _myPlanResponse.postValue(Event(UiEventMyRelationshipPlan.Error(response.value.body()?.error ?: "Unexpected error occured!")))
                    }
                }
            }
        }
    }

    fun getMyRelationshipPlanDetails(planId: Int?) {
        runIO {
            when (val response = repo.getMyRelationshipPlanDetails(planId)) {
                is ResultWrapper.GenericError -> _myPlanDetailResponse.postValue(
                    Event(
                        UiEventMyRelationshipPlanDetails.Error(
                            response.error
                        )
                    )
                )
                is ResultWrapper.Success -> {
                    if (response.value.isSuccessful && response.value.body()?.status == "ok") {
                        _myPlanDetailResponse.postValue(
                            Event(
                                UiEventMyRelationshipPlanDetails.MyRelationshipPlanDetailsData(
                                    response.value.body()!!
                                )
                            )
                        )
                    } else {
                        if (response.value.body()?.error?.contains("generate_auth_cookie") == true)
                            _myPlanDetailResponse.postValue(Event(UiEventMyRelationshipPlanDetails.COOKIEXPIRE))
                        else
                            _myPlanDetailResponse.postValue(Event(UiEventMyRelationshipPlanDetails.Error(response.value.body()?.error ?: "Unexpected error occured!")))
                    }
                }
            }
        }
    }

    fun updateMyRelationshipPlanProgress(planId: Int?, taskIds: String?) {
        runIO {
            when (val response = repo.updateMyRelationshipPlanProgress(planId, taskIds)) {
                is ResultWrapper.GenericError -> _planUpdateResponse.postValue(
                    Event(
                        UiEventDefaultResponse.Error(
                            response.error
                        )
                    )
                )
                is ResultWrapper.Success -> {
                    if (response.value.isSuccessful && response.value.body()?.status == "ok") {
                        _planUpdateResponse.postValue(
                            Event(
                                UiEventDefaultResponse.DefaultResponseData(
                                    response.value.body()!!
                                )
                            )
                        )
                    } else {
                        if (response.value.body()?.error?.contains("generate_auth_cookie") == true)
                            _planUpdateResponse.postValue(Event(UiEventDefaultResponse.COOKIEXPIRE))
                        else
                            _planUpdateResponse.postValue(Event(UiEventDefaultResponse.Error(response.value.body()?.error ?: "Unexpected error occured!")))
                    }
                }
            }
        }
    }

    fun getAvailableRelationshipPlans() {
        runIO {
            when (val response = repo.getAvailableRelationshipPlans()) {
                is ResultWrapper.GenericError -> _relationshipPlanResponse.postValue(
                    Event(
                        UiEventRelationshipPlan.Error(
                            response.error
                        )
                    )
                )
                is ResultWrapper.Success -> {
                    if (response.value.isSuccessful && response.value.body()?.status == "ok") {
                        _relationshipPlanResponse.postValue(
                            Event(
                                UiEventRelationshipPlan.RelationshipPlanData(
                                    response.value.body()!!
                                )
                            )
                        )
                    } else {
                        if (response.value.body()?.error?.contains("generate_auth_cookie") == true)
                            _relationshipPlanResponse.postValue(Event(UiEventRelationshipPlan.COOKIEXPIRE))
                        else
                            _relationshipPlanResponse.postValue(Event(UiEventRelationshipPlan.Error(response.value.body()?.error ?: "Unexpected error occured!")))
                    }
                }
            }
        }
    }

	fun sendToken() {
		runIO {
			when (val response = repo.sendDeviceToken(getDeviceToken())) {
				is ResultWrapper.GenericError -> {
					Log.d("Firbase", "error")
				}
				is ResultWrapper.Success -> {
					if (response.value.isSuccessful && response.value.body()!!.status == "ok") {
						authRepo.setFreshTokenNeeded(true)
						Log.d("Firebase", "device_token: true")
					}
				}
			}
		}
	}

    /*[@Refactor] meta values module | need separate object rather standalone values*/
    // api call could be cancel if scope finished [@refactor]
    fun updateSubscriptionStatus(
        subscriptionStatus: String,
        subscriptionType: String,
        subscriptionExpiryDate: String,
        subscriptionToken:String
    ) {
        runIO {
            when (val response = repo.updateSubscriptionStatus(
                subscriptionStatus = subscriptionStatus,
                subscriptionType = subscriptionType,
                subscriptionExpiryDate = subscriptionExpiryDate,
                subscriptionToken=subscriptionToken
            )) {
                is ResultWrapper.GenericError -> {
                    _memberShipState.postValue(
                        MemberShipResponseEvent.Error(
                            response.error
                        )
                    )
                }
                is ResultWrapper.Success -> {
                    if (response.value.isSuccessful && response.value.body()!!.status == "ok") {
                        _memberShipState.postValue(
                            MemberShipResponseEvent.DataResponse(
                                response.value.body()!!
                            )
                        )
                    } else
                        _memberShipState.postValue(
                            MemberShipResponseEvent.Error(
                                response.value.body()?.error
                            )
                        )
                }
            }
        }
    }



	
    fun flushSavedData() {
        repo.flushData()
    }

//	fun onStateChangedAction(uiAction: UiAction) {
//		when(uiAction) {
//			is UiAction.Navigation -> RedirectApiCall(uiAction.navigation, uiAction.metaValues)
//		}
//	}
//
//	private val _navigationScreen = MutableLiveData<UiAction>()
//	val navigationScreen: LiveData<UiAction> = _navigationScreen
//	private fun RedirectApiCall(navigation: CurrentNavigationScreen, metaValues: UpdatMetaKeys) {
//		_navigationScreen.postValue(UiAction.Navigation(navigation, metaValues))
//	}

	fun subscriptionType() = repo.getSubscriptionType()
	fun activeSubscription() = repo.isSubscriptionActive()

    fun getUserId(): Int = authRepo.getUserId()
    fun getUserObj(): ProfileInfo = repo.getUserObj()!!
	fun isFreshTokenNeeded() = authRepo.isFreshTokenNeeded()
	fun getDeviceToken() = authRepo.getDeviceToken()

    // invitation acceptance group id
    fun getGroupId(): Int = authRepo.getGroupId()
	// DateNightView
	fun getInvitationRedirection() = authRepo.getInvitationRedirection()
	fun removeInvitationRedirection() = authRepo.removeInvitationRedirection()

    sealed class UiEventAccount {
        data class Error(val errorMsg: String?) : UiEventAccount()
        data class Exit(val exitType: AppExitType) : UiEventAccount()
    }

    sealed class UiEventProfile {
        data class Profile(val response: ProfileInfo) : UiEventProfile()
        data class Error(val errorMsg: String) : UiEventProfile()
        object COOKIEXPIRE : UiEventProfile()
    }

    sealed class UiEventHistoryDetails {
        data class HistoryDetails(val response: MoodRingHistoryDetail) : UiEventHistoryDetails()
        data class Error(val errorMsg: String) : UiEventHistoryDetails()
        object COOKIEXPIRE : UiEventHistoryDetails()
    }

    sealed class UiEventCorporate {
        data class CorporateLinks(val response: CorporateInfoResponse) : UiEventCorporate()
        data class Error(val errorMsg: String) : UiEventCorporate()
        object COOKIEXPIRE : UiEventCorporate()
    }


    sealed class UiEventUpdate {
        object Update : UiEventUpdate()
        data class Error(val errorMsg: String) : UiEventUpdate()
        object COOKIEXPIRE : UiEventUpdate()
    }

    sealed class UiEventUnreadNotificationCount {
        data class UiEventUnreadCount(val response: UnreadCountModel) :
            UiEventUnreadNotificationCount()

        data class Error(val errorMsg: String) : UiEventUnreadNotificationCount()
    }

    sealed interface MemberShipResponseEvent {
        data class Error(val errorMsg: String?) : MemberShipResponseEvent
        data class DataResponse(val response: Generic) :
            MemberShipResponseEvent
    }
    sealed class UiEventMyRelationshipPlan {
        data class MyRelationshipPlanData(val response: MyRelationshipPlanResponse) : UiEventMyRelationshipPlan()
        data class Error(val errorMsg: String) : UiEventMyRelationshipPlan()
        object COOKIEXPIRE : UiEventMyRelationshipPlan()
    }

    sealed class UiEventMyRelationshipPlanDetails {
        data class MyRelationshipPlanDetailsData(val response: MyRelationshipPlanDetailsResponse) : UiEventMyRelationshipPlanDetails()
        data class Error(val errorMsg: String) : UiEventMyRelationshipPlanDetails()
        object COOKIEXPIRE : UiEventMyRelationshipPlanDetails()
    }

    sealed class UiEventRelationshipPlan {
        data class RelationshipPlanData(val response: RelationshipPlanResponse) : UiEventRelationshipPlan()
        data class Error(val errorMsg: String) : UiEventRelationshipPlan()
        object COOKIEXPIRE : UiEventRelationshipPlan()
    }

    sealed class UiEventDefaultResponse {
        data class DefaultResponseData(val response: DefaultApiResponse) : UiEventDefaultResponse()
        data class Error(val errorMsg: String) : UiEventDefaultResponse()
        object COOKIEXPIRE : UiEventDefaultResponse()
    }

    //	sealed class UiAction {
//		data class Navigation(
//			val navigation: CurrentNavigationScreen,
//			val metaValues: UpdateMetaKeys
//		) : UiAction()
//	}
//
    enum class AppExitType {
        LOGOUT,
        DEACTIVATE
    }
//
//	enum class CurrentNavigationScreen {
//		EMAIL_SETTING,
//		PUSH_SETTING,
//		FILTER_SETTING,
//		PURCHASE
//	}
}