package com.ssafy.journeymate.api

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path


data class getJourneyResponse(
    val message: String,
    val data: JourneyGetRes
)

data class getMateJourneysResponse(
    val message: String,
    val data: List<JourneyGetRes>
)

data class sendChecklistResponse(
    val message: String,
    val data: List<ItemGetRes>
)

data class registJourneyResponse(
    val message: String,
    val data: Any? = null
)

data class updateJourneyResponse(
    val message: String,
    val data: Any? = null
)


class JourneyGetRes(
    val id: Long,
    val mateId: Long,
    val categoryId: Long,
    val title: String,
    val day: Int,
    val sequence: Int,
    val xcoordinate: Double,
    val ycoordinate: Double

)

class ItemGetRes(
    val name: String,
    val num: Int
)


interface JourneyApi {

    @GET("journey-service/{journeyId}")
    fun getJourney(@Path(value = "journeyId") journeyId: Long): Call<getJourneyResponse>

    @GET("/{mateId}")
    fun getMateJourneys(@Path(value = "mateId") mateId: Long): Call<getMateJourneysResponse>

    @GET("/sendChecklist/{journeyId}")
    fun sendChecklist(@Path(value = "journeyId") journeyId: Long): Call<sendChecklistResponse>

    @POST("/regist")
    fun registJourney(@Body journeyRegistPostReq: JourneyRegistPostReq): Call<registJourneyResponse>

    @PUT("/update")
    fun updateJourney(@Body journeyModifyPutReq: JourneyModifyPutReq): Call<updateJourneyResponse>

    @PUT("/delete/{journeyId}")
    fun deleteJourney(@Path(value = "journeyId") journeyId: Long): Call<JourneyGetRes>

    @PUT("/deletejourneys/{mateId}")
    fun deleteJourneysInMate(@Path(value = "mateId") mateId: Long): Call<List<JourneyGetRes>>


}

data class responseDto(
    val message: String,
    val data: Any?
)


data class JourneyRegistPostReq(
    val mateId: Long,
    val categoryId: Long,
    val title: String,
    val day: Int,
    val sequence: Int,
    val xcoordinate: Double,
    val ycoordinate: Double
)

data class JourneyModifyPutReq(
    val journeyId: Long,
    val mateId: Long,
    val categoryId: Long,
    val title: String,
    val xcoordinate: Double,
    val ycoordinate: Double
)

data class JourneyDeletePutReq(
    val journeyId: Long
)
