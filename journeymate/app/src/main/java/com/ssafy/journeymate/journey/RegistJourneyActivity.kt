package com.ssafy.journeymate.journey

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
import com.ssafy.journeymate.R
import com.ssafy.journeymate.api.JourneyApi
import com.ssafy.journeymate.api.JourneyRegistPostReq
import com.ssafy.journeymate.api.registJourneyResponse
import com.ssafy.journeymate.databinding.ActivityRegistJourneyBinding
import com.ssafy.journeymate.util.OnMapClickListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RegistJourneyActivity : AppCompatActivity(), OnMapClickListener {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityRegistJourneyBinding
    private lateinit var dimScreen: FrameLayout
    private lateinit var cardDimScreen: FrameLayout
    private lateinit var viewSwitcher: ConstraintLayout
    private lateinit var currentLatLag: LatLng
    private val coroutineScope = CoroutineScope(Dispatchers.Main)
    private var selectedCategory: Long = 0
    private lateinit var journeyApi: JourneyApi

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
        val editDayInput = findViewById<EditText>(R.id.editJourneyDay)
        val editSequenceInput = findViewById<EditText>(R.id.editJourneySequence)


        val moveToJourneyMainButton = findViewById<ImageButton>(R.id.backtojourneymainbutton)
        moveToJourneyMainButton.setOnClickListener {
            onBackPressed()
        }

        val mapFragment =
            supportFragmentManager.findFragmentById(R.id.registMapFragment) as RegistMapFragment
        mapFragment.setListener(this)

        dimScreen.setOnClickListener {
            Log.i("RegistJourneyActivity", "Click event on parentConstraintLayout")
            unDimScreen()
        }

        registJourneyButton.setOnClickListener() {
            try {
                val dayInputText = editDayInput.text.toString()
                val sequenceInputText = editSequenceInput.text.toString()

                val day = Integer.parseInt(dayInputText)
                val sequence = Integer.parseInt(sequenceInputText)

                Log.i("registJourneyButton 확인", "${editTitleInput.text}")

                /* registJourney api 보내기 */
                var journeyRegistPostReq = JourneyRegistPostReq(
                    1L,
                    selectedCategory,
                    "${editTitleInput.text}",
                    day,
                    sequence,
                    currentLatLag.latitude,
                    currentLatLag.longitude
                )
                registJourneyAPI(journeyRegistPostReq)

            } catch (e: NumberFormatException) {
                // 숫자로 변환할 수 없는 경우 예외처리
                Log.e("registJourneyButton 오류", "숫자로 변환할 수 없는 값이 입력되었습니다.")
            }

        }

        cardDimScreen.setOnClickListener {
            unDimScreen()
        }

        /* 아래 요소 눌리는거 차단용  */
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

                updateViewAppearance(view, textView, imageView, category?.id == selectedCategory)

                view.setOnClickListener {
                    selectedCategory = category?.id ?: 0
                    notifyDataSetChanged() // 뷰 업데이트 됐다고 notify 시켜주는 것
                }

                return view
            }
        }

        gridView.adapter = adapter

    }

    private fun updateViewAppearance(
        view: View,
        textView: TextView,
        imageView: ImageView,
        isSelected: Boolean
    ) {
        if (isSelected) {
            view.setBackgroundResource(R.drawable.rounded_small_corners_reverse)
            textView.setTextColor(Color.WHITE)
            imageView.setColorFilter(Color.WHITE)

        } else {
            view.setBackgroundResource(R.drawable.rounded_small_corners)
            textView.setTextColor(Color.BLACK)
            imageView.setColorFilter(Color.BLACK)
        }
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

    override fun onBackPressed() {
        // 여기에 뒤로 가기 버튼을 눌렀을 때 실행할 로직을 작성
        super.onBackPressed() // 기본 동작 (현재 액티비티 종료)
    }

    private fun registJourneyAPI(journeyRegistPostReq: JourneyRegistPostReq): Boolean {

        val retrofit = Retrofit.Builder()
            .baseUrl("http://k9a204.p.ssafy.io:8000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        journeyApi = retrofit.create(JourneyApi::class.java)

        journeyApi.registJourney(journeyRegistPostReq)
            .enqueue(object : Callback<registJourneyResponse> {
                override fun onResponse(
                    call: Call<registJourneyResponse>,
                    response: Response<registJourneyResponse>
                ) {
                    if (response.isSuccessful) {

                        val journeyData = response.body()?.data
                        Log.i("registJourneyAPI 성공", journeyData.toString())

                        val message = "일정이 등록되었습니다.."
                        Toast.makeText(this@RegistJourneyActivity, message, Toast.LENGTH_SHORT)
                            .show()
                        onBackPressed()

                    } else {
                        val errorMessage = response.errorBody()?.string()
                        Log.e("registJourneyAPI 에러", "에러 발생: $errorMessage")
                    }
                }

                override fun onFailure(call: Call<registJourneyResponse>, t: Throwable) {
                    Log.e("registJourneyAPI 실패", "에러 발생: ${t.message}")
                }

            })

        return true

    }


}