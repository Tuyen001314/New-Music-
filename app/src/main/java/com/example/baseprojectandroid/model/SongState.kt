package com.example.baseprojectandroid.model

data class SongState(
    val song: Song,
    var state: Int
) {
    companion object {
        val STATE_PLAYING = 0
        val STATE_PAUSE = 1
    }
}

data class Position(
    var duration: Long,
    var currentIndex: Long
) {
    companion object {
        val NOTHING = Position(0, 0)
    }
}