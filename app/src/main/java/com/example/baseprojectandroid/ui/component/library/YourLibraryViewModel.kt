package com.example.baseprojectandroid.ui.component.library

import com.example.baseprojectandroid.data.AppDatabase
import com.example.baseprojectandroid.model.SongEntity
import com.example.baseprojectandroid.server.ApiClient
import com.example.baseprojectandroid.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class YourLibraryViewModel @Inject constructor(
    private val api: ApiClient,
    private val database: AppDatabase
) : BaseViewModel() {
    fun getAllSongDownload() = database.musicDao().getAllSongDownload()

}