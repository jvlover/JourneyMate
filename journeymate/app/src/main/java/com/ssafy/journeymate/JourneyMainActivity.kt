package com.ssafy.journeymate

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.ssafy.journeymate.journey.MapFragment
import com.ssafy.journeymate.journey.RegistJourneyActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

class JourneyMainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_journey_main)
        val journeyMainTextView = findViewById<TextView>(R.id.journeymain)
        journeyMainTextView.text = "원하는 텍스트 내용"

        val moveToRegistButton = findViewById<Button>(R.id.moveToRegistButton)
        moveToRegistButton.setOnClickListener {
            val intent = Intent(this, RegistJourneyActivity::class.java)
            startActivity(intent)
        }

        val mapFragment = supportFragmentManager.findFragmentById(R.id.mapFragment) as MapFragment
        val markers: List<MapFragment.MarkerData> = getMarkersDataFromAPI(mapFragment)
        val scrollView = findViewById<ScrollView>(R.id.journeymainscrollView)
        val journeyMainLayout = LinearLayout(this).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            orientation = LinearLayout.VERTICAL
        }
        createJourneyScrollView(journeyMainLayout)
        scrollView.addView(journeyMainLayout)

    }

    private fun getMarkersDataFromAPI(mapFragment: MapFragment): List<MapFragment.MarkerData> {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://k9a204.p.ssafy.io:8000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(ApiService::class.java)
        val call = service.getJourneys("yourMateId") // yourMateId를 실제 MateId 값으로 대체해야 합니다.

        var markers: List<MapFragment.MarkerData> = emptyList()

        call.enqueue(object : Callback<JourneyResponse> {
            override fun onResponse(
                call: Call<JourneyResponse>,
                response: Response<JourneyResponse>
            ) {
                if (response.isSuccessful) {
                    val journeyResponse = response.body()
                    markers = journeyResponse?.journeys?.map { journey ->
                        MapFragment.MarkerData(
                            journey.xcoordinate.toDouble(),
                            journey.ycoordinate.toDouble(),
                            journey.title
                        )
                    } ?: emptyList()
                    mapFragment.setMarkersData(markers)
                } else {
                    Log.e("API_CALL", "Failed to retrieve journey info.")
                }
            }

            override fun onFailure(call: Call<JourneyResponse>, t: Throwable) {
                Log.e("API_CALL", "Failed to retrieve journey information. Error: ${t.message}")
            }
        })

        return markers
    }

    private fun createJourneyScrollView(journeyMainLayout: LinearLayout) {
        for (i in 0 until 10) {
            // 첫 번째 줄
            val firstLayout = LinearLayout(this).apply {
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                orientation = LinearLayout.HORIZONTAL
                setPadding(180, 6, 6, 6) // 왼쪽 마진 설정
            }

            if (i != 0) {

                val arrowLayout = LinearLayout(this).apply {
                    layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    )
                    orientation = LinearLayout.HORIZONTAL
                    setPadding(24, 6, 6, 6) // 왼쪽 마진 설정
                }


                val arrowImage = ImageView(this).apply {
                    setImageResource(R.drawable.category_1_tourism)
                    scaleType = ImageView.ScaleType.CENTER_CROP
                    val params = LinearLayout.LayoutParams(
                        TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_DIP,
                            36f,
                            resources.displayMetrics
                        ).toInt(),
                        TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_DIP,
                            36f,
                            resources.displayMetrics
                        ).toInt()
                    )
                    params.setMargins(0, 0, 0, 0) // 마진 설정
                    layoutParams = params
                }

                arrowLayout.addView(arrowImage)
                journeyMainLayout.addView(arrowLayout)
            }

            val firstImage = ImageView(this).apply {
                setImageResource(R.drawable.category_1_tourism)
                scaleType = ImageView.ScaleType.CENTER_CROP
                val params = LinearLayout.LayoutParams(
                    TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP,
                        36f,
                        resources.displayMetrics
                    ).toInt(),
                    TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP,
                        36f,
                        resources.displayMetrics
                    ).toInt()
                )
                params.setMargins(60, 0, 0, 0) // 마진 설정
                layoutParams = params
            }
            val firstText = TextView(this).apply {
                text = "날짜 $i"
                setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f) // 폰트 사이즈 설정
                val params = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                params.setMargins(6, 0, 0, 0) // 마진 설정
                layoutParams = params
            }

            firstLayout.addView(firstImage)
            firstLayout.addView(firstText)

            journeyMainLayout.addView(firstLayout)

            // 두 번째 줄
            val secondLayout = LinearLayout(this).apply {
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                orientation = LinearLayout.HORIZONTAL
                setPadding(30, 0, 0, 0) // 왼쪽 마진 설정
            }

            val secondImage = ImageView(this).apply {
                setImageResource(R.drawable.category_2_food)
                scaleType = ImageView.ScaleType.CENTER_CROP
                val params = LinearLayout.LayoutParams(
                    TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP,
                        36f,
                        resources.displayMetrics
                    ).toInt(),
                    TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP,
                        36f,
                        resources.displayMetrics
                    ).toInt()
                )
                layoutParams = params
            }

            val thirdImage = ImageView(this).apply {
                setImageResource(R.drawable.category_2_food)
                scaleType = ImageView.ScaleType.CENTER_CROP
                val params = LinearLayout.LayoutParams(
                    TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP,
                        36f,
                        resources.displayMetrics
                    ).toInt(),
                    TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP,
                        36f,
                        resources.displayMetrics
                    ).toInt()
                )
                layoutParams = params
            }

            val fourthImage = ImageView(this).apply {
                setImageResource(R.drawable.category_2_food)
                scaleType = ImageView.ScaleType.CENTER_CROP
                val params = LinearLayout.LayoutParams(
                    TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP,
                        36f,
                        resources.displayMetrics
                    ).toInt(),
                    TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP,
                        36f,
                        resources.displayMetrics
                    ).toInt()
                )
                layoutParams = params
            }


            val secondText = TextView(this).apply {
                text = "일정 title"
                background = getDrawable(R.drawable.rounded_small_corners) // 배경 설정
                val params = LinearLayout.LayoutParams(
                    TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP,
                        200f,
                        resources.displayMetrics
                    ).toInt(),
                    TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP,
                        36f,
                        resources.displayMetrics
                    ).toInt()
                )
                layoutParams = params
            }

            val firstButton = Button(this).apply {
                background = getDrawable(R.drawable.baseline_dehaze_24) // 배경 설정
                val params = LinearLayout.LayoutParams(
                    TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP,
                        36f,
                        resources.displayMetrics
                    ).toInt(),
                    TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP,
                        36f,
                        resources.displayMetrics
                    ).toInt()
                )
                params.setMargins(6, 0, 0, 0)
                layoutParams = params
            }

            secondLayout.addView(secondImage)
            secondLayout.addView(thirdImage)
            secondLayout.addView(fourthImage)
            secondLayout.addView(secondText)
            secondLayout.addView(firstButton)


            journeyMainLayout.addView(secondLayout)
        }
    }


    data class JourneyGetRes(
        val id: Long,
        val mateId: Long,
        val categoryId: Long,
        val title: String,
        val day: Int,
        val sequence: Int,
        val xcoordinate: Double,
        val ycoordinate: Double
    )

    data class JourneyResponse(
        val journeys: List<JourneyGetRes>
    )

    interface ApiService {
        @GET("journey-service/{mateId}")
        fun getJourneys(@Path("mateId") mateId: String): Call<JourneyResponse>
    }
}