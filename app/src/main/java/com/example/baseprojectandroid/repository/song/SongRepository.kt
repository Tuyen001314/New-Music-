package com.example.baseprojectandroid.repository.song

import com.example.baseprojectandroid.data.response.InsertSongResponse
import com.example.baseprojectandroid.model.Song
import com.example.baseprojectandroid.model.SongUpload
import com.example.baseprojectandroid.utils.DataState
import com.skydoves.sandwich.ApiResponse
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import retrofit2.http.Part

interface SongRepository {
    fun getSongById(id: String)

    fun getAllSong(): Flow<DataState<List<Song>>>

    fun getAllSongLocal(): Flow<DataState<List<Song>>>

    fun uploadSong(
        name: String,
        image: MultipartBody.Part,
        song: MultipartBody.Part,
        category: Int,
        creator: Int
    ): Flow<DataState<InsertSongResponse>>

    fun cancelCurrentUploading()

    fun likeSong()
}