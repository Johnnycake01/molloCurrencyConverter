package com.mollo.myapplication.di.application

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyConverterApp: Application() {
    companion object {
        @get:Synchronized
        lateinit var application: MyConverterApp
            private set
    }

    override fun onCreate() {
        super.onCreate()
        application = this
    }

}