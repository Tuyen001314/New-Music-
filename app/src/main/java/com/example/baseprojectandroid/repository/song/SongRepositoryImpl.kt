package com.example.baseprojectandroid.repository.song

import android.os.Environment
import android.util.Log
import com.airbnb.lottie.utils.Logger
import com.example.baseprojectandroid.model.Song
import com.example.baseprojectandroid.model.SongUpload
import com.example.baseprojectandroid.model.UploadResponse
import com.example.baseprojectandroid.server.ApiClient
import com.example.baseprojectandroid.utils.DataState
import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.message
import com.skydoves.sandwich.suspendMapSuccess
import com.skydoves.sandwich.suspendOnError
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Part
import java.io.File
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
    ): Flow<DataState<Boolean>> =
        flow<DataState<Boolean>> {
            api.uploadSong(name, image, song, category, creator).enqueue(object : Callback<UploadResponse> {
                override fun onResponse(
                    call: Call<UploadResponse>, response: Response<UploadResponse>
                ) {
                    if (response.isSuccessful) {
                        CoroutineScope(io).launch {
                            emit(DataState.Success("ok", true))
                            Log.d("buituyen", "acas")
                        }
                    }
                    Log.d("buituyen", "aca213123s")
                }

                override fun onFailure(call: Call<UploadResponse>, t: Throwable) {
                    CoroutineScope(io).launch {
                        emit(DataState.Success("fail", false))
                    }
                    Log.d("buituyen", "aca34231s")
                }
            })

    }.flowOn(io)


}