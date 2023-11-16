package com.ssafy.journeymate.journey

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.ssafy.journeymate.R
import com.ssafy.journeymate.api.JourneyApi
import com.ssafy.journeymate.api.JourneyGetRes
import com.ssafy.journeymate.api.LoadMateInfoResponse
import com.ssafy.journeymate.api.MateApi
import com.ssafy.journeymate.api.MateGroupData
import com.ssafy.journeymate.api.getMateJourneysResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.Serializable
import java.lang.Math.atan2
import java.lang.Math.cos
import java.lang.Math.sin
import java.lang.Math.sqrt
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import kotlin.math.pow

class JourneyMainActivity : AppCompatActivity() {

    private lateinit var journeyApi: JourneyApi
    private lateinit var mateApi: MateApi
    private lateinit var journeys: List<JourneyGetRes>
    private lateinit var mateInfo: MateGroupData;
    private lateinit var scrollView: ScrollView
    private lateinit var journeyMainLayout: LinearLayout
//    lateinit var markers: List<MapFragment.MarkerData>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_journey_main)
        scrollView = findViewById<ScrollView>(R.id.journeymainscrollView)
        journeyMainLayout = LinearLayout(this).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            orientation = LinearLayout.VERTICAL
        }

        val moveToRegistButton = findViewById<Button>(R.id.moveToRegistButton)
        moveToRegistButton.setOnClickListener {
            var intent = Intent(this, RegistJourneyActivity::class.java)
            startActivity(intent)
        }

        val moveToMateButton = findViewById<ImageButton>(R.id.movetomatebutton)
        moveToMateButton.setOnClickListener {
////            var intent = Intent(this, RegistJourneyActivity::class.java)
//            startActivity(intent)
        }

        /* 전역변수로 mateId 받을거임 */
        CoroutineScope(Dispatchers.Main).launch {
            val mateInfoResult = getMateInfoFromAPI(1)
            // mateInfo 호출 결과를 확인한 후에 getJourneysFromAPI 호출
            if (mateInfoResult != null) {
                getJourneysFromAPI(1)
            }
        }

    }


    private fun getMateInfoFromAPI(mateId: Long): Boolean {

        val retrofit = Retrofit.Builder()
            .baseUrl("http://k9a204.p.ssafy.io:8000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        mateApi = retrofit.create(MateApi::class.java)

        mateApi.loadMateInfo(mateId).enqueue(object : Callback<LoadMateInfoResponse> {
            override fun onResponse(
                call: Call<LoadMateInfoResponse>,
                response: Response<LoadMateInfoResponse>
            ) {
                if (response.isSuccessful) {

                    val mateData = response.body()?.data
                    if (mateData != null) {

                        mateInfo = mateData
                        Log.i("성공", "${mateInfo.toString()}")

                        val journeyMainTextView = findViewById<TextView>(R.id.journeymain)
                        journeyMainTextView.text = "${mateInfo.name}"

                        val journeydate = findViewById<TextView>(R.id.journeydate)
                        val mateStartDate = formateDate(mateInfo.startDate)
                        val mateEndDate = formateDate(mateInfo.endDate)
                        journeydate.text = "여행날짜($mateStartDate ~ $mateEndDate)"

                    } else {
                        // 응답이 성공적이지만, body가 null인 경우
                        Log.e("에러", "응답은 성공했지만 데이터가 없습니다.")
                    }
                } else {
                    val errorMessage = response.errorBody()?.string()
                    Log.e("에러", "Mate 검색 에러 발생: $errorMessage")
                }
            }

            override fun onFailure(call: Call<LoadMateInfoResponse>, t: Throwable) {
                Log.e("에러", "Mate 검색 에러 발생: ${t.message}")
            }

        })

        return true

    }

    private fun formateDate(currentDateTime: String): String {

        val year = currentDateTime.substring(2, 4)
        val month = currentDateTime.substring(5, 7)
        val day = currentDateTime.substring(8, 10)
        return "$year.$month.$day"
    }

    fun addDaysAndFormatDate(inputDate: String, daysToAdd: Int): String {
        // String을 LocalDateTime으로 변환
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
        val dateTime = LocalDateTime.parse(inputDate, formatter)

        // Day만큼 날짜를 더함
        val resultDateTime = dateTime.plus((daysToAdd - 1).toLong(), ChronoUnit.DAYS)

        // 포맷 변경 (MM-dd)
        val resultFormatter = DateTimeFormatter.ofPattern("MM-dd")
        return resultDateTime.format(resultFormatter)
    }

    private fun getJourneysFromAPI(mateId: Long): Boolean {

        val retrofit = Retrofit.Builder()
            .baseUrl("http://k9a204.p.ssafy.io:8000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        journeyApi = retrofit.create(JourneyApi::class.java)

        journeyApi.getMateJourneys(mateId).enqueue(object : Callback<getMateJourneysResponse> {
            override fun onResponse(
                call: Call<getMateJourneysResponse>,
                response: Response<getMateJourneysResponse>
            ) {
                if (response.isSuccessful) {

                    val journeysData = response.body()?.data
                    if (journeysData != null) {

                        journeys = journeysData
                        Log.i("성공", "${journeys.toString()}")
                        Log.i("사이즈 가져오기", "${journeys.size}")

                        val bundle = Bundle()
                        bundle.putSerializable("journeys", journeys as Serializable)
                        val mapFragment = MapFragment()
                        mapFragment.arguments = bundle

                        createJourneyScrollView(journeyMainLayout)
                        scrollView.addView(journeyMainLayout)

                        supportFragmentManager.beginTransaction()
                            .replace(R.id.mapFragment, mapFragment)
                            .commit()


                    } else {
                        // 응답이 성공적이지만, body가 null인 경우
                        Log.e("에러", "응답은 성공했지만 데이터가 없습니다.")
                    }
                } else {
                    val errorMessage = response.errorBody()?.string()
                    Log.e("에러", "Journey 검색 에러 발생: $errorMessage")
                }
            }

            override fun onFailure(call: Call<getMateJourneysResponse>, t: Throwable) {
                Log.e("에러", "user 검색 에러 발생: ${t.message}")
            }

        })

        return true

    }

    private fun createJourneyScrollView(journeyMainLayout: LinearLayout) {

        Log.i("스크롤 만들기 확인", "${journeys.size}")
        journeys.sortedWith(compareBy<JourneyGetRes> { it.day }.thenBy { it.sequence })

        val typeface: Typeface? = resources.getFont(R.font.pretendardregular)
        var previousColor: Int? = null

        for (i in 0 until journeys.size) {
            // 첫 번째 줄
            val firstLayout = LinearLayout(this).apply {
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                gravity = Gravity.CENTER_VERTICAL or Gravity.END

                orientation = LinearLayout.HORIZONTAL
                setPadding(0, 12, 0, 0) // 왼쪽 패딩 설정
            }

            if (i != 0) {

                val arrowImage = ImageView(this).apply {
                    setImageResource(R.drawable.baseline_arrow_downward_24)
                    scaleType = ImageView.ScaleType.CENTER_CROP
                    val params = LinearLayout.LayoutParams(
                        TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_DIP,
                            24f,
                            resources.displayMetrics
                        ).toInt(),
                        TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_DIP,
                            24f,
                            resources.displayMetrics
                        ).toInt()
                    )
                    params.setMargins(12, 0, 12, 0)
                    layoutParams = params
                }

                val rangeTextView = TextView(this).apply {

                    gravity = Gravity.CENTER_VERTICAL
                    val location1 =
                        Location(journeys.get(i - 1).xcoordinate, journeys.get(i - 1).ycoordinate)
                    val location2 =
                        Location(journeys.get(i).xcoordinate, journeys.get(i).ycoordinate)

                    val distance = haversineDistance(location1, location2)

                    text = "${distance}km" // 텍스트 설정
                    setTextColor(Color.BLACK)
                    setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f) // 텍스트 크기 설정
                    setTypeface(typeface, Typeface.NORMAL)
                    val params = LinearLayout.LayoutParams(
                        TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_DIP,
                            64f,
                            resources.displayMetrics
                        ).toInt(),
                        TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_DIP,
                            28f,
                            resources.displayMetrics
                        ).toInt()
                    )
                    params.setMargins(0, 0, 0, 0) // 마진 설정
                    layoutParams = params
                }

                firstLayout.addView(arrowImage)
                firstLayout.addView(rangeTextView)

            }

            val firstImage = LinearLayout(this).apply {
                gravity = Gravity.CENTER_HORIZONTAL
                gravity = Gravity.CENTER_VERTICAL
                setBackgroundResource(R.drawable.rounded_small_corners_black) // 배경 리소스 설정
                val params = LinearLayout.LayoutParams(
                    TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP,
                        78f,
                        resources.displayMetrics
                    ).toInt(),
                    TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP,
                        32f,
                        resources.displayMetrics
                    ).toInt()
                )
                params.setMargins(72, 0, 0, 0) // 마진 설정
                layoutParams = params
            }

            val firstImageView = ImageView(this).apply {
                setImageResource(R.drawable.baseline_send_24)
                scaleType = ImageView.ScaleType.CENTER_CROP
                val params = LinearLayout.LayoutParams(
                    TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP,
                        24f,
                        resources.displayMetrics
                    ).toInt(),
                    TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP,
                        24f,
                        resources.displayMetrics
                    ).toInt()
                )
                params.setMargins(12, 0, 0, 0)
                layoutParams = params
            }

            val firstTextView = TextView(this).apply {
                text = "DAY${journeys.get(i).day}" // 텍스트 설정
                setTextColor(Color.WHITE)
                setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f) // 텍스트 크기 설정
                setTypeface(typeface, Typeface.NORMAL)
            }

            firstImage.addView(firstImageView)
            firstImage.addView(firstTextView)

            val firstText = TextView(this).apply {
                text = "${addDaysAndFormatDate(mateInfo.startDate, journeys.get(i).day)}"
                setTypeface(typeface, Typeface.NORMAL)
                setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f) // 폰트 사이즈 설정
                val params = LinearLayout.LayoutParams(
                    TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP,
                        80f,
                        resources.displayMetrics
                    ).toInt(),
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                params.setMargins(16, 0, 260, 0) // 마진 설정
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
                setPadding(6, 12, 0, 0) // 왼쪽 마진 설정
                gravity = Gravity.CENTER_VERTICAL
            }

            /* 사실 텍스트임 */
            val secondImage = TextView(this).apply {
                setTypeface(typeface, Typeface.NORMAL)
                val drawable = GradientDrawable()
                drawable.shape = GradientDrawable.RECTANGLE
                drawable.cornerRadius = 12 * resources.displayMetrics.density
                drawable.setColor(Color.BLACK)
                background = drawable

                val params = LinearLayout.LayoutParams(
                    TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP,
                        16f,
                        resources.displayMetrics
                    ).toInt(),
                    TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP,
                        16f,
                        resources.displayMetrics
                    ).toInt()
                )
                val typeface: Typeface? = resources.getFont(R.font.pretendardregular)
                setTypeface(typeface, Typeface.NORMAL)
                layoutParams = params
            }

            /* 카테고리 아이콘 표시 */
            val thirdImage = ImageView(this).apply {

                var iconName: String = getIconByCategory(journeys.get(i).categoryId)
                setImageResource(resources.getIdentifier(iconName, "drawable", packageName))
                scaleType = ImageView.ScaleType.CENTER_CROP
                val params = LinearLayout.LayoutParams(
                    TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP,
                        48f,
                        resources.displayMetrics
                    ).toInt(),
                    TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP,
                        48f,
                        resources.displayMetrics
                    ).toInt()
                )
                params.setMargins(18, 0, 18, 0)
                layoutParams = params
            }

            /* 순서 표시, 배경색 랜덤 */
            val fourthImage = TextView(this).apply {
                gravity = Gravity.CENTER_VERTICAL
                gravity = Gravity.CENTER_HORIZONTAL
                text = "${i + 1}"
                setTextColor(Color.WHITE)
                setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f)
                setTypeface(typeface, Typeface.NORMAL)

                fun getRandomColor(): Int {
                    val colors =
                        listOf(
                            "#FFC107",
                            "#FF5722",
                            "#4CAF50",
                            "#9E9E9E",
                            "#3F51B5",
                            "#E91E63",
                            "#009688",
                            "#FF9800",
                            "#673AB7",
                            "#CDDC39"
                        )
                    val availableColors = colors.filter { Color.parseColor(it) != previousColor }
                    val randomColor = availableColors.random()
                    previousColor = Color.parseColor(randomColor)
                    return previousColor!!
                }

                val drawable = GradientDrawable()
                drawable.shape = GradientDrawable.RECTANGLE
                drawable.cornerRadius = 12 * resources.displayMetrics.density
                drawable.setColor(getRandomColor())
                background = drawable

                val params = LinearLayout.LayoutParams(
                    TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP,
                        24f,
                        resources.displayMetrics
                    ).toInt(),
                    TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP,
                        24f,
                        resources.displayMetrics
                    ).toInt()
                )
                params.setMargins(6, 0, 6, 0)
                layoutParams = params
            }


            val secondText = TextView(this).apply {
                text = "${journeys.get(i).title}"
                setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f)
                background = getDrawable(R.drawable.rounded_small_corners_gray) // 배경 설정
                val params = LinearLayout.LayoutParams(
                    TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP,
                        240f,
                        resources.displayMetrics
                    ).toInt(),
                    TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP,
                        32f,
                        resources.displayMetrics
                    ).toInt()
                )
                params.setMargins(20, 0, 0, 0)
                setPadding(24, 0, 0, 0)
                layoutParams = params
            }

            val firstButton = Button(this).apply {
                background = getDrawable(R.drawable.baseline_edit_24) // 배경 설정
                val params = LinearLayout.LayoutParams(
                    TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP,
                        24f,
                        resources.displayMetrics
                    ).toInt(),
                    TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP,
                        24f,
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

    data class Location(val latitude: Double, val longitude: Double)

    fun haversineDistance(location1: Location, location2: Location): String {
        val R = 6371.0 // 지구의 반지름 (단위: km)

        val lat1Rad = Math.toRadians(location1.latitude)
        val lon1Rad = Math.toRadians(location1.longitude)
        val lat2Rad = Math.toRadians(location2.latitude)
        val lon2Rad = Math.toRadians(location2.longitude)

        val dLat = lat2Rad - lat1Rad
        val dLon = lon2Rad - lon1Rad

        val a = sin(dLat / 2).pow(2) + cos(lat1Rad) * cos(lat2Rad) * sin(dLon / 2).pow(2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))

        val distance = R * c
        return String.format("%.1f", distance)
    }

    fun getIconByCategory(categoryId: Long): String {

        if (categoryId == 1L) return "category_1_tourism"
        if (categoryId == 2L) return "category_2_food"
        if (categoryId == 3L) return "category_3_shopping"
        if (categoryId == 4L) return "category_4_attraction"
        if (categoryId == 5L) return "category_5_hiking"
        if (categoryId == 6L) return "category_6_drive"
        if (categoryId == 7L) return "category_7_ski"
        if (categoryId == 8L) return "category_8_firecamp"
        if (categoryId == 9L) return "category_9_beach"
        if (categoryId == 10L) return "category_10_bike"
        if (categoryId == 11L) return "category_11_star"
        else return "category_12_pet"
    }

}