package com.example.baseprojectandroid

import android.app.Application
import com.example.baseprojectandroid.local.LocalStorage
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class App : Application() {

    @Inject
    lateinit var localStorage: LocalStorage

    companion object {
        lateinit var instance: App
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}