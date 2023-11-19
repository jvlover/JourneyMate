package com.ssafy.journeymate.api


import com.gmail.bishoybasily.stomp.lib.constants.*
import com.gmail.bishoybasily.stomp.lib.*
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import java.io.Serializable


data class ChatResponse(val message: String, val data: ChatData) : Serializable


data class ChatData(
    val type: String,
    val mateId: Long,
    val sender: String,
    val message: String,
    val userCount: Long,
    val timestamp: String
)

data class ChatMessage(
    val sender: String,
    val message: String
)

data class ResponseDto(
    val message: String,
    val data: List<ChatData>
)
data class LoadCommentResponse(val message: String, val data: List<ChatData>)

data class SendCommentRequest(val chat: ChatData)

interface ChatApi {

    @GET("/chat-service/{mateId}")
    fun loadComment(@Path("mateId") mateId: String): Call<ResponseDto>


}





