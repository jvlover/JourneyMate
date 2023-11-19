package com.ssafy.journeymate.mate

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.GridView
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.ssafy.journeymate.R

class MateContentsActivity : AppCompatActivity() {
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

//        val gridView = findViewById<GridView>(R.id.gridView)
//        // gridView에 이미지 어댑터 설정해야 함
//
//        val fab = findViewById<FloatingActionButton>(R.id.fab_add_image)
//        fab.setOnClickListener {
//            // 이미지 추가 버튼 클릭 이벤트 처리
//        }
    }


}
