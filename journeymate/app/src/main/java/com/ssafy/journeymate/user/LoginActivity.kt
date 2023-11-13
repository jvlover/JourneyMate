package com.ssafy.journeymate.user

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.kakao.sdk.auth.AuthApiClient
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.auth.model.Prompt
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import com.ssafy.journeymate.LoadScreenActivity
import com.ssafy.journeymate.MainActivity
import com.ssafy.journeymate.api.FindUserResponse
import com.ssafy.journeymate.api.RegistUserRequest
import com.ssafy.journeymate.api.RegistUserResponse
import com.ssafy.journeymate.databinding.ActivityLoginBinding
import com.ssafy.journeymate.global.App
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class LoginActivity : AppCompatActivity(), View.OnClickListener {

    private val binding by lazy { ActivityLoginBinding.inflate(layoutInflater) }
    private lateinit var userApi: com.ssafy.journeymate.api.UserApi

    private val mCallback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
        if (error != null) {
            Log.e("로그인 실패", "로그인 실패 $error")
        } else if (token != null) {
            nextMainActivity()
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            binding.btnLogin.id -> {
                if (UserApiClient.instance.isKakaoTalkLoginAvailable(this)) {
                    UserApiClient.instance.loginWithKakaoTalk(this) { token, error ->
                        if (error != null) {
                            Log.e("로그인 실패", "로그인 실패 $error")
                            if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                                return@loginWithKakaoTalk
                            } else {
                                UserApiClient.instance.loginWithKakaoAccount(
                                    this,
                                    callback = mCallback
                                )
                            }
                        } else if (token != null) {
                            nextMainActivity()
                        }
                    }
                } else {
                    UserApiClient.instance.loginWithKakaoAccount(
                        this,
                        prompts = listOf(Prompt.LOGIN),
                        callback = mCallback
                    )
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        KakaoSdk.init(this, "40596edef539ca00b83b69aa4ae58f12")

        val retrofit = Retrofit.Builder()
            .baseUrl("http://k9a204.p.ssafy.io:8000")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        userApi = retrofit.create(com.ssafy.journeymate.api.UserApi::class.java)

        if (AuthApiClient.instance.hasToken()) {
            UserApiClient.instance.accessTokenInfo { _, error ->
                if (error == null) {
                    nextMainActivity()
                }
            }
        }

        setContentView(binding.root)

        binding.btnLogin.setOnClickListener(this)

        val intent = Intent(this, LoadScreenActivity::class.java)
        startActivity(intent)
    }

    private fun nextMainActivity() {
        setUserProfile()
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun setUserProfile() {
        UserApiClient.instance.me { user, error ->
            if (error != null) {
                Log.e("userInformation", "사용자 정보 요청 실패 $error")
                finish()
            } else if (user != null) {
                Log.d("userInformation", "사용자 정보 요청 성공 : $user")
                if (user.properties?.containsKey("user_id") == true) {
                    // 이미 회원가입 되어있는 경우

                    var findUserResponse: Call<FindUserResponse> =
                        userApi.findUserById(user.properties?.get("user_id").toString())

                    findUserResponse.enqueue(object : Callback<FindUserResponse> {

                        override fun onResponse(
                            call: Call<FindUserResponse>,
                            response: Response<FindUserResponse>
                        ) {
                            val result = response.body()
                            if (result == null) {
                                // 없을 때 ?
                            } else if (result != null) {
                                App.INSTANCE.nickname = result.data.nickname
                                App.INSTANCE.id = result.data.id
                                App.INSTANCE.profileImg = result.data.imgUrl
                                App.INSTANCE.accessToken = result.data.accessToken
                                // 엑세스 토큰 반환할 것
                            }
                        }

                        override fun onFailure(call: Call<FindUserResponse>, t: Throwable) {
                            Log.e("메인액티비티 에러", "${t.localizedMessage}")
                        }
                    })
                } else {
                    // 회원가입 안 된 경우 닉네임 수정 페이지로 넘어가고 회원가입

                    val registUserRequest = RegistUserRequest(
                        user.kakaoAccount?.profile?.nickname.toString(),
                        user.kakaoAccount?.profile?.profileImageUrl.toString()
                    )
                    var registUserResponse: Call<RegistUserResponse> =
                        userApi.registUser(registUserRequest)

                    registUserResponse.enqueue(object : Callback<RegistUserResponse> {

                        override fun onResponse(
                            call: Call<RegistUserResponse>,
                            response: Response<RegistUserResponse>
                        ) {
                            val result = response.body()
                            if (result == null) {
                                // 없을 때 ?
                            } else if (result != null) {
                                App.INSTANCE.nickname = result.data.nickname
                                App.INSTANCE.id = result.data.id
                                App.INSTANCE.profileImg = result.data.imgUrl
                                App.INSTANCE.accessToken = result.data.accessToken
                                // 엑세스 토큰 반환할 것
                            }
                        }

                        override fun onFailure(call: Call<RegistUserResponse>, t: Throwable) {
                            Log.e("메인액티비티 에러", "${t.localizedMessage}")
                        }
                    })
                }

            }
        }
    }
}