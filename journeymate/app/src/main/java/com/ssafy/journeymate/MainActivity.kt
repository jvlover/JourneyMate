package com.ssafy.journeymate

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
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

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val journeyMainTextView = findViewById<TextView>(R.id.journeymain)
        journeyMainTextView.text = "원하는 텍스트 내용"

        val moveToRegistButton = findViewById<Button>(R.id.moveToRegistButton)
        moveToRegistButton.setOnClickListener {
            val intent = Intent(this, RegistJourneyActivity::class.java)
            startActivity(intent)
        }

        val mapFragment = supportFragmentManager.findFragmentById(R.id.mapFragment) as MapFragment
        val markers: List<MapFragment.MarkerData> = getMarkersDataFromAPI(mapFragment)

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