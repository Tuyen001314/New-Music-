package com.example.baseprojectandroid.viewmodel

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Handler
import androidx.lifecycle.viewModelScope
import androidx.palette.graphics.Palette
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.baseprojectandroid.R
import com.example.baseprojectandroid.model.Playlist
import com.example.baseprojectandroid.model.Position
import com.example.baseprojectandroid.model.Song
import com.example.baseprojectandroid.repository.song.SongRepository
import com.example.baseprojectandroid.service.MusicServiceConnector
import com.example.baseprojectandroid.service.PlayerState
import com.example.baseprojectandroid.ui.base.BaseViewModel
import com.example.baseprojectandroid.ui.event.UiEffect
import com.example.baseprojectandroid.utils.DataState
import com.example.baseprojectandroid.utils.Event
import com.example.baseprojectandroid.utils.eventOf
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NowPlayingViewModel @Inject constructor(
    private val musicServiceConnector: MusicServiceConnector,
    private val songRepository: SongRepository,
    private val glide: RequestManager
) : BaseViewModel(), MusicServiceConnector.OnServiceConnected {
    private var isBound = false
    private var needUpdateCurrentSongPosition = true
    private lateinit var workHandler: Handler

    val currentSong: StateFlow<Song>?
        get() = musicServiceConnector.currentSongState

    private val currentSongPosition: StateFlow<Position>?
        get() = musicServiceConnector.currentSongPosition

    val currentPlaylist: StateFlow<Playlist>?
        get() = musicServiceConnector.currentPlaylist

    val currentPlayerState: StateFlow<PlayerState>?
        get() = musicServiceConnector.currentPlayerState

    private val _uiState = MutableStateFlow(
        NowPlayingUiState(
            eventOf(Song.EMPTY),
            eventOf(Song.EMPTY.toSinglePlaylist()),
        )
    )
    val uiState: StateFlow<NowPlayingUiState> get() = _uiState

    private val _uiEffect: MutableStateFlow<Event<NowPlayingUiEffect?>> = MutableStateFlow(eventOf(null))
    val uiEffect: StateFlow<Event<NowPlayingUiEffect?>> get() = _uiEffect

    private val _thumbVibrantColor = MutableStateFlow(Color.TRANSPARENT)
    val thumbVibrantColor: StateFlow<Int> get() = _thumbVibrantColor

    init {
        musicServiceConnector.addOnConnectedListener(this)
        viewModelScope.launch(Dispatchers.Main) {
            workHandler = Handler()
        }
    }

    fun pauseOrPlay() {
        musicServiceConnector.pauseOrPlay()
    }

    fun play(song: Song) {
        musicServiceConnector.play(song)
    }

    fun updatePosition(process: Int) {
        musicServiceConnector.updatePosition(process)
    }

    fun nextSong() {
        musicServiceConnector.nextSong()
    }

    fun prevSong() {
        musicServiceConnector.prevSong()
    }

    fun onClickRepeat() {
        musicServiceConnector.toggleRepeat()
    }

    fun onClickShuffle() {
        musicServiceConnector.toggleRepeat()
    }

    fun updateCurrentSongOfPlaylist(index: Int) =
        musicServiceConnector.updateCurrentSongOfPlaylist(index)

    //Collect all changes of MusicService, and update into uiState
    override fun onConnected() {

        viewModelScope.launch {
            launch {
                songRepository.getAllSong().collect {
                    when (it) {
                        is DataState.Success -> {
                            musicServiceConnector.play(Playlist(songs = it.data!!), 0)
                        }

                        else -> Unit
                    }
                }
            }

            launch {
                currentSongPosition!!.collect { position ->
                    _uiState.update {
                        it.copy(
                            currentPosition = position
                        )
                    }
                }
            }

            launch {
                currentSong!!.collect { song ->
                    _uiState.update {
                        it.copy(
                            song = eventOf(song)
                        )
                    }
                    glide.asBitmap()
                        .load(song.thumbnailUrl)
                        .thumbnail(glide.asBitmap().load(R.drawable.ic_thumbnail_default))
                        .override(100, 100)
                        .error(R.drawable.ic_thumbnail_default)
                        .into(object : CustomTarget<Bitmap>() {
                            override fun onResourceReady(
                                resource: Bitmap,
                                transition: Transition<in Bitmap>?
                            ) {
                                Palette.from(resource).generate {
                                    it?.getVibrantColor(Color.parseColor("#86929A"))?.let {
                                        launch {
                                            _thumbVibrantColor.emit(it)
                                        }
                                    }
                                }
                            }

                            override fun onLoadCleared(placeholder: Drawable?) {
                            }
                        })
                }
            }

            launch {
                currentPlaylist!!.collect { playlist ->
                    _uiState.update {
                        it.copy(
                            currentPlaylist = eventOf(playlist)
                        )
                    }
                }
            }

            launch {
                currentPlayerState!!.collect { playerState ->
                    _uiState.update {
                        it.copy(
                            currentState = eventOf(playerState)
                        )
                    }
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        needUpdateCurrentSongPosition = false
    }

    fun onClickShowTrackOption() {
        viewModelScope.launch {
            _uiEffect.emit(eventOf(NowPlayingUiEffect.ShowTrackOption(currentSong!!.value)))
        }
    }

}

data class NowPlayingUiState(
    var song: Event<Song>,
    var currentPlaylist: Event<Playlist>,
    var currentPosition: Position = Position.NOTHING,
    var currentState: Event<PlayerState> = eventOf(PlayerState.DEFAULT)
) {
    val currentSongIndexInPlayingPlaylist: Int
        get() =
            currentPlaylist.getValue().songs.run {
                indexOf(find { it.url == song.getValue().url })
            }
}

sealed class NowPlayingUiEffect: UiEffect {
    class ShowTrackOption(val song: Song): NowPlayingUiEffect()
}