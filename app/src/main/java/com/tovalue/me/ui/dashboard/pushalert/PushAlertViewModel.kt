package com.tovalue.me.ui.dashboard.pushalert

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tovalue.me.model.notification.NotificationListResponse
import com.tovalue.me.model.notification.UpcomingPlanReview
import com.tovalue.me.network.ResultWrapper
import com.tovalue.me.util.extensions.runIO
import com.tovalue.me.util.livedata.Event

class PushAlertViewModel : ViewModel() {

    private val repo = PushAlertRepository()

    private val _notificationsResponse = MutableLiveData<Event<UiEventNotification>>()
    val notificationsResponse: LiveData<Event<UiEventNotification>> = _notificationsResponse


    private val _upcomingPlanReviewResponse =
        MutableLiveData<Event<UpcomingPlanReviewNotificationEvent>>()
    val upcomingPlanReviewResponse: LiveData<Event<UpcomingPlanReviewNotificationEvent>> =
        _upcomingPlanReviewResponse


    fun getNotifications(
        component: String? = null,
        searchTerms: String? = null,
        isNew: String? = null,
        page: Int? = null
    ) {
        runIO {
            when (val response = repo.getNotifications(
                component = component,
                searchTerms = searchTerms,
                isNew = isNew,
                page = page
            )) {
                is ResultWrapper.GenericError -> _notificationsResponse.postValue(
                    Event(
                        PushAlertViewModel.UiEventNotification.Error(
                            response.error
                        )
                    )
                )
                is ResultWrapper.Success -> {
                    if (response.value.isSuccessful && response.value.body()!!.status == "ok")
                        _notificationsResponse.postValue(
                            Event(
                                UiEventNotification.Notification(
                                    response.value.body()!!
                                )
                            )
                        )
                    else
                        _notificationsResponse.postValue(
                            Event(
                                UiEventNotification.Error(
                                    response.value.body()?.error ?: ""
                                )
                            )
                        )
                }
            }
        }
    }

    fun getReviewUpcomingPlan(
        itemId: Int
    ) {
        runIO {
            when (val response = repo.getReviewUpcomingPlan(
                itemId
            )) {
                is ResultWrapper.GenericError -> _upcomingPlanReviewResponse.postValue(
                    Event(
                        PushAlertViewModel.UpcomingPlanReviewNotificationEvent.Error(
                            response.error
                        )
                    )
                )
                is ResultWrapper.Success -> {
                    if (response.value.isSuccessful && response.value.body()!!.status == "ok")
                        _upcomingPlanReviewResponse.postValue(
                            Event(
                                UpcomingPlanReviewNotificationEvent. UpcomingPlanReviewNotification(
                                    response.value.body()!!
                                )
                            )
                        )
                    else
                        _upcomingPlanReviewResponse.postValue(
                            Event(
                                UpcomingPlanReviewNotificationEvent.Error(
                                    response.value.body()?.error ?: ""
                                )
                            )
                        )
                }
            }
        }
    }


    sealed class UiEventNotification {
        data class Notification(val response: NotificationListResponse) : UiEventNotification()
        data class Error(val errorMsg: String) : UiEventNotification()
    }

    sealed class UpcomingPlanReviewNotificationEvent {
        data class  UpcomingPlanReviewNotification(val response: UpcomingPlanReview) :
            UpcomingPlanReviewNotificationEvent()

        data class Error(val errorMsg: String) : UpcomingPlanReviewNotificationEvent()
    }
}