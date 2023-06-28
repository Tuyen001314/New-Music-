package com.example.baseprojectandroid.ui

import com.example.baseprojectandroid.model.Song

data class SongUiState(
    val song: Song,
    val isPlaying: Boolean = false,
    val isDownloaded: Boolean = false
)