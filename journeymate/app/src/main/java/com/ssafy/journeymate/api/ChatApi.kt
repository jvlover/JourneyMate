package com.ssafy.journeymate.api


import com.gmail.bishoybasily.stomp.lib.constants.*
import com.gmail.bishoybasily.stomp.lib.*
import okhttp3.HttpUrl
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
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
    val chatType: String,
    val chatMessage: String,
    val creator: String,
    val mateId: Long,
    val userCount : Long
)

data class LoadCommentResponse(val message: String, val data: List<ChatData>)

data class SendCommentRequest(val chat: ChatData)

interface ChatApi {

    @GET("/comment-service/{mateId}")
    fun loadComment(@Path(value = "mateId") mateId : Long): Call<List<LoadCommentResponse>>

}




