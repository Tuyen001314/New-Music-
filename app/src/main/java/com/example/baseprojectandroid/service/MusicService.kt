package com.example.baseprojectandroid.service

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.example.baseprojectandroid.model.Position
import com.example.baseprojectandroid.model.Song
import com.example.baseprojectandroid.model.SongState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
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

    private val _currentState = MutableStateFlow(
        SongState(
            currentSong,
            SongState.STATE_PAUSE
        )
    )
    private val _currentPosition = MutableStateFlow(Position.NOTHING)

    val currentState: StateFlow<SongState> get() = _currentState
    val currentSongPosition: StateFlow<Position> get() = _currentPosition

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
            override fun onPlaybackStateChanged(playbackState: Int) {
                super.onPlaybackStateChanged(playbackState)
                if (playbackState == ExoPlayer.STATE_READY) {
                    launch(Dispatchers.Main) {
                        _currentPosition.update {
                            it.copy(
                                duration = exoPlayer.duration
                            )
                        }
                    }
                }
            }

            override fun onIsPlayingChanged(isPlaying: Boolean) {
                super.onIsPlayingChanged(isPlaying)
                launch(Dispatchers.Main) {
                    _currentState.update {
                        it.copy(
                            state = if (isPlaying) SongState.STATE_PLAYING else SongState.STATE_PAUSE
                        )
                    }
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
        launch {
            _currentState.emit(currentSong.toDefaultState())
            _currentPosition.emit(Position.NOTHING)
        }
        //prepare now song
        exoPlayer.setMediaItem(song.toMediaItem())
        exoPlayer.playWhenReady = true
        exoPlayer.prepare()
    }

    fun updateCurrentPosition() {
        launch(Dispatchers.Main) {
            _currentPosition.update {
                it.copy(
                    currentIndex = exoPlayer.currentPosition
                )
            }
        }
    }

}
