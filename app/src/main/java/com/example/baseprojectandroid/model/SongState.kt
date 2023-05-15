package com.example.baseprojectandroid.model

data class SongState(
    val song: Song,
    val state: Int
) {
    companion object {
        val STATE_PLAYING = 0
        val STATE_PAUSE = 1
    }
}