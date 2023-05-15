package com.example.baseprojectandroid.service

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.example.baseprojectandroid.model.Song
import com.example.baseprojectandroid.model.SongState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class MusicService : Service(), CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Default
    private lateinit var exoPlayer: ExoPlayer

    private var currentSong = Song(
        name = "Ung qua chung",
        url = "https://firebasestorage.googleapis.com/v0/b/music-8fef0.appspot.com/o/music%2F%C6%AFng%20Qu%C3%A1%20Ch%E1%BB%ABng%20-%20AMEE.mp3?alt=media&token=3201a2c5-fbd4-4924-940e-dbadb6c91bb0"
    )
    private var currentSongState = SongState(
        currentSong,
        SongState.STATE_PAUSE
    )

    private val _currentState = MutableStateFlow(currentSongState)
    val currentState = _currentState.asStateFlow()

    private val binder = MusicBinder()

    inner class MusicBinder : Binder() {
        fun getService() = this@MusicService
    }

    override fun onBind(intent: Intent?): IBinder? {
        exoPlayer = ExoPlayer.Builder(this).build().apply {
            setMediaItem(MediaItem.fromUri(currentSong.url))
            prepare()
        }
        exoPlayer.addListener(object : Player.Listener {
            override fun onIsPlayingChanged(isPlaying: Boolean) {
                super.onIsPlayingChanged(isPlaying)
                launch {
                    currentSongState = currentSongState.copy(
                        state = if (isPlaying) SongState.STATE_PLAYING else SongState.STATE_PAUSE
                    )
                    _currentState.emit(currentSongState)
                }
            }
        })
        return binder
    }

    fun pauseOrPlay() {
        if (exoPlayer.isPlaying) {
            exoPlayer.pause()
        } else {
            exoPlayer.playWhenReady = true
            exoPlayer.play()
        }
    }

    fun play(song: Song) {
        //release current song
        exoPlayer.stop()

        currentSong = song
        //prepare now song
        exoPlayer.setMediaItem(song.toMediaItem())
        exoPlayer.playWhenReady = true
        exoPlayer.prepare()
    }

}
