package com.example.baseprojectandroid.model

import androidx.media3.common.MediaItem

data class Song(
    val id: String = "1",
    val name: String,
    val url: String,
    val thumbnailUrl: String? = null
) {
    companion object {
        val EMPTY = Song("", "", "")
    }

    fun toDefaultState() = SongState(this, SongState.STATE_PAUSE)

    fun toMediaItem() = MediaItem.fromUri(url)
}