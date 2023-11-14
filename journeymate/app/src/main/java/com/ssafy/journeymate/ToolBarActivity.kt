package com.ssafy.journeymate

import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar


class ToolBarActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tool_bar)

        // Toolbar 설정
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        // 뒤로 가기 버튼 (내비게이션 버튼) 설정
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            // 뒤로 가기 버튼 클릭 시 수행할 액션
            onBackPressed()
        }

        // 제목 설정
        val toolbarTitle: TextView = findViewById(R.id.toolbarTitle)
        toolbarTitle.text = "여행 그룹 상세"

        // 메뉴 버튼 클릭 리스너 설정
        val menuButton: ImageButton = findViewById(R.id.menuButton)
        menuButton.setOnClickListener {
            // 메뉴 버튼 클릭 시 수행할 액션
            openMenu()
        }
    }

    private fun openMenu() {
        // 메뉴 열기 로직 구현
    }
}