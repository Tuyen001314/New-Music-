package com.example.baseprojectandroid.utils

object Constants {
    const val BASE_URL = "http://ec2-3-106-133-27.ap-southeast-2.compute.amazonaws.com:8080/"
    const val TIME_OUT: Long = 20_000

    val STORAGE_PERMISSION = arrayOf(
        android.Manifest.permission.READ_EXTERNAL_STORAGE,
        android.Manifest.permission.WRITE_EXTERNAL_STORAGE
    )
}