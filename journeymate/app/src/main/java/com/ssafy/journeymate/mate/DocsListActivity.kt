package com.ssafy.journeymate.mate

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.GridLayout
import android.widget.ImageButton
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.squareup.picasso.Picasso
import com.ssafy.journeymate.R
import com.ssafy.journeymate.api.DocsListData
import com.ssafy.journeymate.api.FindMateData
import com.ssafy.journeymate.api.LoadDocsListInfoResponse
import com.ssafy.journeymate.api.MateApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DocsListActivity : AppCompatActivity() {

    private lateinit var retrofit: Retrofit
    private lateinit var mateApi: MateApi

    var mateData: FindMateData? = null

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_docs_list)


        val toolbarInclude = findViewById<View>(R.id.mate_docs_list_toolbar) as Toolbar

        val toolbarTitleTextView = toolbarInclude.findViewById<TextView>(R.id.toolbarTitle)

        toolbarTitleTextView.text = "여행 문서 공유"

        val backButton: ImageButton = findViewById(R.id.backButton)
        backButton.setOnClickListener {
            // 뒤로 가기 버튼 클릭
            onBackPressed()
        }

        // mateId 전역변수 꺼내오기
        mateData = intent.getSerializableExtra("mateData") as FindMateData

        val docsListLayout =
            findViewById<GridLayout>(R.id.mate_docs_list_result)

        val groupName = findViewById<TextView>(R.id.docs_mate_name)
        groupName.text = mateData!!.name


        retrofit = Retrofit.Builder()
            .baseUrl("http://k9a204.p.ssafy.io:8000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        mateApi = retrofit.create(MateApi::class.java)

        val callMateDocsListLoad = mateApi.loadDocsListInfo(mateData!!.mateId)

        callMateDocsListLoad.enqueue(object : Callback<LoadDocsListInfoResponse> {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onResponse(
                call: Call<LoadDocsListInfoResponse>,
                response: Response<LoadDocsListInfoResponse>
            ) {
                if (response.isSuccessful) {
                    val docsList = response.body()

                    docsList?.data?.docsInfoList?.forEachIndexed { index, docsListData ->
                        val docsView = createImageButton(docsListData)

                        val row = index / 2
                        val col = index % 2
                        val params = GridLayout.LayoutParams()
                        params.rowSpec = GridLayout.spec(row)
                        params.columnSpec = GridLayout.spec(col)

                        docsListLayout.addView(docsView, params)
                    }

                    Log.d("Response Body", response.toString())
                } else {
                    Log.e("MateListActivity", "Failed to fetch mate list: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<LoadDocsListInfoResponse>, t: Throwable) {
                Log.e("MateListActivity", "Failed to fetch mate list", t)
            }
        })


        val writeButton = findViewById<ImageButton>(R.id.write_docs_button)
        writeButton.setOnClickListener {
            val intent = Intent(this@DocsListActivity, DocsWriteActivity::class.java)
            // 전역 변수에 mateId추가
            intent.putExtra("mateData", mateData)
            startActivity(intent)
        }
    }

    @SuppressLint("MissingInflatedId")
    @RequiresApi(Build.VERSION_CODES.O)
    private fun createImageButton(docsListData: DocsListData): View {
        val inflater = LayoutInflater.from(this)
        val view = inflater.inflate(R.layout.layout_mate_docs_data, null)

        val docsImageButton = view.findViewById<ImageButton>(R.id.docs_image)
        docsImageButton.setImageResource(R.drawable.docs_list_style)

        docsListData.imgFileInfo?.getOrNull(0)?.imgUrl?.let {
            Picasso.get().load(it).into(docsImageButton)
        }

        val docsTitle = view.findViewById<TextView>(R.id.docs_list_title)
        docsTitle.text = docsListData.title

        val docsCreatedDate = view.findViewById<TextView>(R.id.docs_list_created_Date)
        docsCreatedDate.text = docsListData.createdDate?.substringBefore("T")

        docsImageButton.setOnClickListener {
//             클릭된 docsImageButton에 포함된 데이터를 Intent에 담아 다른 페이지로 전달
//             DocsDetail로 이동으로 수정
            val intent = Intent(this@DocsListActivity, DocsDetailActivity::class.java)
            intent.putExtra("docsId", docsListData.docsId)
            startActivity(intent)
        }

        val params = GridLayout.LayoutParams()
        params.rowSpec =
            GridLayout.spec(GridLayout.UNDEFINED, 1f) // 1f는 행의 비율을 나타내며, 모든 행의 비율을 동일하게 하려면 1f로 지정

        view.layoutParams = params

        return view
    }
}

