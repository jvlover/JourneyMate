package com.ssafy.journeymate.journey

import android.content.Context
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.ssafy.journeymate.R
import com.ssafy.journeymate.api.JourneyApi
import com.ssafy.journeymate.api.JourneyGetRes
import com.ssafy.journeymate.api.getMateJourneysResponse
import com.ssafy.journeymate.global.App
import com.ssafy.journeymate.util.OnMapClickListener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RegistMapFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnMapClickListener {

    lateinit var mContent: Context
    private lateinit var mapView: MapView
    private var mMap: GoogleMap? = null
    var currentMarker: Marker? = null
    private var mapClickListener: OnMapClickListener? = null
    private var markers: List<MapFragment.MarkerData>? = null
    private lateinit var journeyApi: JourneyApi
    var journeys: List<JourneyGetRes>? = null


    fun setMarkersData(markerDataList: List<MapFragment.MarkerData>) {
        markers = markerDataList
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val rootView = inflater.inflate(R.layout.fragment_regist_map, container, false)
        val searchEditText = rootView.findViewById<EditText>(R.id.searchRegistEditText)
        searchEditText.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val searchText = v.text.toString()
                if (searchText.isNotBlank()) {
                    moveToLocationByName(searchText)
                }
                true
            } else {
                false
            }
        }
        mapView = rootView.findViewById(R.id.registMapFragment) as MapView
        Log.i("RegistMap 클릭 확인", journeys.toString())
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)
        return rootView
    }

    private fun updateMarkers() {
        mMap?.let { map ->
            if (markers == null || markers!!.isEmpty()) {
                markers = listOf(
                    MapFragment.MarkerData(
                        xcoordinate = 37.503325,
                        ycoordinate = 127.044034,
                        title = "기본위치",
                    )
                )
                val startMarker = LatLng(markers!![0].xcoordinate, markers!![0].ycoordinate)
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(startMarker, 11f))

            }

            markers?.let { markerList ->
                if (markerList.isNotEmpty()) {
                    markerList.forEach { markerData ->
                        map.addMarker(
                            MarkerOptions()
                                .position(LatLng(markerData.xcoordinate, markerData.ycoordinate))
                                .title(markerData.title)
                        )
                    }
                    val startMarker = LatLng(markerList[0].xcoordinate, markerList[0].ycoordinate)
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(startMarker, 11f))
                }
            }
        }
    }

    //지도 객체를 사용할 수 있을 때 자동으로 호출되는 함수
    override fun onMapReady(map: GoogleMap) {
        mMap = map
        getJourneysFromAPI(App.INSTANCE.mateId.toLong())
        mMap?.setOnMapClickListener(this)
    }

    override fun onMapClick(latLng: LatLng) {

        Log.i("RegistMap 클릭 확인", latLng.toString())

        if (!RegistJourneyActivity.isDimScreen) {

            // 기존 마커 제거
            currentMarker?.remove()
            val title = "새로운 일정"
            val snippet = "두근두근! 저니메이트!"

            // 클릭한 위치에 마커 추가
            currentMarker =
                mMap?.addMarker(
                    MarkerOptions().position(latLng).title(title).snippet(snippet).icon(
                        BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)
                    )
                )
            // 화면 어둡게 하기
            (activity as? RegistJourneyActivity)?.dimScreen()

            //Activity에 데이터 전달
            mapClickListener?.setJourneyLatLng(latLng)
        }
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
                        Log.i("registmap 성공", "${journeys.toString()}")
                        Log.i("registmap 사이즈 가져오기", "${journeys!!.size}")
                        // 데이터를 받아서 저장하는 로직
                        if (journeys != null) {
                            var tempMarkers: MutableList<MapFragment.MarkerData> = mutableListOf()
                            for (journey in journeys!!) {
                                tempMarkers.add(
                                    MapFragment.MarkerData(
                                        journey.xcoordinate,
                                        journey.ycoordinate,
                                        journey.title
                                    )
                                )
                            }
                            setMarkersData(tempMarkers)

                            updateMarkers()
                        }


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


    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    data class MarkerData(
        val xcoordinate: Double,
        val ycoordinate: Double,
        val title: String,
    )

    private fun moveToLocationByName(name: String) {
        context?.let {
            val geocoder = Geocoder(it)
            Thread {
                val addresses = geocoder.getFromLocationName(name, 1)
                if (addresses != null && addresses.isNotEmpty()) {
                    val address = addresses[0]
                    val latLng = LatLng(address.latitude, address.longitude)
                    activity?.runOnUiThread {
                        mMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 11f))
                    }
                }
            }.start()
        }
    }

    fun setListener(listener: OnMapClickListener) {
        this.mapClickListener = listener
    }


}