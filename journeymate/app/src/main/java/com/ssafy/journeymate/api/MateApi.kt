package com.ssafy.journeymate.api


import okhttp3.HttpUrl
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.PartMap
import retrofit2.http.Path
import java.io.Serializable
import java.util.Objects


data class RegistMateResponse(val message: String, val data: MateGroupData) : Serializable

data class MateGroupData(
    val name: String,
    val destination: String,
    val startDate: String,
    val endDate: String,
    val users: MutableList<String>,
    val creator: String,
    val createdDate: String,
    val mateId: Long
)

data class ModifyMateResponse(val message: String, val data: MateGroupData)

data class DeleteResponse(val message: String, val data: Objects)

data class LoadMateInfoResponse(val message: String, val data: MateGroupData)

data class RegistContentInfoResponse(val message: String, val data: RegistContentInfoData)

data class RegistContentInfoData(val contentInfo: List<ContentData>)

data class ContentData(
    val creatorId: String,
    val contentId: Long,
    val createdDate: String,
    val imgUrl: String,
    val fileName: String,
    val type: Boolean
)

data class LoadContentDetailInfoResponse(val message: String, val data: LoadContentInfoData)

data class LoadContentInfoData(val contentInfo: List<ContentData>)

data class LoadDocsListInfoResponse(val message: String, val data: LoadDocsListInfoData)

data class LoadDocsListInfoData(val docsInfoList: List<DocsListData>)

data class DocsListData(
    val title: String,
    val docsId: Long,
    val userId: String,
    val createdDate: String,
    val imgFileInfo: List<ImageFileInfo>
)

data class ImageFileInfo(val filename: String, val imgUrl: String)

data class LoadDocsDetailInfoResponse(val message: String, val data: DocsDetailData)

data class DocsDetailData(
    val nickname: String,
    val title: String,
    val content: String,
    val docsId: Long,
    val createdDate: String,
    val imgFileInfo: List<ImageFileInfo>
)

data class RegistDocsResponse(val message: String, val data: DocsDetailData)

data class ModifyDocsResponse(val message: String, val data: ModifyDocsData)

data class ModifyDocsData(
    val nickname: String,
    val title: String,
    val content: String,
    val docsId: Long,
    val createdDate: String,
    val updatedDate: String,
    val imgFileInfo: List<ImageFileInfo>
)


interface MateApi {

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
        @PartMap partMap: Map<String, @JvmSuppressWildcards RequestBody>,
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

    @Multipart
    @POST("/mate-service/docs")
    fun registDocs(
        @PartMap partMap: Map<String, @JvmSuppressWildcards RequestBody>,
        @Part imgFile: List<MultipartBody.Part>
    ): Call<RegistDocsResponse>

    @Multipart
    @PUT("/mate-service/docs")
    fun modifyDocs(
        @Body modifyDocsRequest: ModifyDocsRequest,
        @Part imgFile: List<MultipartBody.Part>
    ): Call<ModifyDocsResponse>

    @DELETE
    fun deleteDocs(@Body deleteDocsRequest: DeleteDocsRequest): Call<DeleteResponse>

}

data class RegistMateRequest(
    val destination: String,
    val name: String,
    val startDate: String,
    val endDate: String,
    val users: MutableList<String>,
    val creator: String
)

data class ModifyMateRequest(
    val mateId: Long,
    val name: String,
    val destination: String,
    val users: MutableList<String>,
    val creator: String
)

data class DeleteMateRequest(val mateId: Long, val creator: String)

data class RegistContentsRequest(val mateId: Long, val userId: String)

data class DeleteContentsRequest(val contents: List<Long>)

data class RegistDocsRequest(
    val title: String,
    val content: String,
    val mateId: Long,
    val userId: String
)

data class ModifyDocsRequest(
    val title: String,
    val content: String,
    val userId: String,
    val docsId: Long
)

data class DeleteDocsRequest(val docsId: Long, val userId: String)
