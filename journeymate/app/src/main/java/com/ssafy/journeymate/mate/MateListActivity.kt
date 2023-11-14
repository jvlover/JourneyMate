package com.ssafy.journeymate.mate

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
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

    var bitmap: Bitmap? = null


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mate_list)

        val toolbarInclude = findViewById<View>(R.id.mate_list_toolbar) as Toolbar

        val toolbarTitleTextView = toolbarInclude.findViewById<TextView>(R.id.toolbarTitle)

        toolbarTitleTextView.text = "여행 그룹 목록"

        // userId 변경 필요
        // val userId = App.INSTANCE.id
        val userId = "11ee7ebf5b8bb5c8aa4bcb99876bba64"

        // nickname
        val nickname = findViewById<TextView>(R.id.user_nickname)
        // nickname.text = App.instance.nickname
        nickname.text = "travel"

        // img
        var imageView: ImageView? = findViewById(R.id.user_profile)

//        val uThread: Thread = object : Thread() {
//            override fun run() {
//                try {
//                    val url = URL("${App.INSTANCE.profileImg}")
//                    // 이미지 URL 경로
//
//                    // web에서 이미지를 가져와 ImageView에 저장할 Bitmap을 만든다.
//                    val conn: HttpURLConnection = url.openConnection() as HttpURLConnection
//                    conn.doInput = true // 서버로부터 응답 수신
//                    conn.connect() // 연결된 곳에 접속할 때 (connect() 호출해야 실제 통신 가능함)
//
//                    val `is`: InputStream = conn.inputStream // inputStream 값 가져오기
//                    bitmap = BitmapFactory.decodeStream(`is`) // Bitmap으로 변환
//
//                } catch (e: MalformedURLException) {
//                    e.printStackTrace()
//                } catch (e: IOException) {
//                    e.printStackTrace()
//                }
//            }
//        }
//        uThread.start() // 작업 Thread 실행
//        try {
//            // 메인 Thread는 별도의 작업 Thread가 작업을 완료할 때까지 대기해야 한다.
//            // join() 호출하여 별도의 작업 Thread가 종료될 때까지 메인 Thread가 기다리도록 한다.
//            // join() 메서드는 InterruptedException을 발생시킨다.
//            uThread.join()
//            // 작업 Thread에서 이미지를 불러오는 작업을 완료한 뒤
//            // UI 작업을 할 수 있는 메인 Thread에서 ImageView에 이미지 지정
//            imageView?.setImageBitmap(bitmap)
//        } catch (e: InterruptedException) {
//            e.printStackTrace()
//        }


        retrofit = Retrofit.Builder()
            .baseUrl("http://k9a204.p.ssafy.io:8000/user-service/mate/{$userId}/") // 여기에 실제 API 서버 주소를 넣어야 합니다.
            .addConverterFactory(GsonConverterFactory.create())
            .build()


        userApi = retrofit.create(UserApi::class.java)

        val mateListLayout: ConstraintLayout = findViewById<ConstraintLayout>(R.id.mate_list_layout)
        val inflater = LayoutInflater.from(this)

        val call = userApi.findMateById()
        call.enqueue(object : Callback<FindDocsListResponse> {
            override fun onResponse(
                call: Call<FindDocsListResponse>,
                response: Response<FindDocsListResponse>
            ) {
                if (response.isSuccessful) {
                    val responseData = response.body()?.data?.docsInfoList
                    responseData?.forEach { data ->

                        val view =
                            inflater.inflate(R.layout.layout_mate_list_data, mateListLayout, false)

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