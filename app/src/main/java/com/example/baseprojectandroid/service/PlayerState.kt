package com.example.baseprojectandroid.service

import androidx.media3.common.Player

data class PlayerState(
    var isPlaying: Boolean = false,
    var repeatMode: Int = Player.REPEAT_MODE_ALL,
    var isShuffle: Boolean = false
) {
    companion object {
        val DEFAULT = PlayerState()
    }
}