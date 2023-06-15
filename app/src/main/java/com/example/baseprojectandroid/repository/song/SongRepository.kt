package com.example.baseprojectandroid.repository.song

import com.example.baseprojectandroid.model.Song
import com.example.baseprojectandroid.utils.DataState
import com.skydoves.sandwich.ApiResponse
import kotlinx.coroutines.flow.Flow

interface SongRepository {
    fun getSongById(id: String)

    fun getAllSong(): Flow<DataState<List<Song>>>


}