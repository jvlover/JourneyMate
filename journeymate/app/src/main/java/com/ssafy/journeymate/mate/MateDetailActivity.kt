package com.ssafy.journeymate.mate

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.ssafy.journeymate.R
import com.ssafy.journeymate.api.DeleteMateRequest
import com.ssafy.journeymate.api.DeleteResponse
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

        // mateId 변경 필요
        val mateId = 2L

        retrofit = Retrofit.Builder()
            .baseUrl("http://k9a204.p.ssafy.io:8000//mate-service/{$mateId}/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        mateApi = retrofit.create(MateApi::class.java)

        // API 호출
        val callMateDataLoad = mateApi.loadMateInfo(mateId)
        callMateDataLoad.enqueue(object : Callback<LoadMateInfoResponse> {
            override fun onResponse(
                call: Call<LoadMateInfoResponse>,
                response: Response<LoadMateInfoResponse>
            ) {
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


        val mateDeleteButton: Button = findViewById(R.id.mate_group_delete)

        mateDeleteButton?.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("알림")
                .setMessage("정말로 그룹을 삭제하시겠습니까?")
                .setPositiveButton("예") { dialog, which ->
                    deleteMateData()
                }
                .setNegativeButton("확인", null)
                .show()
        }
    }

    fun deleteMateData() {

        val retrofit = Retrofit.Builder()
            .baseUrl("http://k9a204.p.ssafy.io:8000//mate-service/delete/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        mateApi = retrofit.create(MateApi::class.java)


        val deleteMateRequest = DeleteMateRequest(
            mateId = 8,
            creator = "11ee81bb69fe87aaaa4bb7bbd1e82908"
        )

        mateApi.deleteMate(deleteMateRequest).enqueue(object : Callback<DeleteResponse> {
            override fun onResponse(
                call: Call<DeleteResponse>,
                response: Response<DeleteResponse>
            ) {
                if (response.isSuccessful) {
                    // 성공적으로 삭제된 경우 처리
                    Log.d("MATE_DELETE", "메이트 삭제 완료")
                } else {
                    // 실패했을 경우 처리
                    Log.d("MATE_DELETE", "메이트 삭제 실패.")
                }
            }

            override fun onFailure(call: Call<DeleteResponse>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })

    }

}

