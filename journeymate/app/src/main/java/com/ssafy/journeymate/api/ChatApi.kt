package com.ssafy.journeymate.api


import okhttp3.HttpUrl
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import java.io.Serializable
import java.time.LocalDateTime
import java.util.Objects


data class ChatResponse(val message: String, val data: ChatData) : Serializable

data class ChatData(
    val chatMessage: String,
    val creator: String,
    val createdDate: LocalDateTime,
    val mateId: Long,
    val userCount : Long
)

data class LoadChatResponse(val message: String, val data: ChatData)

data class RegistChatResponse(val message: String, val data: ChatData)


interface ChatApi {

    @POST("/mate-service/regist")
    fun registMate(@Body registMateRequest: RegistMateRequest): Call<RegistMateResponse>

    @PUT("/mate-service/update")
    fun modifyMate(@Body modifyMateRequest: ModifyMateRequest): Call<ModifyMateResponse>

    @DELETE("/mate-service/delete")
    fun deleteMate(@Body deleteMateRequest: DeleteMateRequest): Call<DeleteResponse>

    @GET("/mate-service/{mateId}")
    fun loadMateInfo(@Path(value = "mateId") mateId: Long): Call<LoadMateInfoResponse>

    @Multipart
    @POST("/mate-service/contents")
    fun registContent(
        @Body registContentsRequest: RegistContentsRequest,
        @Part imgFile: List<MultipartBody.Part>
    ): Call<RegistContentInfoResponse>

    @DELETE("/mate-service/contents")
    fun deleteContent(@Body deleteContentsRequest: DeleteContentsRequest): Call<DeleteResponse>

    @GET("/mate-service/contents/list/{mateId}")
    fun loadContentDetailInfo(@Path(value = "mateId") mateId: Long): Call<LoadContentDetailInfoResponse>

    @GET("/mate-service/docs/list/{mateId}")
    fun loadDocsListInfo(@Path(value = "mateId") mateId: Long): Call<LoadDocsListInfoResponse>

    @GET("/mate-service/docs/{docsId}")
    fun loadDocsDetailInfo(@Path(value = "docsId") docsId: Long): Call<LoadDocsDetailInfoResponse>

    @POST("/mate-service/docs")
    fun registDocs(@Body registDocsRequest: RegistDocsRequest): Call<RegistDocsResponse>

    @Multipart
    @PUT("/mate-service/docs")
    fun modifyDocs(
        @Body modifyDocsRequest: ModifyDocsRequest,
        @Part imgFile: List<MultipartBody.Part>
    ): Call<ModifyDocsResponse>

    @DELETE
    fun deleteDocs(@Body deleteDocsRequest: DeleteDocsRequest): Call<DeleteResponse>

}


