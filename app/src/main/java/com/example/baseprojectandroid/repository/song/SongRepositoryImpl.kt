package com.example.baseprojectandroid.repository.song

import android.os.Environment
import android.provider.ContactsContract.Data
import android.util.Log
import com.example.baseprojectandroid.data.response.InsertSongResponse
import com.example.baseprojectandroid.local.LocalStorage
import com.example.baseprojectandroid.model.Song
import com.example.baseprojectandroid.repository.user.UserRepository
import com.example.baseprojectandroid.server.ApiClient
import com.example.baseprojectandroid.utils.DataState
import com.example.baseprojectandroid.utils.Logger
import com.skydoves.sandwich.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class SongRepositoryImpl @Inject constructor(
    private val userRepository: UserRepository,
    private val api: ApiClient,
    private val localData: LocalStorage,
    private val io: CoroutineContext
) : SongRepository {
    private var currentUploadCall: Call<InsertSongResponse>? = null
    override fun getSongById(id: String) {
        TODO("Not yet implemented")
    }

    override fun getAllSong(): Flow<DataState<List<Song>>> = flow {
        api.getAllSong().suspendOnError {
            Logger.d("failure. message = ${message()}")
            emit(DataState.Failure(message()))
        }.suspendOnException {
            Logger.d("failure. message = ${message()}")
            emit(DataState.Failure(message()))
        }.suspendMapSuccess {
            map {
                Song(
//                    it.category.toString(),
                    it.createdAt,
                    it.creatorId,
                    it.downloadCount,
                    it.id,
                    it.listenedCount,
                    it.name,
                    it.thumbnailUrl,
                    it.url
                ).apply {
                    userRepository.getUser(it.creatorId)?.let { it1 -> setCreator(it1) }
                }
            }
        }.suspendOnSuccess {
            Logger.d("success data = $data")
            emit(DataState.Success(data = data))
        }

    }
//        emit(
//            DataState.Success(
//                data = listOf(
//                    Song(
//                        id = 1,
//                        name = "Ung qua chung",
//                        url = "https://firebasestorage.googleapis.com/v0/b/music-8fef0.appspot.com/o/music%2F%C6%AFng%20Qu%C3%A1%20Ch%E1%BB%ABng%20-%20AMEE.mp3?alt=media&token=3201a2c5-fbd4-4924-940e-dbadb6c91bb0",
//                        thumbnailUrl = "https://firebasestorage.googleapis.com/v0/b/music-8fef0.appspot.com/o/image%2Fartworks-c50r8eqmm2lxm78x-qpupzg-t500x500_20230328111338.jpg?alt=media&token=c0eff59c-50c1-4ba7-91dd-748a065904ec",
//                    ).setCreator(User(name = "AMEE")),
//                    Song(
//                        id = 2,
//                        name = "Ex's hate me",
//                        url = "https://firebasestorage.googleapis.com/v0/b/music-8fef0.appspot.com/o/music%2FExsHateMe-BRayMasewAMee-5878683.mp3?alt=media&token=03820468-365f-4b5e-9ce4-1b214f766ec9",
//                        thumbnailUrl = "https://firebasestorage.googleapis.com/v0/b/music-8fef0.appspot.com/o/image%2F1550063180850.jpg?alt=media&token=6459e61d-ba60-4643-8c25-31d6114316b9",
//                    ).setCreator(User(name = "AMEE")),
//                    Song(
//                        id = 3,
//                        name = "Đen đá không đường",
//                        url = "https://firebasestorage.googleapis.com/v0/b/music-8fef0.appspot.com/o/music%2FDenDaKhongDuongRapVersion-AMeeDMex-6009199.mp3?alt=media&token=7d64740f-507f-4a8c-8fa5-40d6e08e604d",
//                        thumbnailUrl = "https://firebasestorage.googleapis.com/v0/b/music-8fef0.appspot.com/o/image%2F8c9f583a79f97a92cd585c8c2d526cfc.jpg?alt=media&token=1f96b909-8e6d-42a8-86b4-53c13cc077b4",
//                    ).setCreator(User(name = "AMEE")),
//                    Song(
//                        id = 4,
//                        name = "hai mươi hai",
//                        url = "https://firebasestorage.googleapis.com/v0/b/music-8fef0.appspot.com/o/music%2FHaiMuoiHai22-HuaKimTuyenAMEE-7231237.mp3?alt=media&token=c8f90f24-3d21-442a-acaa-c0565c664dd0",
//                        thumbnailUrl = "https://firebasestorage.googleapis.com/v0/b/music-8fef0.appspot.com/o/image%2F1653363505428_300.jpg?alt=media&token=2c0daa37-e6ee-4c02-9d0e-e0f1c13ce796",
//                    ).setCreator(User(name = "AMEE"))
//                )
//            )
//        )


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
        currentUploadCall = api.uploadSong(name, image, song, category, creator)
        currentUploadCall!!.enqueue(object : Callback<InsertSongResponse> {
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

    override fun uploadAvatar(userId: Int, image: MultipartBody.Part): Flow<DataState<InsertSongResponse>> = callbackFlow {
        currentUploadCall = api.changeAvatar(userId, image)
        currentUploadCall!!.enqueue(object : Callback<InsertSongResponse> {
            override fun onResponse(
                call: Call<InsertSongResponse>, response: Response<InsertSongResponse>
            ) {
                if (response.isSuccessful) {
                    CoroutineScope(io).launch {
                        trySend(DataState.Success("ok", response.body()))
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
    }

    override fun cancelCurrentUploading() {
        currentUploadCall?.cancel()
    }

    override fun likeSong() {

    }


}