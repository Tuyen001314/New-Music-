package com.example.baseprojectandroid.utils

fun Long.timePositionToString(): String {
    val totalSecond = this / 1000
    val s = totalSecond % 60
    val m = (totalSecond / 60) % 60
    val h = totalSecond / 3600

    return if (h > 0) {
        String.format("%d:%02d:%02d", h, m, s)
    } else {
        String.format("%d:%02d", m, s)
    }
}