package com.example.baseprojectandroid.repository.song

import android.os.Environment
import android.util.Log
import com.example.baseprojectandroid.data.response.InsertSongResponse
import com.example.baseprojectandroid.model.Song
import com.example.baseprojectandroid.server.ApiClient
import com.example.baseprojectandroid.utils.DataState
import com.skydoves.sandwich.message
import com.skydoves.sandwich.suspendMapSuccess
import com.skydoves.sandwich.suspendOnError
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class SongRepositoryImpl @Inject constructor(
    private val api: ApiClient,
    private val io: CoroutineContext
) : SongRepository {
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

    override fun getAllSongLocal(): Flow<DataState<List<Song>>> = flow {
        emit(DataState.Loading)
        val folder = File(Environment.getExternalStorageDirectory(), "MusicFile")
        val list: MutableList<Song>? = null
        if (folder.listFiles()?.isNotEmpty() == true) {
            for (file in folder.listFiles()!!) {
                if (file.name.endsWith(".mp3")) {
                    list?.add(Song(url = file.absolutePath))
                }
            }
            emit(DataState.Success(data = list))
        } else {
            emit(DataState.Failure("list empty"))
        }
    }

    override fun uploadSong(
        name: String,
        image: MultipartBody.Part,
        song: MultipartBody.Part,
        category: Int,
        creator: Int
    ): Flow<DataState<InsertSongResponse>> = callbackFlow {
        api.uploadSong(name, image, song, category, creator)
            .enqueue(object : Callback<InsertSongResponse> {
                override fun onResponse(
                    call: Call<InsertSongResponse>, response: Response<InsertSongResponse>
                ) {
                    if (response.isSuccessful) {
                        CoroutineScope(io).launch {
                            trySend(DataState.Success("ok", response.body()))
                            Log.d("buituyen", "acas")
                        }
                    } else {
                        CoroutineScope(io).launch {
                            trySend(DataState.Failure(message = response.message()))
                        }
                    }
                }

                override fun onFailure(call: Call<InsertSongResponse>, t: Throwable) {
                    CoroutineScope(io).launch {
                        trySend(DataState.Failure(message = t.message, error = t))
                    }
                    Log.d("buituyen", "aca34231s")
                }
            })

        awaitClose { }
    }


}