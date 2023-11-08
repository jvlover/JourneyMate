package com.ssafy.journeymate

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler

class LoadScreanActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_load_screan)

        startLoading()
    }
    private fun startLoading() {
        val handler = Handler()
        handler.postDelayed({finish()},2000)
    }
}