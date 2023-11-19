package com.ssafy.journeymate.mate

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.journeymate.PopupBarActivity
import com.ssafy.journeymate.R
import com.ssafy.journeymate.api.FindMateData
import com.ssafy.journeymate.api.MateApi
import com.ssafy.journeymate.api.RegistDocsResponse
import com.ssafy.journeymate.global.App
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.io.FileOutputStream

class DocsWriteActivity : AppCompatActivity() {

    private lateinit var retrofit: Retrofit
    private lateinit var mateApi: MateApi

    var mateData: FindMateData? = null
    private var selectedImageUri: Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_docs_write)

        val toolbarInclude = findViewById<View>(R.id.mate_contents_toolbar) as Toolbar

        val toolbarTitleTextView = toolbarInclude.findViewById<TextView>(R.id.toolbarTitle)

        toolbarTitleTextView.text = "여행 문서 작성"

        val backButton: ImageButton = findViewById(R.id.backButton)
        backButton.setOnClickListener {
            // 뒤로 가기 버튼 클릭
            onBackPressed()
        }

        val menuButton: ImageButton = findViewById(R.id.menuButton)
        menuButton.setOnClickListener {
            // 메뉴 버튼 클릭 시 수행할 액션
            startActivity(Intent(this, PopupBarActivity::class.java))
        }

        // mateId 전역변수 꺼내오기
        mateData = intent.getSerializableExtra("mateData") as FindMateData

        val AddBtn = findViewById<Button>(R.id.AddBtn)
        val docsBtn = findViewById<Button>(R.id.docs_regist_btn)

        AddBtn.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 100)
        }

        retrofit = Retrofit.Builder()
            .baseUrl("http://k9a204.p.ssafy.io:8000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        mateApi = retrofit.create(MateApi::class.java)

        docsBtn.setOnClickListener {

            val titleEditText: EditText = findViewById(R.id.mate_docs_title_input)
            val contentEditText: EditText = findViewById(R.id.mate_docs_content_input)

            val title = titleEditText.text.toString()
            val content = contentEditText.text.toString()

            val mateId = mateData!!.mateId
            val userId = App.INSTANCE.id

            val requestMap = HashMap<String, RequestBody>().apply {
                put("title", RequestBody.create("text/plain".toMediaTypeOrNull(), title))
                put("content", RequestBody.create("text/plain".toMediaTypeOrNull(), content))
                put("userId", RequestBody.create("text/plain".toMediaTypeOrNull(), userId))
                put("mateId", RequestBody.create("text/plain".toMediaTypeOrNull(), mateId.toString()))
            }

            val imagePart = selectedImageUri?.let { uri ->
                // 여기서 이미지 파일을 MultipartBody.Part로 변환
                val inputStream = contentResolver.openInputStream(uri)
                val file = File(cacheDir, "tempImage")
                val outputStream = FileOutputStream(file)
                inputStream?.copyTo(outputStream)

                val requestFile = RequestBody.create("image/jpeg".toMediaTypeOrNull(), file)
                MultipartBody.Part.createFormData("imgFile", file.name, requestFile)
            }

            val call = mateApi.registDocs(requestMap, listOfNotNull(imagePart))
            call.enqueue(object : Callback<RegistDocsResponse> {
                override fun onResponse(call: Call<RegistDocsResponse>, response: Response<RegistDocsResponse>) {
                    if (response.isSuccessful) {
                        Log.d("DOCS_WRITE", "문서 작성 완료")
                        val intent = Intent(this@DocsWriteActivity, DocsListActivity::class.java)
                        // 전역 변수에 mateId추가
                        intent.putExtra("mateData", mateData)
                        startActivity(intent)
                    } else {
                        Log.d("DOCS_WRITE", "문서 작성 실패!!")
                    }
                }

                override fun onFailure(call: Call<RegistDocsResponse>, t: Throwable) {
                    Log.d("DOCS_WRITE", "문서 작성 요청 실패: ${t.message}")
                }
            })



        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
            selectedImageUri = data?.data // 선택된 이미지의 URI

            // ImageView에 이미지 표시
            val imageView: ImageView = findViewById(R.id.selectedImageView)
            imageView.setImageURI(selectedImageUri)
        }
    }

}

