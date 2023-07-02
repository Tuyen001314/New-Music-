package com.example.baseprojectandroid.ui.component.home

import androidx.lifecycle.viewModelScope
import com.example.baseprojectandroid.model.Playlist
import com.example.baseprojectandroid.model.Song
import com.example.baseprojectandroid.repository.song.SongRepository
import com.example.baseprojectandroid.service.MusicServiceConnector
import com.example.baseprojectandroid.ui.SongUiState
import com.example.baseprojectandroid.ui.base.BaseViewModel
import com.example.baseprojectandroid.utils.DataState
import com.example.baseprojectandroid.utils.Event
import com.example.baseprojectandroid.utils.eventOf
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val songRepository: SongRepository,
    private val musicServiceConnector: MusicServiceConnector
) : BaseViewModel() {
    var recentSong = MutableStateFlow(listOf<Song>())
    val downloadSong = MutableStateFlow(listOf<SongUiState>())


    private val _uiEffect: MutableStateFlow<Event<HomeUiEffect?>> = MutableStateFlow(eventOf(null))
    val uiEffect: StateFlow<Event<HomeUiEffect?>> get() = _uiEffect

    private val playerState get() = musicServiceConnector.currentPlayerState!!
    private val currentSong get() = musicServiceConnector.currentSongState!!

    init {
        viewModelScope.launch(Dispatchers.IO) {
            songRepository.getAllSong().collect {
                when (it) {
                    is DataState.Failure -> _uiEffect.emit(
                        eventOf(HomeUiEffect.Toast(it.message ?: "Unknown error"))
                    )

                    is DataState.Success -> {
                        recentSong.emit(it.data ?: listOf())
                    }

                    else -> {}
                }
            }

            musicServiceConnector.addOnConnectedListener(object :
                MusicServiceConnector.OnServiceConnected {
                override fun onConnected() {
                    viewModelScope.launch {
                        currentSong.collect { playingSong ->
                            downloadSong.emit(
                                recentSong.value.toMutableList().map {
                                    SongUiState(
                                        song = it,
                                        isPlaying = playingSong.id == it.id
                                    )
                                }

                            )
                        }
                    }
                }
            })
        }
    }

    private fun buildRelatedPlaylist(song: Song): Playlist {
        val relatedSong = recentSong.value
        val first = relatedSong.subList(0, relatedSong.indexOf(song))
        val second = relatedSong.subList(relatedSong.indexOf(song), relatedSong.size)
        return Playlist(songs = second + first)
    }

    fun playSong(song: Song) {
        val relatedPlaylist = buildRelatedPlaylist(song)
        val index = relatedPlaylist.songs.indexOf(song)
        musicServiceConnector.play(relatedPlaylist, index)
    }

    sealed class HomeUiEffect {
        class Toast(val message: String) : HomeUiEffect()
    }

    data class HomeUiState(
        val isLoading: Boolean = false
    )
}