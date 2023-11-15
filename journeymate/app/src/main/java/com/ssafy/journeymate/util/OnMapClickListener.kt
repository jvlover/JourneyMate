package com.ssafy.journeymate.util

import com.google.android.gms.maps.model.LatLng

interface OnMapClickListener {
    fun setJourneyLatLng(latLng: LatLng)
}