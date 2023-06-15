package com.example.baseprojectandroid.repository.song

import com.example.baseprojectandroid.model.Song
import kotlinx.coroutines.flow.Flow

class SongRepositoryImpl: SongRepository {
    override fun getSongById(id: String) {
        TODO("Not yet implemented")
    }

    override fun getAllSong(): Flow<List<Song>> {
        TODO("Not yet implemented")
    }
}