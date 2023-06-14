package com.example.baseprojectandroid.ui

import androidx.lifecycle.viewModelScope
import com.example.baseprojectandroid.repository.SongRepository
import com.example.baseprojectandroid.service.MusicServiceConnector
import com.example.baseprojectandroid.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlaylistSampleViewModel @Inject constructor(
    private val musicServiceConnector: MusicServiceConnector,
    private val songRepository: SongRepository
) : BaseViewModel(), MusicServiceConnector.OnServiceConnected {
//    private val _allSongs = MutableLiveData(listOf<SongState>())

    init {
        viewModelScope.launch(Dispatchers.Default) {
            songRepository.getSongs().collect {
//                _allSongs.postValue(it.map { song -> song.defaultState() })
            }
        }

        musicServiceConnector.addOnConnectedListener(this)
    }

    override fun onConnected() {
//        viewModelScope.launch {
//            musicServiceConnector.currentSongState!!.collect { currentSong ->
//                _allSongs.value!!.forEach {
//                    it.state = if (currentSong.url != it.song.url) SongState.STATE_PAUSE
//                    else currentSong.state
//                }
//                _allSongs.postValue(_allSongs.value)
//            }
//        }
    }
}