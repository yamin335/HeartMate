package com.tovalue.me.model

import com.google.gson.annotations.SerializedName

data class MyRelationshipPlanResponse(
    val data: List<MyRelationshipPlanResponseData>?,
    val status: String?
){
    @SerializedName("error")
    val error: String = ""
}

data class MyRelationshipPlanResponseData(
    val deadline_date_setting: String?,
    val id: Int?,
    val name: String?,
    val partner_name: String?,
    val percentage_complete: String?,
    val plan_id: Int?,
    val plan_title: String?,
    val reminder_status: String?
)


data class MyRelationshipPlanDetailsResponse(
    val checklist: List<MyRelationshipPlanChecklistItem>?,
    val plan_details: MyRelationshipPlanDetails?,
    val status: String?
){
    @SerializedName("error")
    val error: String = ""
}

data class MyRelationshipPlanChecklistItem(
    val interval: String?,
    val interval_id: Int?,
    val task: List<MyRelationshipPlanTask>
)

data class MyRelationshipPlanDetails(
    val completed_task_percentage: Int?,
    val deadline: String?,
    val partner_assignment: String?,
    val title: String?
)

data class MyRelationshipPlanTask(
    val status: String?,
    val task: String?,
    val task_id: Int?,
    var isChecked: Boolean = false
)


data class RelationshipPlanResponse(
    val data: List<RelationshipPlan>?,
    val status: String?
){
    @SerializedName("error")
    val error: String = ""
}

data class RelationshipPlan(
    val id: Int?,
    @SerializedName("plan title")
    val plan_title: String?,
    val plan_description: String?
)