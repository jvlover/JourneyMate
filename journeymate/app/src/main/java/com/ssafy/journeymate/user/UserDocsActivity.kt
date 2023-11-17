package com.ssafy.journeymate.user

import android.annotation.SuppressLint
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
import com.ssafy.journeymate.api.FindDocsData
import com.ssafy.journeymate.api.FindDocsListResponse
import com.ssafy.journeymate.api.UserApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class UserDocsActivity : AppCompatActivity() {

    private lateinit var retrofit: Retrofit
    private lateinit var userApi: UserApi

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_docs)

        val toolbarInclude = findViewById<View>(R.id.user_docs_toolbar) as Toolbar

        val toolbarTitleTextView = toolbarInclude.findViewById<TextView>(R.id.toolbarTitle)

        toolbarTitleTextView.text = "유저 문서 목록"

        val docsListLayout =
            findViewById<GridLayout>(R.id.user_docs_result)

        retrofit = Retrofit.Builder()
            .baseUrl("http://k9a204.p.ssafy.io:8000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        userApi = retrofit.create(UserApi::class.java)

        val callDocsListLoad = userApi.findDocsById("11ee7ebf5b8bb5c8aa4bcb99876bba64")

        callDocsListLoad.enqueue(object : Callback<FindDocsListResponse> {

            @RequiresApi(Build.VERSION_CODES.O)
            override fun onResponse(
                call: Call<FindDocsListResponse>,
                response: Response<FindDocsListResponse>
            ) {
                if (response.isSuccessful) {
                    val findDocsListResponse = response.body()

                    findDocsListResponse?.data?.forEachIndexed { index, findDocsData ->
                        val docsView = createImageButton(findDocsData)

                        val row = index / 2
                        val col = index % 2
                        val params = GridLayout.LayoutParams()
                        params.rowSpec = GridLayout.spec(row)
                        params.columnSpec = GridLayout.spec(col)

                        docsListLayout.addView(docsView, params)
                    }

                    Log.d("Response Body", response.toString())
                } else {
                    Log.e("API 오류", "유저 문서 가져오는 api 오류: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<FindDocsListResponse>, t: Throwable) {
                Log.e("API 오류", "유저 문서 가져오는 api 오류", t)
            }
        })
    }

    @SuppressLint("MissingInflatedId")
    @RequiresApi(Build.VERSION_CODES.O)
    private fun createImageButton(findDocsData: FindDocsData): View {
        val inflater = LayoutInflater.from(this)
        val view = inflater.inflate(R.layout.layout_mate_docs_data, null)

        val docsImageButton = view.findViewById<ImageButton>(R.id.docs_image)
        docsImageButton.setImageResource(R.drawable.docs_list_style)

        findDocsData.imgFileInfo.let {
            Picasso.get().load(it).into(docsImageButton)
        }

        val docsTitle = view.findViewById<TextView>(R.id.docs_list_title)
        docsTitle.text = findDocsData.title

        val docsCreatedDate = view.findViewById<TextView>(R.id.docs_list_created_Date)
        docsCreatedDate.text = findDocsData.createdDate?.substringBefore("T")

        docsImageButton.setOnClickListener {
            // 클릭된 docsImageButton에 포함된 데이터를 Intent에 담아 다른 페이지로 전달
            // DocsDetail로 이동으로 수정
//                val intent = Intent(this@DocsListActivity, DocsDetailActivity::class.java)
//                // 전역 변수에 mateId추가
//                intent.putExtra("docsData", docsListData)
//                startActivity(intent)
        }

        val params = GridLayout.LayoutParams()
        params.rowSpec =
            GridLayout.spec(GridLayout.UNDEFINED, 1f) // 1f는 행의 비율을 나타내며, 모든 행의 비율을 동일하게 하려면 1f로 지정

        view.layoutParams = params

        return view
    }
}