package com.example.baseprojectandroid.ui.component.option

import com.example.baseprojectandroid.model.Song
import com.example.baseprojectandroid.ui.base.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class TrackOptionViewModel: BaseViewModel() {
    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> get() = _uiState

    fun setCurrentSong(song: Song) {

    }

    data class UiState(
        val isLike: Boolean = false,
        val isDownload: Boolean = false
    )
}