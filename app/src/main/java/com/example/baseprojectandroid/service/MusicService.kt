package com.example.baseprojectandroid.service

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.support.v4.media.session.MediaSessionCompat
import androidx.core.content.ContextCompat
import androidx.media.MediaSessionManager
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import androidx.media3.ui.PlayerNotificationManager
import com.example.baseprojectandroid.model.Playlist
import com.example.baseprojectandroid.model.Position
import com.example.baseprojectandroid.model.Song
import com.example.baseprojectandroid.model.SongState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class MusicService : MediaSessionService(), CoroutineScope {
    private var needUpdateCurrentSongPosition = false
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Default
    private lateinit var exoPlayer: ExoPlayer

    private var currentSong = Song.EMPTY

    private val _currentState = MutableStateFlow(
        SongState(
            currentSong,
            SongState.STATE_PAUSE
        )
    )
    private val _currentPosition = MutableStateFlow(Position.NOTHING)

    private val _currentPlaylist = MutableStateFlow(Playlist())

    val currentState: StateFlow<SongState> get() = _currentState
    val currentSongPosition: StateFlow<Position> get() = _currentPosition
    val currentPlaylist: StateFlow<Playlist> get() = _currentPlaylist

    private lateinit var mediaSession: MediaSession
    private lateinit var musicNotificationManager: MusicNotificationManager

    private val binder = MusicBinder()

    inner class MusicBinder : Binder() {
        fun getService() = this@MusicService
    }

    override fun onBind(intent: Intent?): IBinder? {
//        mediaSession = MediaSessionCompat(this, "MusicService").apply {
//            val sessionActivityPendingIntent =
//                packageManager?.getLaunchIntentForPackage(packageName)?.let { sessionIntent ->
//                    PendingIntent.getActivity(this@MusicService, 0, sessionIntent, PendingIntent.FLAG_IMMUTABLE)
//                }
//            setSessionActivity(sessionActivityPendingIntent)
//            isActive = true
//        }
        val player = ExoPlayer.Builder(this).build()
        val mediaSession = MediaSession.Builder(this, player)
            .build()


        musicNotificationManager = MusicNotificationManager(this, mediaSession.sessionCompatToken)


        exoPlayer = ExoPlayer.Builder(this).build().apply {
            setMediaItem(MediaItem.fromUri(currentSong.url))
            prepare()
        }
        exoPlayer.addListener(object : Player.Listener {
            override fun onPlaybackStateChanged(playbackState: Int) {
                super.onPlaybackStateChanged(playbackState)
                if (playbackState == ExoPlayer.STATE_READY) {
                    musicNotificationManager.showNotificationForPlayer(exoPlayer)
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
                if (isPlaying) {
                    needUpdateCurrentSongPosition = true
                    updateCurrentPosition()
                } else {
                    needUpdateCurrentSongPosition = false
                }
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

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession? {
        TODO("Not yet implemented")
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
            _currentState.emit(currentSong.defaultState())
            _currentPosition.emit(Position.NOTHING)
        }
        //prepare now song
        exoPlayer.setMediaItem(song.toMediaItem())
        exoPlayer.playWhenReady = true
        exoPlayer.prepare()
    }

    private lateinit var playlistExoPlayerListener: Player.Listener
    private var currentSongIndexInPlayingPlaylist = 0

    fun play(playlist: Playlist, startSongIndex: Int = 0) {
        launch {
            _currentPlaylist.emit(playlist)
        }
        val startSong = playlist.songs[startSongIndex]
        currentSongIndexInPlayingPlaylist = 0
        try {
            exoPlayer.removeListener(playlistExoPlayerListener)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        play(startSong)
        playlistExoPlayerListener = object : Player.Listener {
            override fun onPlaybackStateChanged(playbackState: Int) {
                super.onPlaybackStateChanged(playbackState)
                when (playbackState) {
                    ExoPlayer.STATE_ENDED -> {
                        currentSongIndexInPlayingPlaylist++
                        try {
                            val song = playlist.songs[currentSongIndexInPlayingPlaylist]
                            play(song)
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }
            }
        }
    }

    fun updateCurrentSongOfPlaylist(index: Int) {
        try {
            val song = _currentPlaylist.value.songs[index]
            currentSongIndexInPlayingPlaylist = index
            play(song)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }


    fun updateCurrentPosition() {
        launch(Dispatchers.Main) {
            while (needUpdateCurrentSongPosition) {
                _currentPosition.update {
                    it.copy(
                        currentIndex = exoPlayer.currentPosition
                    )
                }
                delay(200)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        musicNotificationManager.hideNotification()
    }
}
