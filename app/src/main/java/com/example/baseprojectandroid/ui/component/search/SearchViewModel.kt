package com.example.baseprojectandroid.ui.component.search

import androidx.lifecycle.viewModelScope
import com.example.baseprojectandroid.model.ResponseSearch
import com.example.baseprojectandroid.model.Song
import com.example.baseprojectandroid.server.ApiClient
import com.example.baseprojectandroid.service.MusicServiceConnector
import com.example.baseprojectandroid.ui.SongUiState
import com.example.baseprojectandroid.ui.base.BaseViewModel
import com.example.baseprojectandroid.utils.DataState
import com.skydoves.sandwich.suspendMapSuccess
import com.skydoves.sandwich.suspendOnError
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val api: ApiClient,
    private val musicServiceConnector: MusicServiceConnector
) : BaseViewModel() {

    val searchSong = MutableStateFlow(listOf<SongUiState>())
    var listResultSearch: List<Song>? = null

    fun handleSearch(textSearch: String, userId: Int): Flow<DataState<List<Song>>> = flow {
        api.search(textSearch, userId).suspendMapSuccess {
            listResultSearch = this
        }.suspendOnError {
            
        }
    }
}