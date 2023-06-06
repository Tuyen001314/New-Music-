package com.example.baseprojectandroid.service

import android.app.Service
import android.content.Intent
import android.media.MediaScannerConnection
import android.media.session.MediaSession
import android.os.Binder
import android.os.IBinder
import android.support.v4.media.session.MediaSessionCompat
import android.util.Log
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
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

class MusicService : Service(), CoroutineScope, MusicController {
    private val TAG = javaClass.name
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

    private lateinit var musicNotificationManager: MusicNotificationManager

    private val binder = MusicBinder()

    inner class MusicBinder : Binder() {
        fun getService() = this@MusicService
    }

    override fun onBind(intent: Intent?): IBinder? {
        val mediaSession = MediaSessionCompat(this, "fkdlsaj")
        musicNotificationManager = MusicNotificationManager(this, mediaSession.sessionToken)


        exoPlayer = ExoPlayer.Builder(this).build().apply {
            setMediaItem(MediaItem.fromUri(currentSong.url))
            prepare()
        }
        exoPlayer.addListener(object : Player.Listener {
            override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                super.onMediaItemTransition(mediaItem, reason)
                when(reason) {
                    Player.MEDIA_ITEM_TRANSITION_REASON_AUTO, Player.MEDIA_ITEM_TRANSITION_REASON_SEEK ->
                        launch {
                            val song = mediaItem!!.mediaMetadata.extras!!.get("song") as Song
                            _currentState.emit(song.defaultState())
                        }
                }
                Log.d(TAG, "onMediaItemTransition: ${mediaItem?.mediaMetadata?.title}  reason = $reason")
            }


            override fun onPlaybackStateChanged(playbackState: Int) {
                super.onPlaybackStateChanged(playbackState)
                if (playbackState == ExoPlayer.STATE_READY) {
//                    musicNotificationManager.showNotificationForPlayer(exoPlayer)
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

    override fun pauseOrPlay() {
        if (exoPlayer.isPlaying) {
            exoPlayer.pause()
        } else {
            exoPlayer.playWhenReady = true
            exoPlayer.play()
        }
    }

    override fun play(song: Song) {
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

    override fun play(playlist: Playlist, startSongIndex: Int) {
        launch {
            _currentPlaylist.emit(playlist)
        }
        exoPlayer.clearMediaItems()
        exoPlayer.addMediaItems(playlist.songs.map { it.toMediaItem() })
        exoPlayer.seekTo(startSongIndex, 0)
        exoPlayer.repeatMode = Player.REPEAT_MODE_ALL
        exoPlayer.prepare()
        exoPlayer.playWhenReady = true
    }

    override fun nextSong() {
        exoPlayer.seekToNextMediaItem()
    }

    override fun prevSong() {
        exoPlayer.seekToPreviousMediaItem()
//        exoPlayer.previous()
//        if (_currentPlaylist.value)
    }

    override fun updateCurrentSongOfPlaylist(index: Int) {
        try {
            val song = _currentPlaylist.value.songs[index]
            currentSongIndexInPlayingPlaylist = index
            play(song)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun updateCurrentPlaylist() {

    }

    override fun updatePosition(process: Int) {
        exoPlayer.seekTo((exoPlayer.duration * (process.toFloat() /100)).toLong())
    }


    override fun updateCurrentPosition() {
        launch(Dispatchers.Main) {
            while (needUpdateCurrentSongPosition) {
                _currentPosition.update {
                    it.copy(
                        currentIndex = exoPlayer.currentPosition
                    )
                }
                delay(500)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
//        musicNotificationManager.hideNotification()
    }
}
