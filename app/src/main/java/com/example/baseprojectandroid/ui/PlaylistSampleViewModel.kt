package com.example.baseprojectandroid.ui

import androidx.lifecycle.viewModelScope
import com.example.baseprojectandroid.model.SongState
import com.example.baseprojectandroid.repository.SongRepository
import com.example.baseprojectandroid.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlaylistSampleViewModel @Inject constructor(
    private val songRepository: SongRepository
): BaseViewModel() {
    private val _allSongs = MutableStateFlow(listOf<SongState>())
    val allSongs: StateFlow<List<SongState>> get() = _allSongs
    init {
        viewModelScope.launch(Dispatchers.Default) {
            songRepository.getSongs().collect {
                _allSongs.emit(it.map { song -> song.toDefaultState() })
            }
        }
    }
}