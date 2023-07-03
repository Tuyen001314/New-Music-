package com.example.baseprojectandroid.ui.component.option

import androidx.lifecycle.viewModelScope
import com.example.baseprojectandroid.data.AppDatabase
import com.example.baseprojectandroid.model.FavoriteEntity
import com.example.baseprojectandroid.model.Song
import com.example.baseprojectandroid.model.SongEntity
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
class TrackOptionViewModel @Inject constructor(private val database: AppDatabase) :
    BaseViewModel() {
    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> get() = _uiState

    private var favoriteDocument = ArrayList<FavoriteEntity>()

    fun setCurrentSong(song: Song) {

    }

    fun handleFavorite(song: Song) {
        viewModelScope.launch(Dispatchers.IO) {
            database.favoriteDao().insert(song.toFavorite())
        }
    }

    fun deleteFavorite(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            database.favoriteDao().delete(id)
        }
    }

    fun getFavorite(id: String) =  database.favoriteDao().getFavoriteById(id)

    fun get(path: String) =  database.musicDao().get(path)

    fun insert(musicEntity: SongEntity) = viewModelScope.launch(Dispatchers.IO) {
        database.musicDao().insert(musicEntity)
    }

    data class UiState(
        val isLike: Boolean = false, val isDownload: Boolean = false
    )
}