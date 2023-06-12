package com.tovalue.me.ui.auth

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.tovalue.me.helper.MomensityBingoApp
import com.tovalue.me.model.Oauth
import com.tovalue.me.model.CorporateInfoResponse
import com.tovalue.me.model.TvmReport
import com.tovalue.me.network.ResultWrapper
import com.tovalue.me.ui.auth.primeriii.InventorySide
import com.tovalue.me.ui.auth.primeriii.SideCategory
import com.tovalue.me.ui.auth.primeriii.spectrum.SpectrumResponse
import com.tovalue.me.ui.dashboard.DashboardViewModel
import com.tovalue.me.util.Constants.ACCEPT_REJECT_INVITATION
import com.tovalue.me.util.Constants.AVAILABILITY_STAGE
import com.tovalue.me.util.Constants.BIRTHDAY_STAGE
import com.tovalue.me.util.Constants.CONTROLLER_NONCE
import com.tovalue.me.util.Constants.EMAIL_STAGE
import com.tovalue.me.util.Constants.FACET_STAGE
import com.tovalue.me.util.Constants.GUIDE_STAGE
import com.tovalue.me.util.Constants.INVENTORY_STAGE
import com.tovalue.me.util.Constants.KEY
import com.tovalue.me.util.Constants.LEVEL_ONE_INVITATION_STAGE
import com.tovalue.me.util.Constants.LEVEL_ONE_SCORE_STAGE
import com.tovalue.me.util.Constants.LOCATION_STAGE
import com.tovalue.me.util.Constants.METHOD_NONCE_REGISTER
import com.tovalue.me.util.Constants.NOTIFICATION_STAGE
import com.tovalue.me.util.Constants.Name_STAGE
import com.tovalue.me.util.Constants.PASSWORD_STAGE
import com.tovalue.me.util.Constants.PHOTO_STAGE
import com.tovalue.me.util.Constants.PRIMERIII_STAGE
import com.tovalue.me.util.Constants.PRIMERII_STAGE
import com.tovalue.me.util.Constants.PRIMERIV_STAGE
import com.tovalue.me.util.Constants.PRIMERI_STAGE
import com.tovalue.me.util.Constants.PRIMERVI_STAGE
import com.tovalue.me.util.Constants.PRIMERV_STAGE
import com.tovalue.me.util.Constants.PRIMERX_STAGE
import com.tovalue.me.util.Constants.TVMNUMBER_STAGE
import com.tovalue.me.util.Constants.UNLOCK_INVITATION
import com.tovalue.me.util.extensions.runIO
import com.tovalue.me.util.extensions.savedStateValueHandler
import com.tovalue.me.util.livedata.Event
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

const val MOBILE_NUMBER = "mobileNumber"
const val DIAL_CODE = "dialCode"
const val STAGE = "stage"
const val GROUP_KEY = "group_key"

class AuthViewModel(savedState: SavedStateHandle) : ViewModel() {
    private val authRepo = AuthRepository()

    private val _onBoardingVideo = MutableLiveData<UiEventOnBoardingResponse>()
    val onBoardingResponse: LiveData<UiEventOnBoardingResponse> = _onBoardingVideo

    private val _state = MutableLiveData<Event<UiEvent>>()
    val state: LiveData<Event<UiEvent>> = _state

    private val _otpState = MutableLiveData<Event<UiEventOtp>>()
    val otpState: LiveData<Event<UiEventOtp>> = _otpState

    // [ Resume screen from where user left while registration ]
    private val _screenEvent = MutableLiveData<Event<UiEventStage>>()
    val screenEvent: LiveData<Event<UiEventStage>> = _screenEvent

    // user data info update state
    private val _updateState = MutableLiveData<UiEvent>()
    val updateState: LiveData<UiEvent> = _updateState

    // notification state
    private val _notificationState = MutableLiveData<UiEvent>()
    val notificationState: LiveData<UiEvent> = _notificationState

    private val _updateDatingPreference = MutableLiveData<UiEvent>()
    val updateDatingPreference: LiveData<UiEvent> = _updateDatingPreference

    // location state
    private val _locationState = MutableLiveData<Event<UiEvent>>()
    val locationState: LiveData<Event<UiEvent>> = _locationState

    private val _corporateInfoResponse = MutableLiveData<Event<AuthViewModel.UiEventCorporate>>()
    val corporateInfoResponse: LiveData<Event<AuthViewModel.UiEventCorporate>> = _corporateInfoResponse

    /*Inventory Module State [Start]*/
    // inventory sideCount
    private val _sidePosition = MutableLiveData<Int>()
    val sidePosition: LiveData<Int> = _sidePosition

    // get sides
    private val _inventory = MutableLiveData<InventorySide>()
    val inventory: LiveData<InventorySide> = _inventory

    // get questions based on group key
    private val _question = MutableLiveData<Event<UiEventQuestionLoad>>()
    val question: LiveData<Event<UiEventQuestionLoad>> = _question

    private val _answer = MutableLiveData<Event<UiEventQuestionaire>>()
    val answer: LiveData<Event<UiEventQuestionaire>> = _answer

    // inventory of my life from profile screen
    private val _categoryState = MutableLiveData<Event<UiEventCategory>>()
    val cateogryState: LiveData<Event<UiEventCategory>> = _categoryState

    // initialize category object to share it for other fragments
    private val _inventoryData = MutableLiveData<SideCategory>()
    val inventoryData: LiveData<SideCategory> = _inventoryData
    fun setInventoryCategoryData(category: SideCategory) {
        _inventoryData.value = category
    }
    
    var UserTotalLifeScore = 0
    /*	if registration on then this check will be true and if not then false*/
    // To enable back button and other adjustment of Ui according to this check
    var isEditMode: Boolean = true
    /*Inventory Module [End]*/

    // [Invitation] different flow for registration and invitation while onboarding user
    private val _invitatonState = MutableLiveData<UiInvitation>()
    val invitationState: LiveData<UiInvitation> = _invitatonState

    // tvm Report [ To Value Me Report ]
    private val _reportState = MutableLiveData<Event<UiEventTvmReport>>()
    val reportState: LiveData<Event<UiEventTvmReport>> = _reportState

    // guide state [DateNight Catalog]
    private val _guideState = MutableLiveData<UiEventTvmReport>()
    val guideState: LiveData<UiEventTvmReport> = _guideState

    // Great Job State
    private val _jobState = MutableLiveData<SpectrumResponse>()
    val jobState: LiveData<SpectrumResponse> = _jobState
    // inventory data
    private val _inventoryResult = MutableLiveData<SpectrumResponse>()
    val inventoryResult: LiveData<SpectrumResponse> = _inventoryResult
    
    fun setInventoryData(inventoryData: SpectrumResponse) {
        _inventoryResult.value = inventoryData
    }

    var number by savedStateValueHandler<String>(handle = savedState, key = MOBILE_NUMBER)
    var countryCode by savedStateValueHandler<String>(handle = savedState, key = DIAL_CODE)
    var categoryKey by savedStateValueHandler<Int>(handle = savedState, key = GROUP_KEY)

    init {
        when (MomensityBingoApp.preferencesManager!!.getStringValue(STAGE)) {
            PRIMERI_STAGE -> _screenEvent.postValue(
                Event(
                    UiEventStage.ResumeScreenOnSatrt(
                        ScreenStage.PRIMERI
                    )
                )
            )
            PRIMERII_STAGE -> _screenEvent.postValue(
                Event(
                    UiEventStage.ResumeScreenOnSatrt(
                        ScreenStage.PRIMERII
                    )
                )
            )
            PRIMERIII_STAGE -> _screenEvent.postValue(
                Event(
                    UiEventStage.ResumeScreenOnSatrt(
                        ScreenStage.PRIMERIII
                    )
                )
            )
            PRIMERIV_STAGE -> _screenEvent.postValue(
                Event(
                    UiEventStage.ResumeScreenOnSatrt(
                        ScreenStage.PRIMERIV
                    )
                )
            )
            PRIMERV_STAGE -> _screenEvent.postValue(
                Event(
                    UiEventStage.ResumeScreenOnSatrt(
                        ScreenStage.PRIMERV
                    )
                )
            )
            PRIMERVI_STAGE -> _screenEvent.postValue(Event(UiEventStage.ResumeScreenOnSatrt(ScreenStage.PRIMERVI)))
            PRIMERX_STAGE -> _screenEvent.postValue(Event(UiEventStage.ResumeScreenOnSatrt(ScreenStage.PRIMERX)))
            Name_STAGE -> _screenEvent.postValue(Event(UiEventStage.ResumeScreenOnSatrt(ScreenStage.NAME)))
            EMAIL_STAGE -> _screenEvent.postValue(Event(UiEventStage.ResumeScreenOnSatrt(ScreenStage.EMAIL)))
            PASSWORD_STAGE -> _screenEvent.postValue(
                Event(
                    UiEventStage.ResumeScreenOnSatrt(
                        ScreenStage.PASSWORD
                    )
                )
            )
            BIRTHDAY_STAGE -> _screenEvent.postValue(
                Event(
                    UiEventStage.ResumeScreenOnSatrt(
                        ScreenStage.BIRTHDAY
                    )
                )
            )
            NOTIFICATION_STAGE -> _screenEvent.postValue(
                Event(
                    UiEventStage.ResumeScreenOnSatrt(
                        ScreenStage.NOTIFICATION
                    )
                )
            )
            PHOTO_STAGE -> _screenEvent.postValue(Event(UiEventStage.ResumeScreenOnSatrt(ScreenStage.PHOTO)))
            LOCATION_STAGE -> _screenEvent.postValue(
                Event(
                    UiEventStage.ResumeScreenOnSatrt(
                        ScreenStage.LOCATION
                    )
                )
            )
            AVAILABILITY_STAGE -> _screenEvent.postValue(
                Event(
                    UiEventStage.ResumeScreenOnSatrt(
                        ScreenStage.AVAILABILITY
                    )
                )
            )
            INVENTORY_STAGE -> _screenEvent.postValue(
                Event(
                    UiEventStage.ResumeScreenOnSatrt(
                        ScreenStage.INVENTORY
                    )
                )
            )
            FACET_STAGE -> _screenEvent.postValue(Event(UiEventStage.ResumeScreenOnSatrt(ScreenStage.FACET)))
            GUIDE_STAGE -> _screenEvent.postValue(Event(UiEventStage.ResumeScreenOnSatrt(ScreenStage.GUIDE)))
            TVMNUMBER_STAGE -> _screenEvent.postValue(
                Event(
                    UiEventStage.ResumeScreenOnSatrt(
                        ScreenStage.TVMNUMBER
                    )
                )
            )
            LEVEL_ONE_SCORE_STAGE -> _screenEvent.postValue(
                Event(
                    UiEventStage.ResumeScreenOnSatrt(
                        ScreenStage.LOS
                    )
                )
            )
            LEVEL_ONE_INVITATION_STAGE -> _screenEvent.postValue(
                Event(
                    UiEventStage.ResumeScreenOnSatrt(
                        ScreenStage.LOI
                    )
                )
            )
            ACCEPT_REJECT_INVITATION -> _screenEvent.postValue(
                Event(
                    UiEventStage.ResumeScreenOnSatrt(
                        ScreenStage.INVITATION
                    )
                )
            )
            UNLOCK_INVITATION -> _screenEvent.postValue(
                Event(
                    UiEventStage.ResumeScreenOnSatrt(
                        ScreenStage.UNLOCK
                    )
                )
            )
        }
    }

//	private suspend fun verifyPhoneNumber(username: String): Oauth {
//		val obj = CompletableDeferred<Oauth>()
//		runIO {
//			when (val response = authRepo.verifyNumber(username)) {
//				is ResultWrapper.GenericError -> error(response.error)
//				is ResultWrapper.Success -> {
//					if (response.value.isSuccessful) {
//						if (response.value.body()!!.status == "ok")
//							obj.complete(response.value.body()!!)
//						else
//							obj.complete(response.value.body()!!)
//					}
//				}
//			}
//		}
//		return obj.await()
//	}

    private suspend fun getCookie(username: String): Oauth {
        val obj = CompletableDeferred<Oauth>()
        runIO {
            when (val response = authRepo.getCookie(username)) {
                is ResultWrapper.GenericError -> error(response.error)
                is ResultWrapper.Success -> {
                    if (response.value.isSuccessful) {
                        if (response.value.body()!!.status == "ok")
                            obj.complete(response.value.body()!!)
                        else
                            obj.complete(response.value.body()!!)
                    }
                }
            }
        }
        return obj.await()
    }

//	private suspend fun getCookie(username: String, nonce: String): Oauth {
//		val obj = CompletableDeferred<Oauth>()
//		runIO {
//			when (val response = authRepo.getCookie(username, nonce)) {
//				is ResultWrapper.GenericError -> error(response.error)
//				is ResultWrapper.Success -> {
//					if (response.value.isSuccessful) {
//						if (response.value.body()!!.status == "ok")
//							obj.complete(response.value.body()!!)
//						else
//							obj.complete(response.value.body()!!)
//					}
//				}
//			}
//		}
//		return obj.await()
//	}

    private suspend fun getNonce(controllerName: String, controllerMethod: String): String {
        val nonce = CompletableDeferred<String>()
        runIO {
            when (val response = authRepo.getNonce(controllerName, controllerMethod)) {
                is ResultWrapper.GenericError -> error(response.error)
                is ResultWrapper.Success -> {
                    if (response.value.isSuccessful && response.value.body()!!.status == "ok")
                        nonce.complete(response.value.body()!!.nonce)
                }
            }
        }
        return nonce.await()
    }

    sealed class UiEventCorporate {
        data class CorporateLinks(val response: CorporateInfoResponse) : UiEventCorporate()
        data class Error(val errorMsg: String) : UiEventCorporate()
        object COOKIEXPIRE : UiEventCorporate()
    }

    // TODO refactor this functon 3 apis nonce, register, generate_aut_cookie
    fun register(
        email: String = "",
        password: String = "",
        username: String = "",
        key: String = KEY
    ) {
        runIO {
            val loginResponse: Oauth = getCookie(countryCode.plus(number))
            if (loginResponse.status == "error") {
                _otpState.postValue(Event(UiEventOtp.Error(loginResponse.error)))
            } else if (loginResponse.jwt != null) {
                saveUserData(
                    loginResponse.jwt.trim(),
                    loginResponse.userId,
                    loginResponse.inventoryId
                ) // replaced 492 with loginResponse.userInventoryId when api fixed
                authRepo.setUserName(username)
                if (loginResponse.invitationCode.isNotEmpty()) {
                    // save response and flush when invitee complete registration
                    saveLoginResponse(loginResponse)
                }
                // adjust the logic for invitee | logged in user there
                // if invitation code is empty it means no invitation there
                // if cookie is empty it means it's fresh user proceed it with normal registration flow
                // if invitation code is not empty it mean this is invitee user

                _otpState.postValue(Event(UiEventOtp.Data(loginResponse)))
            } else {
                val nonceResult = getNonce(CONTROLLER_NONCE, METHOD_NONCE_REGISTER)
                when (val response = authRepo.createUser(
                    email, password, username,
                    key, nonceResult
                )) {
                    is ResultWrapper.GenericError -> _otpState.postValue(
                        Event(
                            UiEventOtp.Error(
                                response.error
                            )
                        )
                    )
                    is ResultWrapper.Success -> {
                        if (response.value.isSuccessful) {
                            if (response.value.body()!!.status == "ok") {
                                authRepo.setUserName(username)
                                saveUserData(
                                    response.value.body()!!.jwt.trim(),
                                    response.value.body()!!.userId,
                                    response.value.body()!!.userInventoryId
                                )
                                bookMarkProgress(PRIMERI_STAGE)
                                _otpState.postValue(Event(UiEventOtp.GotoPrimerScreen))
                            } else {
                                _otpState.postValue(Event(UiEventOtp.Error(response.value.body()!!.error)))
                            }
                        } else {
                            _otpState.postValue(
                                Event(
                                    UiEventOtp.Error(
                                        response.value.errorBody().toString()
                                    )
                                )
                            )
                        }
                    }
                }
            }
        }
    }

    fun updateUser() {
        val fullName = authRepo.getSavedName()
        val firstName = fullName.substringBefore(" ")
        val lastName = fullName.substringAfter(" ")
        val email = authRepo.getSavedEmail()
        runIO {
            when (val response = authRepo.updateUser(
                firstName,
                lastName,
                email,
                lastName
            )) {
                is ResultWrapper.GenericError -> _updateState.postValue(UiEvent.Error(response.error))
                is ResultWrapper.Success -> {
                    if (response.value.isSuccessful) {
                        if (response.value.body()!!.status == "ok") {
                            _state.postValue(Event(UiEvent.GotoNextScreen))
                        } else {
                            if (response.value.body()!!.error.contains("generate_auth_cookie")) {
                                val response: Oauth = getCookie(authRepo.getUserName())
                                if (response.jwt != null) {
                                    authRepo.saveToken(response.jwt)
                                    updateUser()
                                } else _state.postValue(Event(UiEvent.Error(response.error)))
                            } else {
                                _state.postValue(Event(UiEvent.Error(response.value.body()!!.error)))
                            }
                        }
                    }
                }
            }
        }
    }

    fun updateNotificationSetting(enable: Boolean) {
        runIO {
            when (val response =
                authRepo.updateNotificationSetting(enable, authRepo.getBirthDate())) {
                is ResultWrapper.GenericError -> _notificationState.postValue(UiEvent.Error(response.error))
                is ResultWrapper.Success -> {
                    if (response.value.isSuccessful) {
                        if (response.value.body()!!.status == "ok")
                            _notificationState.postValue(UiEvent.GotoNextScreen)
                        else {
                            if (response.value.body()!!.error.contains("generate_auth_cookie")) {
                                val response: Oauth = getCookie(authRepo.getUserName())
                                if (response.jwt != null) {
                                    authRepo.saveToken(response.jwt)
                                    updateNotificationSetting(enable)
                                } else _notificationState.postValue(UiEvent.Error(response.error))
                            } else {
                                _notificationState.postValue(UiEvent.Error(response.value.body()!!.error))
                            }
                        }
                    }
                }
            }
        }
    }

    fun updateDatingPreference(
        maxDistance: String,
        location: String,
        dating_availability: String
    ) {
        Log.i("TAG-->", "username: ${authRepo.getUserName()}")
        runIO {
            when (val response =
                authRepo.updateDatingPreference(maxDistance, location, dating_availability)) {
                is ResultWrapper.GenericError -> _updateDatingPreference.postValue(
                    UiEvent.Error(
                        response.error
                    )
                )
                is ResultWrapper.Success -> {
                    if (response.value.isSuccessful) {
                        if (response.value.body()!!.status == "ok")
                            _updateDatingPreference.postValue(UiEvent.GotoNextScreen)
                        else {
                            if (response.value.body()!!.error.contains("generate_auth_cookie")) {
                                val response: Oauth = getCookie(authRepo.getUserName())
                                if (response.jwt != null) {
                                    //	authRepo.saveUserCookie(response.jwt)
                                    //updateNotificationSetting(enable)
                                } else _updateDatingPreference.postValue(UiEvent.Error(response.error))
                            } else {
                                _updateDatingPreference.postValue(UiEvent.Error(response.value.body()!!.error))
                            }
                        }
                    }
                }
            }
        }
    }

    // LBA ~= birthdate Availability
    fun updateLBA(datingAvailability: String?) {
        runIO {
            when (val response = authRepo.updateAvailability(
                datingAvailability
            )) {
                is ResultWrapper.GenericError -> _locationState.postValue(
                    Event(
                        UiEvent.Error(
                            response.error
                        )
                    )
                )
                is ResultWrapper.Success -> {
                    if (response.value.isSuccessful) {
                        if (response.value.body()!!.status == "ok")
                            _locationState.postValue(Event(UiEvent.GotoNextScreen))
                        else
                            _locationState.postValue(Event(UiEvent.Error(response.value.body()!!.error)))
                    }
                }
            }
        }
    }

    fun getInventorySides() {
        runIO {
            when (val response = authRepo.getInventoryData()) {
                is ResultWrapper.GenericError -> response.error
                is ResultWrapper.Success -> {
                    if (response.value.isSuccessful) {
                        if (response.value.body()!!.status == "ok")
                            _inventory.postValue(response.value.body())
                        else
                            _inventory.postValue(response.value.body())
                    }
                }
            }
        }
    }

    fun getQuestionsViaGroupKey(categoryKey: Int) {
        runIO {
            when (val response = authRepo.getInventoryQuestion(categoryKey)) {
                is ResultWrapper.GenericError -> _question.postValue(
                    Event(
                        UiEventQuestionLoad.Error(
                            response.error
                        )
                    )
                )
                is ResultWrapper.Success -> {
                    if (response.value.isSuccessful) {
                        if (response.value.body()!!.status == "ok")
                            _question.postValue(Event(UiEventQuestionLoad.Inventory(response.value.body()!!)))
                        else
                            _question.postValue(
                                Event(
                                    UiEventQuestionLoad.Error(
                                        response.value.errorBody().toString()
                                    )
                                )
                            )
                    }
                }
            }
        }
    }

    fun verifyEmail(email: String) {
        runIO {
            when (val response = authRepo.emailExists(email)) {
                is ResultWrapper.GenericError -> error(response.error)
                is ResultWrapper.Success -> {
                    if (response.value.isSuccessful) {
                        if (response.value.body()!!.status == "ok") {
                            saveEmail(email)
                            updateUser()
                        } else {
                            _state.postValue(Event(UiEvent.Error(response.value.body()!!.error)))
                        }
                    } else {
                        _state.postValue(
                            Event(
                                UiEvent.Error(
                                    response.value.errorBody().toString()
                                )
                            )
                        )
                    }
                }
            }
        }
    }

    fun updateInventoryValues(categoryId: Int, serializedJson: String, title: String) {
        runIO {
            when (val response =
                authRepo.updateInventoryValues(categoryId, serializedJson, title)) {
                is ResultWrapper.GenericError -> error(response.error)
                is ResultWrapper.Success -> {
                    if (response.value.isSuccessful) {
                        setSpectrumData(response.value.body()!!)
                        _answer.postValue(Event(UiEventQuestionaire.RedirectToSideScreen))
                    } else {
                        _answer.postValue(
                            Event(
                                UiEventQuestionaire.Error(
                                    response.value.errorBody().toString()
                                )
                            )
                        )
                    }
                }
            }
        }
    }

    fun getPersonalityFacts() {
        runIO {
            when (val response = authRepo.getPersonalityFacts()) {
                is ResultWrapper.GenericError -> _reportState.postValue(
                    Event(
                        UiEventTvmReport.Error(
                            response.error
                        )
                    )
                )
                is ResultWrapper.Success -> {
                    if (response.value.isSuccessful) {
                        if (response.value.body()!!.status == "ok")
                            _reportState.postValue(Event(UiEventTvmReport.Report(response.value.body()!!)))
                        else
                            _reportState.postValue(
                                Event(
                                    UiEventTvmReport.Error(
                                        response.value.errorBody().toString()
                                    )
                                )
                            )
                    }
                }
            }
        }
    }

    fun getDayNightCataLog() {
        runIO {
            when (val response = authRepo.getDayNightCateLog()) {
                is ResultWrapper.GenericError -> _guideState.postValue(
                    UiEventTvmReport.Error(
                        response.error
                    )
                )
                is ResultWrapper.Success -> {
                    if (response.value.isSuccessful) {
                        if (response.value.body()!!.status == "ok")
                            _guideState.postValue(UiEventTvmReport.Report(response.value.body()!!))
                        else
                            _guideState.postValue(
                                UiEventTvmReport.Error(
                                    response.value.errorBody().toString()
                                )
                            )
                    }
                }
            }
        }
    }

    fun levelOneInvitation(type: String) {
        runIO {
            when (val response = if (type == "accept") authRepo.getLoginData()?.let {
                authRepo.acceptLevelOneInvitation(
                    it.invitationCode,
                    authRepo.getUserId()
                )
            } else authRepo.getLoginData()?.let {
                authRepo.declineLevelOneInvitation(
                    it.invitationCode,
                    authRepo.getUserId()
                )
            }) {
                is ResultWrapper.GenericError -> _invitatonState.postValue(
                    UiInvitation.Error(
                        response.error
                    )
                )
                is ResultWrapper.Success -> {
                    if (response.value.isSuccessful && response.value.body()!!.status == "ok") {
                        // save group id after invitation acceptance
                        if (type == "accept") authRepo.setGroupId(response.value.body()!!.group_id)
                        _invitatonState.postValue(UiInvitation.GoToNextScreen)
                    } else {
                        _invitatonState.postValue(UiInvitation.Error(response.value.body()!!.error))
                    }
                }
            }
        }
    }
    
    fun updateLocation(lat: Double, lng: Double, location: String) {
        runIO {
            when (val response = authRepo.updateUserLocation(lat, lng, location)) {
                is ResultWrapper.GenericError -> {
                    _locationState.postValue(Event(UiEvent.Error(response.error)))
                }
                is ResultWrapper.Success -> {
                    if (response.value.isSuccessful) {
                        if (response.value.body()!!.status == "ok")
                            _locationState.postValue(Event(UiEvent.GotoNextScreen))
                        else
                            _locationState.postValue(Event(UiEvent.Error(response.value.body()!!.error)))
                    }
                }
            }
        }
    }

    fun onUiAction(ui: UiAction) {
        when (ui) {
            is UiAction.ItemClicked -> setCategoryClick(ui.position)
        }
    }

    private fun setCategoryClick(position: Int) {
        _categoryState.postValue(Event(UiEventCategory.CategoryPosition(position)))
    }

    //	Side page count [side ~= inventory page] [Refactor]
    fun setSidePosition(position: Int) {
        _sidePosition.value = position
    }

    fun getSidePosition(): Int = _sidePosition.value!!.toInt()

    // set spectrum music | data for great job screen
    // set value from main thread or pass this data to view and then set function from view
    private suspend fun setSpectrumData(spectrumData: SpectrumResponse) {
        withContext(Dispatchers.Main) {
            _jobState.value = spectrumData
        }
    }

    fun setBookMarkProgress(stageValue: String) {
        runIO { bookMarkProgress(stageValue) }
    }

    fun saveName(name: String) {
        runIO { authRepo.saveName(name) }
    }

    fun getName(): String = authRepo.getSavedName()

    private fun bookMarkProgress(stage: String) {
        authRepo.saveRegistrationProgress(stage)
    }

    private fun saveUserData(token: String, id: Int, lifeInventoryId: Int) {
        authRepo.saveToken(token)
        authRepo.saveUserId(id)
        authRepo.saveInventoryId(lifeInventoryId)
    }

    fun saveEmail(email: String) {
        runIO { authRepo.saveEmail(email) }
    }

    fun saveBirthDate(date: String) {
        runIO { authRepo.saveBirthDate(date) }
    }

    fun setUserLoggedIn(isEnable: Boolean) {
        runIO {
            authRepo.setUserLoggedIn(isEnable)
        }
    }
    fun isUserLoggedIn() = authRepo.isUserLoggedIn()
    fun setSpectrumMusicUrl(musicUrl: String) = runIO { authRepo.setSpectrumMusicUrl(musicUrl) }
    fun getSpectrumDemoUrl(): String = authRepo.getSpectrumMusicUrl()
    fun getAvatarForSpectrum(): String = authRepo.getAvatar()
    fun saveLoginResponse(loginResponse: Oauth) = authRepo.setLoginData(loginResponse)
    fun getLoginResponse(): Oauth? = authRepo.getLoginData()
    fun redirectToInvitation(enabled: Boolean) = runIO {authRepo.redirectToInvitationLaunch(enabled)}

    fun getOnBoardingVideo() {
        runIO {
            when (val response = authRepo.getOnBoardingVideo()) {


                is ResultWrapper.GenericError -> _onBoardingVideo.postValue(
                    UiEventOnBoardingResponse.Error(
                        response.error.toString()
                    )
                )
                is ResultWrapper.Success -> {
                    if (response.value.isSuccessful) {

                        if (response.value.body()!!.status == "ok") {
                            authRepo.setSpectrumMusicUrl(response.value.body()!!.spectrumDemo)
                            _onBoardingVideo.postValue(
                                UiEventOnBoardingResponse.Data(
                                    response.value.body()!!
                                )
                            )
                        } else {
                            _onBoardingVideo.postValue(
                                UiEventOnBoardingResponse.Error(
                                    response.value.errorBody().toString()
                                )
                            )
                        }


                    }
                }

            }
        }
    }

    fun getCorporateInfo() {
        runIO {
            when (val response = authRepo.getCorporateLinks()) {
                is ResultWrapper.GenericError -> _corporateInfoResponse.postValue(
                    Event(
                        AuthViewModel.UiEventCorporate.Error(
                            response.error
                        )
                    )
                )
                is ResultWrapper.Success -> {
                    if (response.value.isSuccessful && response.value.body()!!.status == "ok") {
                        _corporateInfoResponse.postValue(
                            Event(
                                AuthViewModel.UiEventCorporate.CorporateLinks(
                                    response.value.body()!!
                                )
                            )
                        )
                    } else {
                        if (response.value.body()!!.error.contains("generate_auth_cookie"))
                            _corporateInfoResponse.postValue(Event(AuthViewModel.UiEventCorporate.COOKIEXPIRE))
                        else
                            _corporateInfoResponse.postValue(Event(AuthViewModel.UiEventCorporate.Error(response.value.body()!!.error)))
                    }
                }
            }
        }
    }


    fun setDeviceToken(devieToken: String) = runIO { authRepo.setDeviceToken(devieToken) }
    fun setFreshTokenNeeded(isRequired: Boolean) = runIO { authRepo.setFreshTokenNeeded(isRequired) }
    
    /*PhoneAuth Event | Email Exist*/
    sealed class UiEvent {
        object GotoNextScreen : UiEvent()
        data class Error(val errorMsg: String?) : UiEvent()
    }

    /*OTP Event*/
    sealed class UiEventOtp {
        object GotoPrimerScreen : UiEventOtp()
        data class Data(val userRecordStatus: Oauth) : UiEventOtp()
        data class Error(val errorMsg: String?) : UiEventOtp()
    }

    /*Landing page Event*/
    sealed class UiEventStage {
        data class ResumeScreenOnSatrt(val stage: ScreenStage) : UiEventStage()
    }

    // update question values Event
    sealed class UiEventQuestionaire {
        object RedirectToSideScreen : UiEventQuestionaire()
        data class Error(val errorMsg: String?) : UiEventQuestionaire()
    }

    sealed class UiEventQuestionLoad {
        data class Error(val errorMsg: String?) : UiEventQuestionLoad()
        data class Inventory(val data: InventorySide) : UiEventQuestionLoad()
    }

    // tvmReport Data
    sealed class UiEventTvmReport {
        data class Report(val data: TvmReport) : UiEventTvmReport()
        data class Error(val errorMsg: String?) : UiEventTvmReport()
    }

    sealed class UiEventCategory {
        data class CategoryPosition(val selectedCategoryPosition: Int) : UiEventCategory()
    }

    // click event
    sealed class UiAction {
        data class ItemClicked(val position: Int) : UiAction()
    }

    // invitation state
    sealed class UiInvitation {
        object GoToNextScreen : UiInvitation()
        data class Error(val msg: String?) : UiInvitation()
    }

    /* OnBoarding video Ui */
    sealed class UiEventOnBoardingResponse {
        data class Data(val success: CorporateInfoResponse) : UiEventOnBoardingResponse()
        data class Error(val error: String?) : UiEventOnBoardingResponse()
    }

    /*resume screen launch where it left*/
    enum class ScreenStage {
        PRIMERI,
        PRIMERII,
        PRIMERIII,
        PRIMERIV,
        PRIMERV,
        PRIMERX,
        PRIMERVI,
        NAME,
        EMAIL,
        PASSWORD,
        BIRTHDAY,
        NOTIFICATION,
        PHOTO,
        LOCATION,
        AVAILABILITY,
        INVENTORY,
        FACET,
        GUIDE,
        TVMNUMBER,
        LOS, /*level one score setting */
        LOI,  /*level one inviation guide*/
        INVITATION,
        UNLOCK
    }
}