package com.ssafy.journeymate.api

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import java.io.Serializable
import java.time.LocalDateTime

data class RegistUserResponse(val message: String, val data: RegistUserData)

data class RegistUserData(
    val id: String,
    val nickname: String,
    val imgUrl: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)

data class FindUserResponse(val message: String, val data: FindUserData)

data class FindUserData(
    val id: String,
    val nickname: String,
    val imgUrl: String,
    val createdAt: String,
    val updatedAt: String
)

data class ModifyUserProfileResponse(val message: String, val data: ModifyUserProfileData)

data class ModifyUserProfileData(
    val id: String,
    val nickname: String,
    val imgUrl: String,
    val updatedAt: String
)

data class RegistMateBridgeResponse(val message: String, val data: RegistMateBridgeData)

data class RegistMateBridgeData(
    val id: Long,
    val user: FindUserData,
    val mateId: Long,
    val createdAt: String,
    val updatedAt: String,
    val isCreator: Boolean
)

data class FindMateBridgeResponse(val message: String, val data: FindMateBridgeData)

data class FindMateBridgeData(val users: List<FindUserData>, val creator: String)

data class ModifyMateBridgeResponse(val message: String, val data: ModifyMateBridgeData)

data class ModifyMateBridgeData(
    val id: Long,
    val user: FindUserData,
    val mateId: Long,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val isCreator: Boolean
)

data class FindDocsListResponse(val message: String, val data: FindDocsListData)

data class FindDocsListData(val docsInfoList: List<FindDocsData>)

data class FindDocsData(
    val title: String,
    val docsId: Long,
    val createdData: LocalDateTime,
    val imgFileInfo: List<FileRessponseDto>
)

data class FileRessponseDto(val filename: String, val imgUrl: String)

data class FindJourneyResponse(val message: String, val data: List<FindJourneyData>)

data class FindJourneyData(
    val id: Long,
    val mateId: Long,
    val categoryId: Long,
    val title: String,
    val day: Int,
    val sequence: Int,
    val xcoordinate: Float,
    val ycoordinate: Float
)

data class FindMateResponse(val message: String, val data: List<FindMateData>)

data class FindMateData(
    val mateId: Long,
    val name: String,
    val startDate: String,
    val endDate: String,
    val users: List<String>,
    val destination: String,
    val creator: String,
    val createdDate: String
) : Serializable

interface UserApi {

    @POST("/user-service/regist")
    fun registUser(@Body registUserRequest: RegistUserRequest): Call<RegistUserResponse>

    @POST("/user-service/mateBridge")
    fun registMateBridge(@Body registMateBridgeRequest: RegistMateBridgeRequest): Call<RegistMateBridgeResponse>

    @PUT("/user-service/mateBridge")
    fun modifyMateBridge(@Body modifyMateBridgeRequest: ModifyMateBridgeRequest): Call<ModifyMateBridgeResponse>

    @GET("/user-service/findById/{id}")
    fun findUserById(@Path(value = "id") id: String): Call<FindUserResponse>

    @GET("/user-service/findByNickname/{nickname}")
    fun findUserByNickname(@Path(value = "nickname") nickname: String): Call<FindUserResponse>

    @GET("/user-service/mateBridge/{mateId}")
    fun findUserByMateId(@Path(value = "mateId") mateId: Long): Call<FindMateBridgeResponse>

    @PUT("/user-service/exit/{id}")
    fun deleteUser(@Path(value = "id") id: String): Call<Boolean>

    @PUT("/user-service")
    fun modifyProfile(@Body modifyUserProfileRequest: ModifyUserProfileRequest): Call<ModifyUserProfileResponse>

    @GET("/user-service/duplicateCheck/{nickname}")
    fun nicknameDuplicateCheck(@Path(value = "nickname") nickname: String): Call<Boolean>

    @GET("/user-service/mate/{id}")
    fun findMateById(@Path(value = "id") id: String): Call<FindMateResponse>

    @GET("/user-service/journey/{id}")
    fun findTodayJourneyById(@Path(value = "id") id: String): Call<FindJourneyResponse>

    @GET("/user-service/docs/{id}")
    fun findDocsById(@Path(value = "id") id: String): Call<FindDocsListResponse>


}

data class RegistUserRequest(val nickname: String, val imgUrl: String)

data class ModifyUserProfileRequest(val id: String, val nickname: String, val imgUrl: String)

data class RegistMateBridgeRequest(val mateId: Long, val users: List<String>, val creator: String)

data class ModifyMateBridgeRequest(val mateId: Long, val users: List<String>, val creator: String)
