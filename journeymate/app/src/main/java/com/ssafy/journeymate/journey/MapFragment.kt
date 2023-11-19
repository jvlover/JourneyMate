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
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.ssafy.journeymate.R
import com.ssafy.journeymate.api.JourneyGetRes

class MapFragment : Fragment(), OnMapReadyCallback {

    lateinit var mContent: Context
    private var markers: List<MarkerData>? = null
    private lateinit var mapView: MapView
    private var mMap: GoogleMap? = null

    fun setMarkersData(markerDataList: List<MarkerData>) {
        markers = markerDataList
    }

    interface OnMapClickListener {
        fun onMapClick()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_map, container, false)
        mapView = rootView.findViewById(R.id.mapFragment) as MapView
        val searchEditText = rootView.findViewById<EditText>(R.id.searchEditText)

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

        val journeys: List<JourneyGetRes>? =
            arguments?.getSerializable("journeys") as? List<JourneyGetRes>

        Log.i("마커 확인", "${journeys.toString()}")

        if (journeys != null) {
            Log.i("map fragment 확인", "${journeys.size}")
            var tempMarkers: MutableList<MarkerData> = mutableListOf()

            for (journey in journeys) {
                tempMarkers.add(MarkerData(journey.xcoordinate, journey.ycoordinate, journey.title))
            }
            setMarkersData(tempMarkers)
        }


        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)
        return rootView
    }

    private fun updateMarkers() {
        mMap?.let { map ->
            if (markers == null || markers!!.isEmpty()) {
                markers = listOf(
                    MarkerData(
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
        updateMarkers()
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
        val title: String
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

}