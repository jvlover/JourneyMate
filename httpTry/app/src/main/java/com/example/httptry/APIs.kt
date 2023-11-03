package com.example.httptry

import android.text.Editable
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

data class Error(val code: String, val message: String)
data class UserLoginPostRes(val userId: Long, val loginId: String, val nickname: String)
// 전달 형식 어떻게?
data class LoginResponse(val success: Boolean, val data: UserLoginPostRes, val error: Error)

interface ApiService {
    @POST("/user/login")
    fun login(@Body loginRequest: LoginRequest): Call<LoginResponse>
}

data class LoginRequest(val loginId: String, val loginPassword: String)