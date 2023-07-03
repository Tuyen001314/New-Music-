package com.example.baseprojectandroid.ui.component.download

import com.example.baseprojectandroid.extension.getDirFile
import com.example.baseprojectandroid.model.Song
import com.example.baseprojectandroid.model.SongEntity
import com.example.baseprojectandroid.server.ApiClient
import com.example.baseprojectandroid.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import java.io.File
import javax.inject.Inject

@HiltViewModel
class DownloadViewModel @Inject constructor(private val api: ApiClient) : BaseViewModel() {

}
