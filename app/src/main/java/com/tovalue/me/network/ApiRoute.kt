package com.tovalue.me.network

import com.tovalue.me.model.*
import com.tovalue.me.model.DateNightOffer
import com.tovalue.me.model.datenightcatalog.DateNightCatalogDetailResponse
import com.tovalue.me.model.datenightcatalog.HolderData
import com.tovalue.me.model.datingJourney.*
import com.tovalue.me.model.moodRingModels.MoodRingHistoryDetailResponse
import com.tovalue.me.model.moodRingModels.MoodRingHistoryResponse
import com.tovalue.me.model.moodRingModels.MoodRingStoreResponse
import com.tovalue.me.model.notification.NotificationListResponse
import com.tovalue.me.model.notification.UnreadCountModel
import com.tovalue.me.model.notification.UpcomingPlanReview
import com.tovalue.me.model.upcomingplans.UpcomingPlansResponse
import com.tovalue.me.model.whatworksguide.VoteResponse
import com.tovalue.me.model.whatworksguide.WhatWorksGuideDataRespose
import com.tovalue.me.model.whatworksguide.WhatWorksGuideRespose
import com.tovalue.me.ui.auth.primeriii.InventorySide
import com.tovalue.me.ui.auth.primeriii.spectrum.SpectrumResponse
import com.tovalue.me.ui.dashboard.manageInvitation.DeleteInvitation
import com.tovalue.me.ui.dashboard.manageInvitation.InvitationsHistory
import com.tovalue.me.ui.dashboard.upcomingplans.models.*
import com.tovalue.me.ui.levelTwoInvitation.response.CommitmentOfferResponse
import com.tovalue.me.ui.visionboard.response.IceBreakerResponseModel
import com.tovalue.me.ui.visionboard.response.PartnerVisionBoardModel
import com.tovalue.me.ui.visionboard.response.VisionBoardModel
import com.tovalue.me.util.Constants.SECRET_KEY
import com.tovalue.me.util.Utils.getRandomStr
import com.tovalue.me.util.Utils.getRandomString
import okhttp3.MultipartBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface ApiRoute {

    @GET("get_corporate_info/")
    suspend fun getCorporateInfo(
        @Query("cache") cacheParam: String? = getRandomString()): Response<CorporateInfoResponse>

    // Deprecated
//    @POST("userplus/generate_auth_cookie/")
//    suspend fun getFreshCookie(
//        @Query("username") username: String,
//        @Query("password") password: String,
//        @Query("key") key: String = KEY
//    ): Response<Oauth>

    @POST("invitations/generate_dating_invitation/")
    suspend fun levelOneInvitation(
        @Query("cache") cacheParam: String? = getRandomString(),
        @Query("user_id") userId: Int,
        @Query("firstName") firstName: String,
        @Query("invitation_type") invitationType: String,
        @Query("match_source") matchSource: String,
        @Query("group_level") groupLevel: Int,
        @Query("invitee_phone_number", encoded = true) inviteeNumber: String
    ): Response<Oauth>

    @POST("invitations/dating_partnership_accepted/")
    suspend fun acceptLevelOneInvitation(
        @Query("invitation_code") code: String,
        @Query("user_id") userId: Int
    ): Response<HomeJourney>

    @POST("invitations/dating_partnership_rejected/")
    suspend fun declineLevelOneInvitation(
        @Query("invitation_code") code: String,
        @Query("user_id") userId: Int
    ): Response<HomeJourney>

    @POST("userplus/get_profile/")
    suspend fun getUserInfo(
        @Query("user_id") userId: Int,
        @Query("cache") cacheParam: String
    ): Response<ProfileInfo>

    @POST("userplus/delete_account/")
    suspend fun deactivateAccount(
        @Query("nonce") nonceValue: String
    ): Response<Generic>


    @POST("userplus/logoff/")
    suspend fun logout(@Query("aws") aws: String): Response<Generic>

    @POST("userplus/retrieve_password/")
    fun forgotPassword(
        @Query("key") secretKey: String,
        @Query("user_login") email: String
    ): Call<Generic?>?

    @Multipart
    @POST("userplus/avatar_upload/")
    suspend fun avatarUpload(
        @Query("cache") cacheParam: String = getRandomStr(),
        @Part file: MultipartBody.Part
    ): Response<Avatar>

    @POST("userplus/update_user_meta_vars/")
    suspend fun updateUserMetaValues(
        @Query("cache") cacheParam: String = getRandomStr(),
        @Query("birthday") birthday: String? = null,
        @Query("all_push_notifications") notification: Boolean? = null,
        @Query("push_new_discoveries") discovery: Boolean? = null,
        @Query("push_new_invitations") invitation: Boolean? = null,
        @Query("push_mood_rings") newMoodRings: Boolean? = null,
        @Query("push_promotions") promotion: Boolean? = null,
        @Query("push_announcements") announcement: Boolean? = null,
        @Query("email_all_notifications") allEmail: Boolean? = null,
        @Query("email_new_discoveries") emailDiscovery: Boolean? = null,
        @Query("email_new_invitations") emailInvitation: Boolean? = null,
        @Query("email_mood_rings") emailNewMoodRings: Boolean? = null,
        @Query("email_promotions") emailPromotion: Boolean? = null,
        @Query("email_announcements") emailAnnouncement: Boolean? = null,
        @Query("subscription_status") subscriptionStatus: String? = null,
        @Query("subscription_token") subscriptionToken: String? = null,
        @Query("subscription_type") subscriptionType: String? = null,
        @Query("level_1_score") levelOneScore: Int? = null,
        @Query("level_2_score") levelTwoScore: Int? = null,
        @Query("my_location") location: String? = null,
        @Query("max_distance") eventMaxDistance: String? = null,
        @Query("location") registeredLocation: String? = null,
        @Query("dating_availability") dating_availability: String? = null,
        @Query("android_device_token") deviceToken: String? = null,
        @Query("latitude") lat: Double? = null,
        @Query("longitude") lng: Double? = null
    ): Response<Generic>

    // vendor change reveert [get]
//    @POST("userplus/get_user_meta/")
//    suspend fun getMetaValues(
//        @Query("cookie", encoded = true) cookie: String,
//        @Query("key") secretKey: String,
//    ): UpdateMetaKeys

    @POST("userplus/onboarding/")
    suspend fun getCookie(
        @Query("cache") cacheParam: String? = getRandomString(),
        @Query("username", encoded = true) number: String
    ): Response<Oauth>

    @POST("get_nonce/?json=get_nonce")
    suspend fun getNonceToken(
        @Query("controller") controllerType: String, @Query("method") method: String
    ): Response<Nonce>

    @POST("userplus/register/")
    suspend fun createUser(
        @Query("cache") cacheParam: String? = getRandomString(),
        @Query("email", encoded = true) email: String,
        @Query("user_pass") password: String,
        @Query("username", encoded = true) userName: String,
        @Query("nonce") nonceValue: String
    ): Response<Oauth>

    @POST("userplus/update_user/")
    suspend fun updateUser(
        @Query("first_name") firstName: String,
        @Query("last_name") lastName: String,
        @Query("user_email") email: String,
        @Query("display_name") displayName: String
    ): Response<Generic>

    // need to remove and merge in the above api
    @POST("userplus/xprofile_update/")
    suspend fun updateAvailability(
        @Query("availability") metaValue: String?
    ): Response<Generic>

    @GET("userplus/get_life_inventory/")
    suspend fun getInventorySides(
        @Query("user_id") userId: Int
    ): Response<InventorySide>

    @POST("userplus/get_life_inventory_questions_by_side/")
    suspend fun getInventoryQuestion(
        @Query("category_id") groupKey: Int,
        @Query("secret") secret: String = SECRET_KEY
    ): Response<InventorySide>

    @POST("userplus/email_exists/")
    suspend fun emailExist(
        @Query("email") email: String
    ): Response<Generic>

    @POST("userplus/update_life_inventory_by_category/")
    suspend fun updateInventoryValues(
        @Query("user_id") userId: Int,
        @Query("category_id") categoryId: Int,
        @Query("serialized_json", encoded = true) answerValues: String,
        @Query("category_title") title: String,
        @Query("secret") secret: String = SECRET_KEY
    ): Response<SpectrumResponse>

    @POST("userplus/to_value_me_report/")
    suspend fun getTVMReport(
        @Query("cache") cacheParam: String? = getRandomString()
    ): Response<TvmReport>

    @POST("userplus/date_night_catalog/")
    suspend fun getGuideCatalog(): Response<TvmReport>


    @POST("userplus/dating_journeys/")
    suspend fun getDatingJourneys(@Query("cache") cacheParam: String? = getRandomString()): Response<DatingResponse>


    @POST("userplus/get_partner_date_night_catalog/")
    suspend fun getPartnerDateNightCatalog(
        @Query("partner_user_id") partnerUserId: Int,
        @Query("group_id") groupId: Int,
        @Query("can_see_promoted") canSeePromoted:Int,
        @Query("offset") offSet:Int = 0,
        @Query("cache") cache:String? = getRandomString()
    ): Response<HolderData.DateNightCatalogResponse>


    @POST("userplus/get_date_night/")
    suspend fun getDateNightCatalogDetail(
        @Query("date_night_id") dateNightId: String,
        @Query("is_partner") isPartner:Int,//1 or 0,
        @Query("cache") cache: String? = getRandomString()

    ): Response<DateNightCatalogDetailResponse>


    @Multipart
    @POST("userplus/offer_date_night/")
    suspend fun sendOfferDateNight(
        @Query("event_description") description: String,
        @Query("date_night_id") dateNightId: String,
        @Query("date_week_id") dateWeekId: String,
        @Query("inventory_topic") inventoryTopic: String,
        @Query("partner_id") partnerUserId: Int,
        @Query("title") title:String,
        @Query("event_address") event_address:String,
        @Query("event_end_date") event_end_date:String,
        @Query("event_start_date") event_start_date:String,
        @Query("event_start_time") startTime: String,
        @Query("event_end_time") endTime: String,
        @Query("group_id") groupId: Int,
        @Query("url") url:String,
        @Part attachment: MultipartBody.Part

    ): Response<DateNightOfferResponse>


    @POST("userplus/get_date_night")
    suspend fun getDateNightById(
        @Query("date_night_id") id: Int
    ): Response<JSONObject>

    @POST("userplus/get_date_journey_home/")
    suspend fun getDatingJourneyHome(
        @Query("group_id") groupId: Int
    ): Response<JourneyHomeResponse>

//    @POST("userplus/create_level_2_invitation/")
    @POST("invitationsprogressions/generate_progression/")
    suspend fun upGradeToLevel(
        @Query("group_id") groupId: Int,
        @Query("cache") cacheParam: String? = getRandomString(),
        @Query("level") progressLevel: Int,
        @Query("comfort_response") comfort_response: String?,
        @Query("balance_response") balance_response: String?,
        @Query("safety_response") safety_response: String?
    ): Response<UpgradeToLevelModel>


    @POST("userplus/get_master_planner/")
    suspend fun getMasterPlanner(
        @Query("cache") cacheParam: String? = getRandomString(),
        @Query("from_date") fromDate: String,
        @Query("to_date") toDate: String,
        @Query("group_id", encoded = true) groupId: String
    ): Response<UpcomingPlansResponse>


    @POST("userplus/schedule_me_time/")
    suspend fun scheduleMeTime(
        @Query("event_start_date") startDate: String,
        @Query("event_start_time", encoded = true) startTime: String,
        @Query("event_end_date") endDate: String,
        @Query("event_end_time", encoded = true) endTime: String
    ): Response<Generic>

    @POST("userplus/get_date_event_details/")
    suspend fun getEventDetail(
        @Query("cache") cacheParam: String? = getRandomString(),
        @Query("event_id") eventId: Int
    ): Response<UpcomingPlanDetailResponse>

    @POST("userplus/get_reschedule_offer_details/")
    suspend fun getReScheduledEventDetail(
        @Query("cache") cacheParam: String? = getRandomString(),
        @Query("event_reschedule_id") eventId: Int
    ): Response<UpcomingPlanDetailResponse>

    @POST("userplus/get_date_night_offer/")
    suspend fun getDateNightOffer(
        @Query("cache") cacheParam: String? = getRandomString(),
        @Query("date_night_offer_id") dateNightOfferId: Int
    ): Response<UpcomingPlanDetailResponse>

    @POST("userplus/submit_date_night_offer_decision/")
    suspend fun submitDateNightOfferDecision(
        @Query("date_night_offer_id") dateNightOfferId: Int,
        @Query("decision") decision: Int,
        @Query("event_start_date") startDate: String,
        @Query("event_start_time", encoded = true) startTime: String,
        @Query("event_end_date") endDate: String,
        @Query("event_end_time", encoded = true) endTime: String
    ): Response<DateNightOfferDecisionResponse>

    @POST("userplus/offer_reschedule_date_night_event/")
    suspend fun offerRescheduleDateNightEvent(
        @Query("event_id") eventId: Int,
        @Query("event_start_date") startDate: String,
        @Query("event_start_time", encoded = true) startTime: String,
        @Query("event_end_date") endDate: String,
        @Query("event_end_time", encoded = true) endTime: String
    ): Response<RescheduleOrCancelEventResponse>


    @POST("userplus/accept_reschedule_date_night_event/")
    suspend fun acceptRescheduleDateNightEvent(
        @Query("event_id") eventId: Int,
        @Query("reschedule_offer_id") rescheduleOfferId: Int,
        @Query("event_start_date") startDate: String,
        @Query("event_start_time") startTime: String,
    ): Response<AcceptRescheduleOrCancelEventResponse>

    @POST("userplus/cancel_me_time/")
    suspend fun cancelMeTime(
        @Query("event_id") eventId: Int,
    ): Response<Generic>


    @POST("userplus/get_cancel_reasons/")
    suspend fun getCancelReasons(
        @Query("cache") cacheParam: String? = getRandomString()
    ): Response<CancelReasonResponse>


    @POST("userplus/cancel_date_night_event/")
    suspend fun cancelDateNightEvent(
        @Query("event_id") eventId: Int,
        @Query("cancel_reason_id") cancelReasonID: Int
    ): Response<RescheduleOrCancelEventResponse>


    /*Notificaitons*/
    @POST("userplus/notifications_unread_count")
    suspend fun getUnreadNotificationCount(): Response<UnreadCountModel>

    @POST("userplus/notifications")
    suspend fun getNotifications(
        @Query("cache") cacheParam: String? = getRandomString(),
        @Query("component") component: String? = null,
        @Query("search_terms") searchTerms: String? = null,
        @Query("is_new") isNew: String? = null,
        @Query("page") page: String? = null,
        @Query("per_page") perPage: String? = null,
    ): Response<NotificationListResponse>


    @POST("userplus/save_daily_mood_ring")
    suspend fun saveMoodRing(
        @Query("emotionally") emotionally: Int,
        @Query("mentally") mentally: Int,
        @Query("physically") physically: Int,
        @Query("communally") communally: Int,
        @Query("professionally") professionally: Int,
        @Query("spiritually") spiritually: Int,
        @Query("emotionally_explanation") emotionally_explanation: String,
        @Query("mentally_explanation") mentally_explanation: String,
        @Query("spiritually_explanation") spiritually_explanation: String,
        @Query("communally_explanation") communally_explanation: String,
        @Query("physically_explanation") physically_explanation: String,
        @Query("professionally_explanation") professionally_explanation: String,
    ): Response<MoodRingStoreResponse>

    @GET("userplus/get_mood_ring_history")
    suspend fun getMoodRingHistory(
        @Query("page_number") page_number: Int,
        @Query("for_week_ending") for_week_ending: String
    ): Response<MoodRingHistoryResponse>

    @GET("userplus/get_mood_ring")
    suspend fun getMoodRingHistoryDetails(
        @Query("mood_ring_id") mood_ring_id: Int
    ): Response<MoodRingHistoryDetailResponse>

    @POST("get_what_works_home/")
    suspend fun getWhatWorks(): Response<WhatWorksGuideRespose>


    @POST("get_what_works_detail/")
    suspend fun getWhatWorksData(@Query("guide_id") guideID: Int? = null): Response<WhatWorksGuideDataRespose>


    @POST("guide_vote/")
    suspend fun sendVote(
        @Query("guide_id") guideID: Int? = null,
        @Query("vote") vote: Int? = null
    ): Response<VoteResponse>

    @POST("userplus/get_commitment_offer")
    suspend fun getCommitmentOffer(
        @Query("cache") cacheParam: String? = getRandomString(),
        @Query("offer_post_id") offerPostId: Int? = null
    ): Response<CommitmentOfferResponse>

    @POST("userplus/accept_commitment_offer")
    suspend fun acceptCommitmentOffer(
        @Query("cache") cacheParam: String? = getRandomString(),
        @Query("offer_post_id") offerPostId: Int? = null,
        @Query("group_id") groupId: Int? = null,
        @Query("comfort_response") comfortResponse: String? = null,
        @Query("balance_response") balanceResponse: String? = null,
        @Query("safety_response") safetyResponse: String? = null,
    ): Response<CommitmentOfferResponse>


    @POST("userplus/decline_commitment_offer")
    suspend fun declineCommitmentOffer(
        @Query("cache") cacheParam: String? = getRandomString(),
        @Query("offer_post_id") offerPostId: Int? = null,
        @Query("group_id") groupId: Int? = null,
        @Query("decision_response") decisionResponse: Int? = null,
    ): Response<CommitmentOfferResponse>

    @POST("userplus/commitment_double_confirmation")
    suspend fun commitmentDoubleConfirmation(
        @Query("cache") cacheParam: String? = getRandomString(),
        @Query("acceptance_post_id") acceptancePostId: Int? = null,
        @Query("group_id") groupId: Int? = null,
        @Query("rejection_message") rejectionMessage: String? = null,
        @Query("acceptance_status") acceptanceStatus: Int? = null,
    ): Response<CommitmentOfferResponse>

    @POST("userplus/get_all_promoted_events_near_us/")
    suspend fun getAllPromotedEvents(
        @Query("partner_user_id") partnerUserId: Int,
        @Query("group_id") groupId: Int,
        @Query("can_see_promoted") promoted: Int,
    ): Response<HolderData.PromotedEventResponse>




    @GET("userplus/get_date_night_catalog")
    suspend fun getDateNightIdeas(
        @Query("page") page: Int,
        @Query("can_see_promoted") canSeePromoted: Int,
        @Query("group_id") groupId: Int,
        @Query("user_id") userId: Int
    ): Response<HolderData.DateNightCatalogResponse>

    @GET("userplus/get_date_night")
    suspend fun getDateNightIdeaDetails(
        @Query("date_night_id") dateNightId: Int,
        @Query("is_partner") isPartner: Int
    ): Response<DateNightCatalogDetailResponse>

    @POST("userplus/review_upcoming_plan/")
    suspend fun getReviewUpcomingPlan(
        @Query("cache") cacheParam: String? = getRandomString(),
        @Query("item_id") itemId: Int? = null
    ): Response<UpcomingPlanReview>

    @POST("userplus/get_invitation_history")
    suspend fun getInvitationHistory(
        @Query("cache") cacheParam: String? = getRandomString()
    ): Response<InvitationsHistory>

    @POST("userplus/delete_invitation")
    suspend fun deleteInvitationHistory(
        @Query("cache") cacheParam: String? = getRandomString(),
        @Query("invitation_code") invitationCode: String,
        @Query("group_id") groupId: Int
    ): Response<DeleteInvitation>

    @POST("userplus/get_master_planner/")
    suspend fun getMasterPlannerCalender(
        @Query("cache") cacheParam: String? = getRandomString(),
        @Query("from_date") fromDate: String,
        @Query("to_date") toDate: String,
        @Query("dating_journey_calendar") datingJourneyCalender: String,
        @Query("group_id", encoded = true) groupId: String
    ): Response<DatingOurCalenderResponse>

    @POST("userplus/offer_date_night/")
    suspend fun sendOfferDateNightFromCalendar(
        @Query("event_description") description: String,
        @Query("date_week_id") dateWeekId: Int,
        @Query("inventory_topic") inventoryTopic: String,
        @Query("partner_id") partnerUserId: Int,
        @Query("title") title: String,
        @Query("event_start_date") startDate: String,
        @Query("event_start_time") startTime: String,
        @Query("event_end_date") endDate: String,
        @Query("event_end_time") endTime: String,
        @Query("group_id") groupId: Int,
        @Query("url") url: String,
        @Query("destination") destination: String
    ): Response<DateNightOfferResponse>

    @Multipart
    @POST("userplus/offer_date_night/")
    suspend fun createOfferDateNight(
        @Query("event_description") eventDescription: String,
        @Query("date_night_id") dateNightId: Int,
        @Query("date_week_id") dateWeekId: Int,
        @Query("inventory_topic") inventoryTopic: String,
        @Query("partner_id") partnerId: Int,
        @Query("title") title: String,
        @Query("event_start_date") startDate: String,
        @Query("event_start_time", encoded = true) startTime: String,
        @Query("event_end_date") endDate: String,
        @Query("event_end_time", encoded = true) endTime: String,
        @Query("event_address", encoded = true) eventAddress: String,
        @Query("group_id", encoded = true) groupId: Int,
        @Query("url", encoded = true) url: String,
        @Part file: MultipartBody.Part?
    ): Response<DateNightOffer>

    @POST("userplus/update_subscription_status/")
    suspend fun updateSubscriptionStatus(
        @Query("cache") cacheParam: String? = getRandomString(),
        @Query("subscription_status") subscriptionStatus: String,
        @Query("subscription_type") subscriptionType: String,
        @Query("subscription_token") subscriptionToken: String,
        @Query("subscription_expiry_date") subscriptionExpiryDate: String,
        @Query("device_type") groupId: String = "android"
    ): Response<Generic>

    @GET("relationshipgoals/get_my_relationship_plans")
    suspend fun getMyRelationshipPlans(): Response<MyRelationshipPlanResponse>

    @GET("relationshipgoals/get_my_relationship_plan_details")
    suspend fun getMyRelationshipPlanDetails(
        @Query("plan_id") planId: Int?
    ): Response<MyRelationshipPlanDetailsResponse>

    @GET("relationshipgoals/get_relationship_goal_plans")
    suspend fun getAvailableRelationshipPlans(): Response<RelationshipPlanResponse>

    @POST("relationshipgoals/update_my_relationship_plan_progress")
    suspend fun updateMyRelationshipPlanProgress(
        @Query("plan_id") planId: Int?,
        @Query("task_id") taskIds: String?
    ): Response<DefaultApiResponse>

    @POST("meetgreettestimonial/get_mg_testimonial_questions")
    suspend fun getMeetGreetQuestiond(
        @Query("cache") cacheParam: String? = getRandomString(),
        @Query("group_id") groupId: Int
    ): Response<DatingMeetGreetResponse>

    @POST("meetgreettestimonial/submit_mg_testimonial")
    suspend fun sendMGTestimonial(
        @Query("group_id") groupId: Int,
        @Query("for_month") forMonth: String,
        @Query("of_year") ofYear: String,
        @Query("experience_type") experienceType: String,
        @Query("field_1") field1: String,
        @Query("field_2") field2: String,
        @Query("field_3") field3: String,
        @Query("inviation_included") inviationIncluded: Int,
        @Query("status") status: String,
    ): Response<DatingSubmitMeetGreetResponse>

    @POST("meetgreettestimonial/acknowledge_mg_testimonial")
    suspend fun sendMGACKTestimonial(
        @Query("group_id") groupId: Int,
        @Query("testimonial_id") testimonialId: Int,
        @Query("for_month") forMonth: String,
        @Query("of_year") ofYear: String,
        @Query("experience_type") experienceType: String,
        @Query("field_1") field1: String,
        @Query("field_2") field2: String,
        @Query("field_3") field3: String,
        @Query("status") status: String,
    ): Response<DatingSubmitMeetGreetResponse>

    @POST("meetgreettestimonial/get_mg_testimonial_history")
    suspend fun getMeetGreetHistory(
        @Query("cache") cacheParam: String? = getRandomString(),
        @Query("group_id") groupId: Int
    ): Response<DatingMeetGreetHistoryResponse>

    @POST("meetgreettestimonial/get_mg_testimonial_details")
    suspend fun getReviewCouplePlan(
        @Query("cache") cacheParam: String? = getRandomString(),
        @Query("testimonial_id") testimonialId: Int
    ): Response<DatingReviewCouplePlanResponse>
    
    @POST("visionboard/get_vision_board")
    suspend fun getVisionBoard(
        @Query("cache") cacheParam: String? = getRandomString()
    ): Response<VisionBoardModel>
    
    @POST("visionboard/get_icebreakers")
    suspend fun getIceBreakers(
        @Query("vision_board_id") id: Int = 1,
        @Query("cache") cacheParam: String? = getRandomString(),
    ): Response<IceBreakerResponseModel>
    
    @POST("visionboard/get_partner_vision_board/")
    suspend fun getPartnerVisionBoard(
        @Query("vision_board_id") id: Int = 1
    ): Response<PartnerVisionBoardModel>
    
    @POST("visionboard/submit_mindset")
    suspend fun mindsetData(
        @Query("cache") cacheParam: String? = getRandomString(),
        @Query("mindset") msg: String
    ): Response<Generic>
    
    @POST("visionboard/submit_adulting_season")
    suspend fun adultSeasonData(
        @Query("vision_board_id") id: Int = 1,
        @Query("primary_selection") primaryMsg: String,
        @Query("secondary_selection") secondaryMsg: String
    ): Response<Generic>
    
    @POST("visionboard/submit_bliss")
    suspend fun blissData(
        @Query("vision_board_id") id: Int = 1,
        @Query("primary_selection") primaryMsg: String,
        @Query("secondary_selection") secondaryMsg: String
    ): Response<Generic>
    
    @POST("visionboard/submit_dating_style")
    suspend fun dateStyleData(
        @Query("cache") cacheParam: String? = getRandomString(),
        @Query("entry_1") entryOne: String,
        @Query("entry_2") entryTwo: String,
        @Query("entry_3") entryThree: String
    ): Response<Generic>
}