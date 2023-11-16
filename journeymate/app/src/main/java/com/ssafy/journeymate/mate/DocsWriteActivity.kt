package com.ssafy.journeymate.mate

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.ssafy.journeymate.R

class DocsWriteActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_docs_write)

        val toolbarInclude = findViewById<View>(R.id.mate_contents_toolbar) as Toolbar

        val toolbarTitleTextView = toolbarInclude.findViewById<TextView>(R.id.toolbarTitle)

        toolbarTitleTextView.text = "여행 문서 작성"
        val AddBtn = findViewById<Button>(R.id.AddBtn)
        val docsBtn = findViewById<Button>(R.id.docs_regist_btn)

        AddBtn.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 100)
        }

        docsBtn.setOnClickListener {

        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
            val selectedImageUri = data?.data // 선택된 이미지의 URI

            // ImageView에 이미지 표시
            val imageView: ImageView = findViewById(R.id.selectedImageView)
            imageView.setImageURI(selectedImageUri)
        }
    }

}
