package com.example.baseprojectandroid.viewmodel

import android.os.Handler
import androidx.lifecycle.viewModelScope
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
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NowPlayingViewModel @Inject constructor(
    private val musicServiceConnector: MusicServiceConnector,
    private val songRepository: SongRepository
) : BaseViewModel(), MusicServiceConnector.OnServiceConnected {
    private var isBound = false
    private var needUpdateCurrentSongPosition = true
    private lateinit var workHandler: Handler

    val currentSongState: StateFlow<SongState>?
        get() = musicServiceConnector.currentSongState

    private val currentSongPosition: StateFlow<Position>?
        get() = musicServiceConnector.currentSongPosition

    private val _uiState = MutableStateFlow(
        NowPlayingUiState(
            eventOf(Song.EMPTY.toDefaultState())
        )
    )

    val uiState: StateFlow<NowPlayingUiState> get() = _uiState

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
        viewModelScope.launch(Dispatchers.Default) {
            while (needUpdateCurrentSongPosition) {
                if (needUpdateCurrentSongPosition) {
                    musicServiceConnector.updateCurrentPosition()
                    delay(200)
                }
            }
        }

    }

    override fun onConnected() {
        viewModelScope.launch {
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
                currentSongState!!.collect {songState ->
                    _uiState.update {
                        it.copy(
                            songState = eventOf(songState)
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
    var currentPosition: Position = Position.NOTHING
)