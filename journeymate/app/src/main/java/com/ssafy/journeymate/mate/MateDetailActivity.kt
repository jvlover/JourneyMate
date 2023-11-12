package com.ssafy.journeymate.mate

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import com.ssafy.journeymate.R
import com.ssafy.journeymate.api.LoadMateInfoResponse
import com.ssafy.journeymate.api.MateApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MateDetailActivity : AppCompatActivity() {
    private lateinit var retrofit: Retrofit
    private lateinit var mateApi: MateApi

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mate_detail)

        val mateId = 2L

        // Retrofit instance 생성
        retrofit = Retrofit.Builder()
            .baseUrl("http://k9a204.p.ssafy.io:8000//mate-service/{$mateId}") // 여기에 실제 API 서버 주소를 넣어야 합니다.
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        mateApi = retrofit.create(MateApi::class.java)

        // API 호출
        val call = mateApi.loadMateInfo(mateId) // 여기에 실제 id 값을 넣어야 합니다.
        call.enqueue(object : Callback<LoadMateInfoResponse> {
            override fun onResponse(call: Call<LoadMateInfoResponse>, response: Response<LoadMateInfoResponse>) {
                if (response.isSuccessful) {
                    val responseData = response.body()?.data

                    // mate 이름
                    val mateNameTextView = findViewById<TextView>(R.id.detail_mate_name)
                    mateNameTextView.text = responseData?.name

                    // userProfile의 nickname 수만큼 프로필 필요
                    val mateDetailActivity = findViewById<ConstraintLayout>(R.id.mate_detail_layout)
                    val inflater = LayoutInflater.from(this@MateDetailActivity)


                }
            }

            override fun onFailure(call: Call<LoadMateInfoResponse>, t: Throwable) {
                Log.e("error log", "실패했습니다. ")
            }
        })
    }
}