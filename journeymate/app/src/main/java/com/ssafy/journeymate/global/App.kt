package com.ssafy.journeymate.global

import android.app.Application

class App : Application() {

    init {
        INSTANCE = this
    }

    override fun onCreate() {
        super.onCreate()
    }

    companion object {
        lateinit var INSTANCE: App
    }

    var id = ""

    var nickname = ""

    var profileImg = ""

    var accessToken = ""

    var mateId = ""

}