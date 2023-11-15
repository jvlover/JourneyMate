package com.ssafy.journeymate.mate

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.GridLayout
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.ssafy.journeymate.R
import com.ssafy.journeymate.api.FindMateData
import com.ssafy.journeymate.api.FindMateResponse
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
            .baseUrl("http://k9a204.p.ssafy.io:8000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        userApi = retrofit.create(UserApi::class.java)

        val mateListLayout =
            findViewById<GridLayout>(R.id.mate_info_result)
        val inflater = LayoutInflater.from(this)

        val call = userApi.findMateById(userId)

        call.enqueue(object : Callback<FindMateResponse> {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onResponse(
                call: Call<FindMateResponse>,
                response: Response<FindMateResponse>
            ) {
                if (response.isSuccessful) {
                    val mateList = response.body()

                    mateList?.data?.forEachIndexed { index, findMateData ->
                        val mateView = createImageButton(findMateData)

                        val row = index / 2
                        val col = index % 2
                        val params = GridLayout.LayoutParams()
                        params.rowSpec = GridLayout.spec(row)
                        params.columnSpec = GridLayout.spec(col)

                        mateListLayout.addView(mateView, params)
                    }

                    Log.d("Response Body", response.toString())
                } else {
                    Log.e("MateListActivity", "Failed to fetch mate list: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<FindMateResponse>, t: Throwable) {
                Log.e("MateListActivity", "Failed to fetch mate list", t)
            }
        })


    }

    @SuppressLint("MissingInflatedId")
    @RequiresApi(Build.VERSION_CODES.O)
    private fun createImageButton(findMateData: FindMateData): View {
        val inflater = LayoutInflater.from(this)
        val view = inflater.inflate(R.layout.layout_mate_list_data, null)

        val mateInfoImageButton = view.findViewById<ImageButton>(R.id.mate_info)
        val mateNameTextView = view.findViewById<TextView>(R.id.mate_name)
        val mateDestinationTextView = view.findViewById<TextView>(R.id.mate_destination)
        val startDateTextView = view.findViewById<TextView>(R.id.mate_start_date_input)
        val endDateTextView = view.findViewById<TextView>(R.id.mate_end_date_input)
        val mateIdTextView = view.findViewById<TextView>(R.id.mate_id)


        mateInfoImageButton.setImageResource(R.drawable.blue_rectangle)
        mateNameTextView.text = findMateData.name

        mateDestinationTextView.text = findMateData.destination
        startDateTextView.text = findMateData.startDate.substringBefore("T")
        endDateTextView.text = findMateData.endDate.substringBefore("T")
        mateIdTextView.text = findMateData.mateId.toString()

        mateInfoImageButton.setOnClickListener {
            // 클릭된 mateInfoImageButton에 포함된 데이터를 Intent에 담아 다른 페이지로 전달
            val intent = Intent(this@MateListActivity, MateDetailActivity::class.java)
            intent.putExtra("mateData", findMateData)
            startActivity(intent)
        }


        val params = GridLayout.LayoutParams()
        params.rowSpec =
            GridLayout.spec(GridLayout.UNDEFINED, 1f) // 1f는 행의 비율을 나타내며, 모든 행의 비율을 동일하게 하려면 1f로 지정

        view.layoutParams = params

        return view
    }


}