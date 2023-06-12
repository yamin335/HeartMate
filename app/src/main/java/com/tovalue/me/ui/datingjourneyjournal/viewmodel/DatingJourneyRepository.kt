package com.tovalue.me.ui.datingjourneyjournal.viewmodel

import com.tovalue.me.network.APIClient
import com.tovalue.me.network.safeApiCall

class DatingJourneyRepository {


    suspend fun getDatingJournal() = safeApiCall {

        APIClient.aPIClient.getDateNightById(id = 1)
    }
    
    suspend fun getDatingHomeJourney(groupId: Int) =
        safeApiCall {
            APIClient.aPIClient.getDatingJourneyHome(
                groupId = groupId
            )
        }

    suspend fun getMasterPlannerForCalender(
        fromDate: String,
        toDate: String,
        groupId: String
    ) = safeApiCall {
        APIClient.aPIClient.getMasterPlannerCalender(
            fromDate = fromDate,
            toDate = toDate,
            groupId = groupId,
            datingJourneyCalender = "2"
        )
    }

    suspend fun sendDateNightOffer(
        startDate: String,
        startTime: String,
        endDate: String,
        endTime: String,
        description: String,
        dateWeekId: Int,
        inventoryTopic: String,
        title:String,
        destination:String,
        url:String,
        groupId: Int,
        partnerUserId: Int
    ) = safeApiCall {

        APIClient.aPIClient.sendOfferDateNightFromCalendar(
            startTime = startTime,
            endTime = endTime,
            startDate=startDate,
            endDate=endDate,
            description = description,
            destination = destination,
            url = url,
            title = title,
            dateWeekId = dateWeekId,
            inventoryTopic = inventoryTopic,
            groupId = groupId,
            partnerUserId = partnerUserId,
        )

    }

    suspend fun getMeetGreetQuestions(
        groupId: Int
    ) = safeApiCall {
        APIClient.aPIClient.getMeetGreetQuestiond(
            groupId = groupId
        )
    }



    suspend fun sendMGTestimonial(
        groupId: Int,
        forMonth: String,
        ofYear: String,
        experienceType: String,
        field1: String,
        field2: String,
        field3: String,
        inviationIncluded: Int,
        status: String
    ) = safeApiCall {
        APIClient.aPIClient.sendMGTestimonial(
            groupId = groupId,
            forMonth = forMonth,
            ofYear = ofYear,
            experienceType = experienceType,
            field1 = field1,
            field2 = field2,
            field3 = field3,
            inviationIncluded = inviationIncluded,
            status = status
        )
    }

    suspend fun sendMGACKTestimonial(
        groupId: Int,
        testimonialID: Int,
        forMonth: String,
        ofYear: String,
        experienceType: String,
        field1: String,
        field2: String,
        field3: String,
        status: String
    ) = safeApiCall {
        APIClient.aPIClient.sendMGACKTestimonial(
            groupId = groupId,
            testimonialId=testimonialID,
            forMonth = forMonth,
            ofYear = ofYear,
            experienceType = experienceType,
            field1 = field1,
            field2 = field2,
            field3 = field3,
            status = status
        )
    }

    suspend fun getMeetGreetHistory(
        groupId: Int
    ) = safeApiCall {
        APIClient.aPIClient.getMeetGreetHistory(
            groupId = groupId
        )
    }

    suspend fun getReviewCouplePlan(
        testimonialID: Int
    ) = safeApiCall {
        APIClient.aPIClient.getReviewCouplePlan(
            testimonialId = testimonialID
        )
    }

}