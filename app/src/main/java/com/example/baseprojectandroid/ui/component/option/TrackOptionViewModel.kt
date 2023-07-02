package com.example.baseprojectandroid.ui.component.option

import androidx.lifecycle.viewModelScope
import com.example.baseprojectandroid.data.AppDatabase
import com.example.baseprojectandroid.model.FavoriteEntity
import com.example.baseprojectandroid.model.Song
import com.example.baseprojectandroid.ui.base.BaseViewModel
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.ArrayList
import javax.inject.Inject

@HiltViewModel
class TrackOptionViewModel @Inject constructor(private val appDatabase: AppDatabase) :
    BaseViewModel() {
    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> get() = _uiState

    private var favoriteDocument = ArrayList<FavoriteEntity>()

    fun setCurrentSong(song: Song) {

    }

    fun handleFavorite(song: Song) {
        viewModelScope.launch(Dispatchers.IO) {
            appDatabase.favoriteDao().insert(song.toFavorite())
        }
    }

    data class UiState(
        val isLike: Boolean = false, val isDownload: Boolean = false
    )
}