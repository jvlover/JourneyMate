package com.ssafy.journeymate.mate

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.GridView
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.ssafy.journeymate.R
import com.ssafy.journeymate.api.FindMateData
import com.ssafy.journeymate.api.MateApi
import retrofit2.Retrofit
import com.bumptech.glide.Glide
import com.ssafy.journeymate.api.LoadContentDetailInfoResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.converter.gson.GsonConverterFactory

class MateContentsActivity : AppCompatActivity() {

    private lateinit var retrofit: Retrofit
    private lateinit var mateApi: MateApi
    var mateData: FindMateData? = null

    companion object {
        private const val IMAGE_PICK_CODE = 1001
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mate_contents)

        val toolbarInclude = findViewById<View>(R.id.mate_contents_toolbar) as Toolbar

        val toolbarTitleTextView = toolbarInclude.findViewById<TextView>(R.id.toolbarTitle)

        toolbarTitleTextView.text = "여행 콘텐츠"

        val backButton: ImageButton = findViewById(R.id.backButton)
        backButton.setOnClickListener {
            // 뒤로 가기 버튼 클릭
            onBackPressed()
        }

        mateData = intent.getSerializableExtra("mateData") as FindMateData

        val imageUrls = listOf<String>() // API에서 가져와야 합니다.
        val gridView: GridView = findViewById(R.id.gallery_view)

        retrofit = Retrofit.Builder()
            .baseUrl("http://k9a204.p.ssafy.io:8000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        mateApi = retrofit.create(MateApi::class.java)

        val contentLoadCall = mateApi.loadContentDetailInfo(mateData!!.mateId)
//        contentLoadCall.enqueue(object : Callback<LoadContentDetailInfoResponse>{
//            override fun onResponse(
//                call: Call<LoadContentDetailInfoResponse>,
//                response: Response<LoadContentDetailInfoResponse>
//            ) {
//                Log.d("CONTENTS", "콘텐츠 불러오기 성공")
//
//            }
//
//            override fun onFailure(call: Call<LoadContentDetailInfoResponse>, t: Throwable) {
//                Log.d("CONTENT", "콘텐츠 요청 실패: ${t.message}")
//            }
//        })
//

//        gridView.adapter = ImageAdapter(this, imageUrls)

    }

    fun onAddImageClicked(view: View) {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//
//        if (requestCode == IMAGE_PICK_CODE && resultCode == RESULT_OK) {
//            // 여기에서 URI 리스트를 처리합니다.
//            val imageUris = ArrayList<Uri>()
//
//            if (data?.clipData != null) {
//                // 사용자가 여러 이미지를 선택했을 경우
//                val clipData = data.clipData!!
//                for (i in 0 until clipData.itemCount) {
//                    val imageUri = clipData.getItemAt(i).uri
//                    imageUris.add(imageUri)
//                }
//            } else if (data?.data != null) {
//                // 단일 이미지 선택
//                data.data?.let { uri ->
//                    imageUris.add(uri)
//                }
//            }
//
//            // 이미지 URI 리스트를 GridView의 Adapter에 전달하고 업데이트합니다.
//            val gridView: GridView = findViewById(R.id.gallery_view)
//            gridView.adapter = ImageAdapter(this, imageUris)
//        }


}

//class ImageAdapter(private val context: Context, private val imageUris: List<Uri>) : BaseAdapter() {
//    // ...
//
//    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
//        // ...
//        // URI를 사용하여 이미지를 ImageView에 로드합니다.
//        Glide.with(context)
//            .load(imageUris[position])
//            .into(imageView)
//        // ...
//    }
//}