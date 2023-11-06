package com.example.httptry

import android.text.Editable
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import java.util.Date

data class Error(val code: String, val message: String)
data class UserLoginPostRes(val userId: Long, val loginId: String, val nickname: String)
// 전달 형식 어떻게?
data class LoginResponse(val success: Boolean, val data: UserLoginPostRes, val error: Error)

data class CommentLoadGetRes(val commentId: String, val mateId: String, val sender: String, val message: String, val userCount: Long, val timeStampe: Date)

data class LoadCommentResponse(val success: Boolean, val data: CommentLoadGetRes, val error: Error)

interface ApiService {
    @POST("/user/login")
    fun login(@Body loginRequest: LoginRequest): Call<LoginResponse>
    @GET("/comment-service/{mateId}")
    fun loadComment(@Path(value = "mateId") mateId : String): Call<List<LoadCommentResponse>>
}

data class LoginRequest(val loginId: String, val loginPassword: String)

