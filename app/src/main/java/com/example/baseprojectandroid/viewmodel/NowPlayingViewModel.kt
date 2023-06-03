package com.example.baseprojectandroid.viewmodel

import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Handler
import androidx.lifecycle.viewModelScope
import androidx.palette.graphics.Palette
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.baseprojectandroid.model.Playlist
import com.example.baseprojectandroid.model.Position
import com.example.baseprojectandroid.model.Song
import com.example.baseprojectandroid.model.SongState
import com.example.baseprojectandroid.repository.SongRepository
import com.example.baseprojectandroid.service.MusicServiceConnector
import com.example.baseprojectandroid.ui.base.BaseViewModel
import com.example.baseprojectandroid.utils.Event
import com.example.baseprojectandroid.utils.eventOf
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
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

    val currentSongState: StateFlow<SongState>?
        get() = musicServiceConnector.currentSongState

    private val currentSongPosition: StateFlow<Position>?
        get() = musicServiceConnector.currentSongPosition
    private val currentPlaylist: StateFlow<Playlist>? get() = musicServiceConnector.currentPlaylist

    private val _uiState = MutableStateFlow(
        NowPlayingUiState(
            eventOf(Song.EMPTY.defaultState()),
            eventOf(Song.EMPTY.toSinglePlaylist())
        )
    )

    val uiState: StateFlow<NowPlayingUiState> get() = _uiState

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

    fun updateCurrentSongOfPlaylist(index: Int) = musicServiceConnector.updateCurrentSongOfPlaylist(index)

    //Collect all changes of MusicService, and update into uiState
    override fun onConnected() {

        viewModelScope.launch {
            launch {
                songRepository.getSongs().collect {
                    musicServiceConnector.play(Playlist(songs = it), 0)
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
                currentSongState!!.collect { songState ->
                    _uiState.update {
                        it.copy(
                            songState = eventOf(songState)
                        )
                    }
                    glide.asBitmap()
                        .load(songState.song.thumbnailUrl)
                        .override(100, 100)
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
        }
    }

    override fun onCleared() {
        super.onCleared()
        needUpdateCurrentSongPosition = false
    }

}

data class NowPlayingUiState(
    var songState: Event<SongState>,
    var currentPlaylist: Event<Playlist>,
    var currentPosition: Position = Position.NOTHING,
) {
    val currentSongIndexInPlayingPlaylist: Int get() =
        currentPlaylist.getValue().songs.run {
            indexOf(find { it.url == songState.getValue().song.url })
        }
}