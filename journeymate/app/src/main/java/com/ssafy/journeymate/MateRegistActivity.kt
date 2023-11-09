package com.ssafy.journeymate

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MateRegistActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mate_regist)

        // 프래그먼트 매니저를 사용하여 프래그먼트를 추가합니다.
        if (savedInstanceState == null) { // 액티비티가 재생성되지 않은 경우에만 프래그먼트 추가
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView, MateRegistFragment())
                .commit()
        }
        
    }
}