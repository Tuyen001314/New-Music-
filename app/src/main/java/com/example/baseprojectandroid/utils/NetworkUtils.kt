package com.example.baseprojectandroid.utils

import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build

object NetworkUtils {

    lateinit var connectivityManager: ConnectivityManager
    @JvmStatic
    @Suppress("DEPRECATION")
    fun isNetworkConnected(): Boolean {
        val cm = connectivityManager
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val networkCapabilities = cm.activeNetwork ?: return false
            val nc = cm.getNetworkCapabilities(networkCapabilities) ?: return false
            when {
                nc.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                nc.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                nc.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            val activeNetwork = cm.activeNetworkInfo
            activeNetwork != null && activeNetwork.isConnectedOrConnecting
        }
    }

}