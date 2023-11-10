package com.ssafy.journeymate.journey

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.ViewSwitcher
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.ui.AppBarConfiguration
import com.ssafy.journeymate.MainActivity
import com.ssafy.journeymate.R
import com.ssafy.journeymate.databinding.ActivityRegistJourneyBinding

class RegistJourneyActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityRegistJourneyBinding
    private lateinit var dimScreen: FrameLayout
    private lateinit var viewSwitcher: ViewSwitcher

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_regist_journey)

        // 변수 초기화
        dimScreen = findViewById(R.id.dim_screen)
        viewSwitcher = findViewById(R.id.journeyregistviewswitcher)

        val moveToJourneyMainButton = findViewById<ImageButton>(R.id.backtojourneymainbutton)
        moveToJourneyMainButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            val options = ActivityOptions.makeSceneTransitionAnimation(this).toBundle()
            startActivity(intent, options)
        }

    }

    fun dimScreen() {
        dimScreen.visibility = View.VISIBLE
        viewSwitcher.visibility = View.VISIBLE
    }

    fun unDimScreen() {
        dimScreen.visibility = View.GONE
        viewSwitcher.visibility = View.GONE
    }
}