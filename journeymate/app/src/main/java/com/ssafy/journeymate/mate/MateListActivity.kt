package com.ssafy.journeymate.mate

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import com.ssafy.journeymate.R
import com.ssafy.journeymate.api.FindDocsListResponse
import com.ssafy.journeymate.api.UserApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MateListActivity : AppCompatActivity() {

    private lateinit var userApi: UserApi
    private lateinit var retrofit: Retrofit
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mate_list)


        val userId = "11ee7c7b3842d729b20e1d0553c96277"

        retrofit = Retrofit.Builder()
            .baseUrl("http://k9a204.p.ssafy.io:8000/user-service/mate/{$userId}/") // 여기에 실제 API 서버 주소를 넣어야 합니다.
            .addConverterFactory(GsonConverterFactory.create())
            .build()


        userApi = retrofit.create(UserApi::class.java)

        val mateListLayout = findViewById<LinearLayout>(R.id.mate_list_layout)
        val inflater = LayoutInflater.from(this)

        val call = userApi.findDocsById(userId) // 여기에 실제 id 값을 넣어야 합니다.
        call.enqueue(object : Callback<FindDocsListResponse> {
            override fun onResponse(call: Call<FindDocsListResponse>, response: Response<FindDocsListResponse>) {
                if (response.isSuccessful) {
                    val responseData = response.body()?.data?.docsInfoList
                    responseData?.forEach { data ->

                        val view = inflater.inflate(R.layout.layout_mate_list_data, mateListLayout, false)

                        val textView = view.findViewById<TextView>(R.id.mate_name)
                        textView.text = data.title // 제목 설정


                        mateListLayout.addView(view)
                    }
                }
            }

            override fun onFailure(call: Call<FindDocsListResponse>, t: Throwable) {
                Log.e("error log", "실패했습니다. ")
            }
        })

    }

}