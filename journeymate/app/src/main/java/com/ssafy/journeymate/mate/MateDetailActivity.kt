package com.ssafy.journeymate.mate

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import com.ssafy.journeymate.R
import com.ssafy.journeymate.api.FindMateData
import com.ssafy.journeymate.api.LoadMateInfoResponse
import com.ssafy.journeymate.api.MateApi
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

class MateDetailActivity : AppCompatActivity() {
    private lateinit var retrofit: Retrofit
    private lateinit var mateApi: MateApi

    var mateData: FindMateData? = null

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mate_detail)

        val toolbarInclude = findViewById<View>(R.id.mate_detail_toolbar) as Toolbar

        val toolbarTitleTextView = toolbarInclude.findViewById<TextView>(R.id.toolbarTitle)

        toolbarTitleTextView.text = "여행 그룹 상세"


        // mateId 변경 필요
        mateData = intent.getSerializableExtra("mateData") as FindMateData

        //Log.i("mateId 받아온 데이터 입니다", "${mateId} : 번호")

        retrofit = Retrofit.Builder()
            .baseUrl("http://k9a204.p.ssafy.io:8000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        mateApi = retrofit.create(MateApi::class.java)

        // API 호출
        val callMateDataLoad = mateApi.loadMateInfo(mateData!!.mateId)
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


                    val destinationTextView = findViewById<TextView>(R.id.destination_text_result)
                    destinationTextView.text = responseData?.destination

                    val startEndTextView = findViewById<TextView>(R.id.start_end_date_result)

                    if (startEndTextView != null && responseData != null) {
                        val startDate = responseData.startDate.substringBefore("T") ?: "N/A"
                        val endDate = responseData.endDate.substringBefore("T") ?: "N/A"
                        startEndTextView.text = "$startDate ~ $endDate"
                    } else {
                        Log.e("MateDetailActivity", "startEndTextView 또는 responseData null")
                    }

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
                .setNegativeButton("아니오", null)
                .show()
        }


        val mateDocsListButton: Button = findViewById(R.id.mate_group_document_btn)

        mateDocsListButton?.setOnClickListener {
            val intent = Intent(this@MateDetailActivity, DocsListActivity::class.java)
            // 전역 변수에 mateId추가
            intent.putExtra("mateData", mateData)
            startActivity(intent)
        }
    }

    fun deleteMateData() {

//        val retrofit = Retrofit.Builder()
//            .baseUrl("http://k9a204.p.ssafy.io:8000/")
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//
//        mateApi = retrofit.create(MateApi::class.java)
//
//
//        val deleteMateRequest = mateData?.let {
//            DeleteMateRequest(
//                mateId = it.mateId,
//                creator = "11ee7ebf5b8bb5c8aa4bcb99876bba64"
//                // creator = App.Instance.id
//            )
//        }
//
//        if (deleteMateRequest != null) {
//            mateApi.deleteMate(deleteMateRequest).enqueue(object : Callback<DeleteResponse> {
//                override fun onResponse(
//                    call: Call<DeleteResponse>,
//                    response: Response<DeleteResponse>
//                ) {
//                    if (response.isSuccessful) {
//                        // 성공적으로 삭제된 경우 처리
//                        Log.d("MATE_DELETE", "메이트 삭제 완료")
//                        val intent = Intent(this@MateDetailActivity, MateListActivity::class.java)
//                        startActivity(intent)
//                    } else {
//                        // 실패했을 경우 처리
//                        Log.d("MATE_DELETE", "메이트 삭제 실패.")
//                    }
//                }
//
//                override fun onFailure(call: Call<DeleteResponse>, t: Throwable) {
//                    TODO("Not yet implemented")
//                }
//            })
//        }

        val JSON = "application/json; charset=utf-8".toMediaType()

        // OkHttpClient 인스턴스 생성
        val client = OkHttpClient()

        val json = """{"mateId": "${mateData?.mateId}", "creator": "${mateData?.creator}"}"""
        val body = RequestBody.create(JSON, json)

        val request = Request.Builder()
            .url("http://k9a204.p.ssafy.io:8000/mate-service/delete")
            .delete(body)
            .build()

        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                if (response.isSuccessful) {
                    Log.d("MATE_DELETE", "메이트 삭제 완료")
                    val intent = Intent(this@MateDetailActivity, MateListActivity::class.java)
                    startActivity(intent)
                } else {
                    // 실패했을 경우 처리
                    Log.d("MATE_DELETE", "메이트 삭제 실패.")
                }
                response.close() // 항상 Response 객체를 닫아야 합니다
            }

            override fun onFailure(call: okhttp3.Call, e: IOException) {
                // 요청 실패 시 처리
                Log.d("MATE_DELETE", "메이트 삭제 요청 실패: ${e.message}")
            }
        })
    }

}

