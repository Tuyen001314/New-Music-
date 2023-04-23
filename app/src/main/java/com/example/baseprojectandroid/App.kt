package com.example.baseprojectandroid

import android.app.Application


class App : Application() {

    companion object {
        lateinit var instance: App
    }
}