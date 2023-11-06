package com.example.httptry

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.httptry.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    // ViewBinding 사용
    private lateinit var binding: ActivityMainBinding
    private lateinit var apiService: ApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val retrofit = Retrofit.Builder()
            .baseUrl("http://70.12.246.220:8080")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiService = retrofit.create(ApiService::class.java)

        binding.button1.setOnClickListener {
            val loginRequest = LoginRequest(binding.idInput.text.toString(), binding.pwInput.text.toString())
            val callLogin: Call<LoginResponse> = apiService.login(loginRequest)

            callLogin.enqueue(object: Callback<LoginResponse> {
                override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                    val result = response.body() as LoginResponse?
                    if( result == null ) {
                        binding.textView.text = "안녕!"
                    }
                    else if (result != null) {
                        binding.textView.text = result.data.nickname
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    binding.textView.text = "에러"
                    Log.e("메인액티비티 에러", "${t.localizedMessage}")
                }
            })
        }

        binding.button2.setOnClickListener {
            val mateId = binding.mateIdInput.text.toString()
            val callLoadComment: Call<List<LoadCommentResponse>> = apiService.loadComment(mateId)
            callLoadComment.enqueue(object: Callback<List<LoadCommentResponse>> {
                override fun onResponse(
                    call: Call<List<LoadCommentResponse>>,
                    response: Response<List<LoadCommentResponse>>
                ) {
                    val result = response.body()
                    if(result.isNullOrEmpty()) binding.textView2.text = "잘안됨"
                    else {
                        val commentsText = result.joinToString("\n") { comment ->
                            "보낸이: ${comment.data.sender}, 메시지: ${comment.data.message}, 시간: ${comment.data.timeStampe}"
                        }
                        binding.textView2.text = commentsText
                    }
                }

                override fun onFailure(call: Call<List<LoadCommentResponse>>, t: Throwable) {
                    binding.textView2.text = "에러"
                    Log.e("메인 액티비티 에러", "onFailure: ", t)
                }
            })
        }


    }
}