package com.ssafy.journeymate.mate

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ssafy.journeymate.R
import com.ssafy.journeymate.api.LoadDocsListInfoResponse
import retrofit2.Call

class DocsListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_docs_list)


        // @GET("/mate-service/docs/list/{mateId}")
        //    fun loadDocsListInfo(@Path(value = "mateId") mateId: Long): Call<LoadDocsListInfoResponse>


        // DocsListData 개수 만큼 문서 나타나게 하기


    }
}