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
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.ssafy.journeymate.R

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

        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)
        return rootView
    }

    //지도 객체를 사용할 수 있을 때 자동으로 호출되는 함수
    override fun onMapReady(map: GoogleMap) {
        mMap = map
        val marker = LatLng(37.514655, 126.979974)
        map.addMarker(
            MarkerOptions().position(marker).title("기본위치").snippet("서울")
        )
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(marker, 11f))

        if (markers == null) {
            markers = listOf(
                MarkerData(
                    xcoordinate = 37.503325,
                    ycoordinate = 127.044034,
                    title = "기본위치2",
                )
            )
        }

        markers!!.forEach { markerData ->
            map.addMarker(
                MarkerOptions()
                    .position(LatLng(markerData.xcoordinate, markerData.ycoordinate))
                    .title(markerData.title)
            )
        }

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