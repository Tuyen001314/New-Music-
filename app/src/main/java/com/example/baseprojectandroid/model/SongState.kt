package com.example.baseprojectandroid.model

data class Position(
    var duration: Long,
    var currentIndex: Long
) {
    companion object {
        val NOTHING = Position(0, 0)
    }
}