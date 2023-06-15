package com.example.baseprojectandroid.repository.song

import com.example.baseprojectandroid.model.Song
import kotlinx.coroutines.flow.Flow

interface SongRepository {
    fun getSongById(id: String)

    fun getAllSong(): Flow<List<Song>>


}