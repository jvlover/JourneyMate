package com.ssafy.journeymate.api

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

data class ModifyPersonalChecklistResponse(
    val message: String,
    val data: ModifyPersonalChecklistData
)

data class LoadAllChecklistInfoResponse(val message: String, val data: Array<LoadChecklistInfoData>)

data class LoadChecklistInfoResponse(val message: String, val data: LoadChecklistInfoData)

data class ModifyPersonalChecklistData(
    val id: Long,
    val userId: String,
    val journeyId: Long,
    val name: String,
    val num: Int,
    val isChecked: Boolean,
    val createdAt: String,
    val updatedAt: String
)

data class LoadChecklistInfoData(
    val id: Long,
    val userId: String,
    val journeyId: Long,
    val name: String,
    val num: Int,
    val isChecked: Boolean,
    val createdAt: String,
    val updatedAt: String
)

interface ChecklistApi {

    @PUT("/check-service")
    fun modifyPersonalChecklist(@Body modifyPersonalChecklistRequest: ModifyPersonalChecklistRequest): Call<ModifyPersonalChecklistResponse>

    @GET("/check-service/{userId}/{journeyId}")
    fun loadAllChecklistInfo(
        @Path(value = "userId") userId: String,
        @Path(value = "journeyId") journeyId: Long
    ): Call<LoadAllChecklistInfoResponse>

    @GET("/check-service/{id}")
    fun loadChecklistInfo(@Path(value = "id") id: Long): Call<LoadChecklistInfoResponse>
}

data class ModifyPersonalChecklistRequest(
    val userId: String,
    val journeyId: Long,
    val personalItems: Array<PersonalItem>
)

data class PersonalItem(
    val id: Long,
    val name: String,
    val num: Int,
    val isChecked: Boolean,
    val isDeleted: Boolean
)