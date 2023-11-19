package com.ssafy.journeymate.mate

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.ssafy.journeymate.PopupBarActivity
import com.ssafy.journeymate.R
import com.ssafy.journeymate.api.LoadDocsDetailInfoResponse
import com.ssafy.journeymate.api.MateApi
import com.ssafy.journeymate.databinding.ActivityDocsDetailBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL

class DocsDetailActivity : AppCompatActivity() {

    private val binding by lazy { ActivityDocsDetailBinding.inflate(layoutInflater) }
    private lateinit var retrofit: Retrofit
    private lateinit var mateApi: MateApi


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        var imageView: ImageView? = binding.imgDocsDetail;
        var bitmap: Bitmap? = null
        val docsId = intent.getLongExtra("docsId", 0)

        retrofit = Retrofit.Builder()
            .baseUrl("http://k9a204.p.ssafy.io:8000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        mateApi = retrofit.create(MateApi::class.java)

        val callDocsDetailLoad = mateApi.loadDocsDetailInfo(docsId)
        callDocsDetailLoad.enqueue(object : Callback<LoadDocsDetailInfoResponse> {
            override fun onResponse(
                call: Call<LoadDocsDetailInfoResponse>,
                response: Response<LoadDocsDetailInfoResponse>
            ) {
                if (response.isSuccessful) {
                    val docsDetailData = response.body()?.data
                    val txtDocsDetailName = findViewById<TextView>(R.id.txt_docs_detail_name)
                    txtDocsDetailName.text = docsDetailData?.title

                    val txtDocsDetailDate = findViewById<TextView>(R.id.txt_docs_detail_date)
                    txtDocsDetailDate.text = docsDetailData?.createdDate?.substringBefore("T") ?: "N/A"

                    val txtDocsDetailContent = findViewById<TextView>(R.id.txt_docs_detail_content)
                    txtDocsDetailContent.text = docsDetailData?.content

                    val uThread: Thread = object : Thread() {
                        override fun run() {
                            try {
                                val url = URL("${docsDetailData?.imgFileInfo?.get(0)?.imgUrl}")
                                val conn: HttpURLConnection =
                                    url.openConnection() as HttpURLConnection
                                conn.doInput = true // 서버로부터 응답 수신
                                conn.connect() // 연결된 곳에 접속할 때 (connect() 호출해야 실제 통신 가능함)

                                val `is`: InputStream = conn.inputStream // inputStream 값 가져오기
                                bitmap = BitmapFactory.decodeStream(`is`) // Bitmap으로 변환

                            } catch (e: MalformedURLException) {
                                e.printStackTrace()
                            } catch (e: IOException) {
                                e.printStackTrace()
                            }
                        }
                    }
                    uThread.start() // 작업 Thread 실행
                    try {
                        uThread.join()
                        imageView?.setImageBitmap(bitmap)
                    } catch (e: InterruptedException) {
                        e.printStackTrace()
                    }
                } else {
                    Log.e("error log", "실패했습니다. ${response.message()}")
                }
            }

            override fun onFailure(call: Call<LoadDocsDetailInfoResponse>, t: Throwable) {
                Log.e("error log", "실패했습니다.", t)
            }
        })
        val toolbarInclude = findViewById<View>(R.id.docs_Detail_toolbar) as Toolbar

        val toolbarTitleTextView = toolbarInclude.findViewById<TextView>(R.id.toolbarTitle)

        toolbarTitleTextView.text = "문서 상세 정보"

        val backButton: ImageButton = findViewById(R.id.backButton)
        backButton.setOnClickListener {
            // 뒤로 가기 버튼 클릭 시 수행할 액션
            onBackPressed()
        }
        val menuButton: ImageButton = findViewById(R.id.menuButton)
        menuButton.setOnClickListener {
            // 메뉴 버튼 클릭 시 수행할 액션
            startActivity(Intent(this, PopupBarActivity::class.java))
        }
    }
}