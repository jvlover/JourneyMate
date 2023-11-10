package com.ssafy.journeymate.journey

import android.content.Context
import android.location.Geocoder
import android.os.Bundle
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

class RegistMapFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnMapClickListener {

    lateinit var mContent: Context
    private lateinit var mapView: MapView
    private var mMap: GoogleMap? = null
    private var currentMarker: Marker? = null

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
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)
        return rootView
    }

    //지도 객체를 사용할 수 있을 때 자동으로 호출되는 함수
    override fun onMapReady(map: GoogleMap) {
        mMap = map
        val marker = LatLng(37.514655, 126.979974)
        map.addMarker(MarkerOptions().position(marker).title("기본위치").snippet("서울"))
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(marker, 11f))

        val markers = listOf(
            MarkerData(
                xcoordinate = 37.503325,
                ycoordinate = 127.044034,
                title = "기본위치2",
                snippet = "역삼"
            )
        )

        markers.forEach { markerData ->
            map.addMarker(
                MarkerOptions()
                    .position(LatLng(markerData.xcoordinate, markerData.ycoordinate))
                    .title(markerData.title)
                    .snippet(markerData.snippet)
            )
        }

        map.setOnMapClickListener(this)
    }

    override fun onMapClick(latLng: LatLng) {
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
        (activity as? RegistJourneyActivity)?.dimScreen()
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
        val snippet: String
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