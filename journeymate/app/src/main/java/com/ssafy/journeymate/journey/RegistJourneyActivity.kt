package com.ssafy.journeymate.journey

import android.app.ActivityOptions
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.GridView
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.ui.AppBarConfiguration
import com.google.android.gms.maps.model.LatLng
import com.ssafy.journeymate.JourneyMainActivity
import com.ssafy.journeymate.R
import com.ssafy.journeymate.databinding.ActivityRegistJourneyBinding
import com.ssafy.journeymate.util.OnMapClickListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class RegistJourneyActivity : AppCompatActivity(), OnMapClickListener {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityRegistJourneyBinding
    private lateinit var dimScreen: FrameLayout
    private lateinit var cardDimScreen: FrameLayout
    private lateinit var viewSwitcher: ConstraintLayout
    private lateinit var currentLatLag: LatLng
    private val coroutineScope = CoroutineScope(Dispatchers.Main)
    private var selectedCategory: Long = 0

    /* 카테고리명 다른 api 다하면 가장 마지막에 변경 예정 */
    private val categorys: List<Category> = listOf(
        Category(1, "category_1_tourism", "관광"),
        Category(2, "category_2_food", "음식"),
        Category(3, "category_3_shopping", "쇼핑"),
        Category(4, "category_4_attraction", "테마파크"),
        Category(5, "category_5_hiking", "등산"),
        Category(6, "category_6_drive", "드라이브"),
        Category(7, "category_7_ski", "스키"),
        Category(8, "category_8_firecamp", "캠핑"),
        Category(9, "category_9_beach", "바다"),
        Category(10, "category_10_bike", "자전거"),
        Category(11, "category_11_star", "별관측"),
        Category(12, "category_12_pet", "애견여행")
    )

    companion object {
        @JvmStatic
        var isDimScreen: Boolean = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_regist_journey)
        // 변수 초기화
        dimScreen = findViewById(R.id.dim_screen)
        cardDimScreen = findViewById(R.id.dim_screencard)
        viewSwitcher = findViewById(R.id.journeyregistviewswitcher)
        val registJourneyButton = findViewById<Button>(R.id.registJourneyButton)
        val editTitleInput = findViewById<EditText>(R.id.editJourneyTitleText)


        val moveToJourneyMainButton = findViewById<ImageButton>(R.id.backtojourneymainbutton)
        moveToJourneyMainButton.setOnClickListener {
            val intent = Intent(this, JourneyMainActivity::class.java)
            val options = ActivityOptions.makeSceneTransitionAnimation(this).toBundle()
            startActivity(intent, options)
        }

        val mapFragment =
            supportFragmentManager.findFragmentById(R.id.registMapFragment) as RegistMapFragment
        mapFragment.setListener(this)

        dimScreen.setOnClickListener {
            Log.i("RegistJourneyActivity", "Click event on parentConstraintLayout")
            unDimScreen()
        }

        registJourneyButton.setOnClickListener() {
            Log.i("텍스트", "${editTitleInput.text}")
        }

        cardDimScreen.setOnClickListener {
            unDimScreen()
        }

        viewSwitcher.setOnClickListener() {
        }


        var gridView = findViewById<GridView>(R.id.gridView)

        val adapter = object : ArrayAdapter<Category>(this, R.layout.item_category, categorys) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = convertView ?: LayoutInflater.from(context)
                    .inflate(R.layout.item_category, parent, false)
                val category = getItem(position)

                val imageView = view.findViewById<ImageView>(R.id.categoryIcon)
                val textView = view.findViewById<TextView>(R.id.categoryName)

                val imageResource = resources.getIdentifier(category?.icon, "drawable", packageName)
                imageView.setImageResource(imageResource)

                textView.text = category?.name

                if (category?.id == selectedCategory) {
                    view.setBackgroundResource(R.drawable.rounded_small_corners_reverse)
                    textView.setTextColor(Color.WHITE)
                } else {
                    view.setBackgroundResource(R.drawable.rounded_small_corners)
                    textView.setTextColor(Color.BLACK)
                }

                // 클릭 이벤트 처리
                view.setOnClickListener {
                    Log.i("gridcheck", "${category?.id}")
                    selectedCategory = category?.id ?: 0

                    // 선택된 아이템의 배경과 텍스트 색상 변경
                    view.setBackgroundResource(R.drawable.rounded_small_corners_reverse)
                    textView.setTextColor(Color.WHITE) // 텍스트 색상 변경

                    // 다른 아이템들의 배경과 텍스트 색상을 원래대로 되돌림
                    for (i in 0 until parent.childCount) {
                        if (i != position) {
                            val otherView = parent.getChildAt(i)
                            otherView.setBackgroundResource(R.drawable.rounded_small_corners) // 원래의 배경 리소스로 변경
                            val otherTextView = otherView.findViewById<TextView>(R.id.categoryName)
                            otherTextView.setTextColor(Color.BLACK) // 원래의 텍스트 색상으로 변경
                        }
                    }
                }


                return view
            }
        }

        gridView.adapter = adapter

    }


    fun dimScreen() {

        if (!isDimScreen) {
            isDimScreen = true
            coroutineScope.launch {
                delay(600) // 0.6초 지연
                dimScreen.visibility = View.VISIBLE
                cardDimScreen.visibility = View.VISIBLE
                viewSwitcher.visibility = View.VISIBLE
            }
        }
    }

    fun unDimScreen() {
        if (isDimScreen) {
            isDimScreen = false
            val message = "일정 위치가 취소되었습니다.."
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            dimScreen.visibility = View.GONE
            cardDimScreen.visibility = View.GONE
            viewSwitcher.visibility = View.GONE
        }
    }

    override fun setJourneyLatLng(latLng: LatLng) {
        currentLatLag = latLng
        val message = "일정 위치가 등록되었습니다."
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        Log.i("JourneyLatLng", message)
    }

    override fun onDestroy() {
        super.onDestroy()
        coroutineScope.cancel() // 액티비티 종료시 코루틴도 함께 종료
    }

    data class Category(
        val id: Long,
        val icon: String,
        val name: String
    )


}