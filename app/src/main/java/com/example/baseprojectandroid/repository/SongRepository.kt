package com.example.baseprojectandroid.repository

import com.example.baseprojectandroid.model.Song
import com.example.baseprojectandroid.server.ApiClient
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SongRepository @Inject constructor(
    private val api: ApiClient
){
    suspend fun getSongs() = flow {
        emit(listOf(
            Song(
                name = "Ung qua chung",
                url =  "https://firebasestorage.googleapis.com/v0/b/music-8fef0.appspot.com/o/music%2F%C6%AFng%20Qu%C3%A1%20Ch%E1%BB%ABng%20-%20AMEE.mp3?alt=media&token=3201a2c5-fbd4-4924-940e-dbadb6c91bb0",
                thumbnailUrl = "https://firebasestorage.googleapis.com/v0/b/music-8fef0.appspot.com/o/image%2Fartworks-c50r8eqmm2lxm78x-qpupzg-t500x500_20230328111338.jpg?alt=media&token=c0eff59c-50c1-4ba7-91dd-748a065904ec"
            ),
            Song(
                name = "Ex's hate me",
                url = "https://firebasestorage.googleapis.com/v0/b/music-8fef0.appspot.com/o/music%2FExsHateMe-BRayMasewAMee-5878683.mp3?alt=media&token=03820468-365f-4b5e-9ce4-1b214f766ec9",
                thumbnailUrl = "https://firebasestorage.googleapis.com/v0/b/music-8fef0.appspot.com/o/image%2F1550063180850.jpg?alt=media&token=6459e61d-ba60-4643-8c25-31d6114316b9"
            )
        ))
    }
}