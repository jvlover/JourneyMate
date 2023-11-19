package com.ssafy.journeymate.mate

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.BaseAdapter
import android.widget.GridView
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import com.bumptech.glide.Glide
import com.ssafy.journeymate.R
import com.ssafy.journeymate.api.ContentData
import com.ssafy.journeymate.api.FindMateData
import com.ssafy.journeymate.api.MateApi
import retrofit2.Retrofit
import com.ssafy.journeymate.api.LoadContentDetailInfoResponse
import com.ssafy.journeymate.api.RegistContentInfoResponse
import com.ssafy.journeymate.api.RegistContentsRequest
import com.ssafy.journeymate.global.App
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.io.FileOutputStream

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

        val mateName = findViewById<TextView>(R.id.mate_content_group_name)
        mateName.text = mateData!!.name

        retrofit = Retrofit.Builder()
            .baseUrl("http://k9a204.p.ssafy.io:8000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        mateApi = retrofit.create(MateApi::class.java)

        val gridView: GridView = findViewById(R.id.gallery_view)
        gridView.adapter = ImageAdapter(this, listOf())

        loadContents()


    }

    private fun updateGridView(imageUrls: List<String>) {
        val gridView: GridView = findViewById(R.id.gallery_view)
        (gridView.adapter as ImageAdapter).updateData(imageUrls)
    }

    fun onAddImageClicked(view: View) {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == IMAGE_PICK_CODE && resultCode == RESULT_OK) {
            val imageUris = ArrayList<Uri>()

            if (data?.clipData != null) {
                // 사용자가 여러 이미지를 선택했을 경우
                val clipData = data.clipData!!
                for (i in 0 until clipData.itemCount) {
                    val imageUri = clipData.getItemAt(i).uri
                    imageUris.add(imageUri)
                }
            } else if (data?.data != null) {
                // 단일 이미지 선택
                data.data?.let { uri ->
                    imageUris.add(uri)
                }
            }
            uploadContents(imageUris)

        }
    }

    private fun loadContents(){
        val contentLoadCall = mateApi.loadContentDetailInfo(mateData!!.mateId)
        contentLoadCall.enqueue(object : Callback<LoadContentDetailInfoResponse>{
            override fun onResponse(
                call: Call<LoadContentDetailInfoResponse>,
                response: Response<LoadContentDetailInfoResponse>
            ) {
                Log.d("CONTENTS", "콘텐츠 불러오기 성공")
                val contentDatas : List<ContentData>? = response.body()?.data?.contentInfo// API 응답에서 이미지 URL 리스트 추출

                val imgUrls = contentDatas?.map { it.imgUrl.toString() }
                if (imgUrls != null) {
                    updateGridView(imgUrls)
                }
            }

            override fun onFailure(call: Call<LoadContentDetailInfoResponse>, t: Throwable) {
                Log.d("CONTENT", "콘텐츠 요청 실패: ${t.message}")
            }
        })
    }

    private fun uploadContents(imageUris: ArrayList<Uri>) {
        val parts: MutableList<MultipartBody.Part> = mutableListOf()

        val requestMap = HashMap<String, RequestBody>().apply {
            put("mateId", RequestBody.create("text/plain".toMediaTypeOrNull(), mateData!!.mateId.toString()))
            put("userId", RequestBody.create("text/plain".toMediaTypeOrNull(), App.INSTANCE.id))
        }

        for (uri in imageUris) {
            val inputStream = contentResolver.openInputStream(uri)
            val file = File(cacheDir, "tempImage_${System.currentTimeMillis()}") // 고유한 파일 이름 생성
            FileOutputStream(file).use { outputStream ->
                inputStream?.copyTo(outputStream)
            }

            val requestFile = RequestBody.create("image/jpeg".toMediaTypeOrNull(), file)
            val body = MultipartBody.Part.createFormData("imgFile", file.name, requestFile)
            parts.add(body)
        }


        val call = mateApi.registContent(requestMap, parts)
        call.enqueue(object : Callback<RegistContentInfoResponse> {
            override fun onResponse(
                call: Call<RegistContentInfoResponse>,
                response: Response<RegistContentInfoResponse>
            ) {
                if (response.isSuccessful) {
                    Log.d("CONTENTS", "콘텐츠 저장하기 성공")
                    loadContents()
                } else {
                    Log.d("CONTENTS", "콘텐츠 저장하기 실패: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<RegistContentInfoResponse>, t: Throwable) {
                Log.d("CONTENT", "콘텐츠 저장하기 실패: ${t.message}")
            }
        })
    }

}
class ImageAdapter(private val context: Context, private var imageUrls: List<String>) : BaseAdapter() {

    override fun getCount(): Int {
        return imageUrls.size
    }

    override fun getItem(position: Int): Any {
        return imageUrls[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val imageView: ImageView
        if (convertView == null) {
            // 새 ImageView 생성
            imageView = ImageView(context)
            imageView.layoutParams = AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            imageView.adjustViewBounds = true
            imageView.scaleType = ImageView.ScaleType.FIT_CENTER
        } else {
            imageView = convertView as ImageView
        }

        // 이미지 로드
        Glide.with(context)
            .load(imageUrls[position])
            .into(imageView)

        return imageView
    }
    fun updateData(newImageUrls: List<String>) {
        imageUrls = newImageUrls
        notifyDataSetChanged()
    }
}