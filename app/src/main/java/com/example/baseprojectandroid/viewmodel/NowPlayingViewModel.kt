package com.example.baseprojectandroid.viewmodel

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import com.example.baseprojectandroid.model.Song
import com.example.baseprojectandroid.model.SongState
import com.example.baseprojectandroid.repository.SongRepository
import com.example.baseprojectandroid.service.MusicService
import com.example.baseprojectandroid.service.MusicServiceConnector
import com.example.baseprojectandroid.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class NowPlayingViewModel @Inject constructor(
    private val musicServiceConnector: MusicServiceConnector,
    private val songRepository: SongRepository
): BaseViewModel(), MusicServiceConnector.OnServiceConnected {
    private var isBound = false

    val currentSongState: StateFlow<SongState>?
        get() = musicServiceConnector.currentSongState

    init {
        musicServiceConnector.addOnConnectedListener(this)
    }

    fun pauseOrPlay() {
        musicServiceConnector.pauseOrPlay()
    }

    fun play(song: Song) {
        musicServiceConnector.play(song)
    }

    override fun onConnected() {
    }
}