package com.example.baseprojectandroid.repository.song

import com.example.baseprojectandroid.model.Song
import com.example.baseprojectandroid.server.ApiClient
import com.example.baseprojectandroid.utils.DataState
import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.message
import com.skydoves.sandwich.suspendMapSuccess
import com.skydoves.sandwich.suspendOnError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class SongRepositoryImpl @Inject constructor(
    private val api: ApiClient,
    private val io: CoroutineContext
): SongRepository {
    override fun getSongById(id: String) {
        TODO("Not yet implemented")
    }

    override fun getAllSong(): Flow<DataState<List<Song>>> = flow {
        api.getAllSong()
            .suspendMapSuccess {
                emit(DataState.Success(data = this))
            }
            .suspendOnError {
                emit(DataState.Failure(message = message()))
            }
    }.flowOn(io)
}