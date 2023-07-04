package com.example.baseprojectandroid.ui.component.option

import androidx.lifecycle.viewModelScope
import com.example.baseprojectandroid.data.AppDatabase
import com.example.baseprojectandroid.model.FavoriteEntity
import com.example.baseprojectandroid.model.Song
import com.example.baseprojectandroid.model.SongEntity
import com.example.baseprojectandroid.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TrackOptionViewModel @Inject constructor(private val database: AppDatabase) :
    BaseViewModel() {
    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> get() = _uiState
    private lateinit var currentSong: Song

    private var favoriteDocument = ArrayList<FavoriteEntity>()

    fun setCurrentSong(song: Song) {
        currentSong = song
        viewModelScope.launch(Dispatchers.Default) {
            _uiState.emit(
                UiState(
                    isLike = getFavorite(song.id.toString())?.isFavorite ?: false,
                    isDownload = isDownloadSong(song)
                )
            )
        }
    }

    fun handleFavorite(song: Song) {
        viewModelScope.launch(Dispatchers.IO) {
            database.favoriteDao().insert(song.toFavorite())
            _uiState.emit(
                _uiState.value.copy(
                    isLike = true
                )
            )
        }
    }

    fun deleteFavorite(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            database.favoriteDao().delete(id)
            _uiState.emit(
                _uiState.value.copy(
                    isLike = false
                )
            )
        }
    }

    fun getFavorite(id: String) = database.favoriteDao().getFavoriteById(id)

    fun get(path: String) = database.musicDao().get(path)

    fun insert(musicEntity: SongEntity) = viewModelScope.launch(Dispatchers.IO) {
        database.musicDao().insert(musicEntity)
    }

    suspend fun isDownloadSong(song: Song): Boolean {
        return database.musicDao().get(song.id.toString()) != null
    }

    data class UiState(
        val isLike: Boolean = false, val isDownload: Boolean = false
    )
}