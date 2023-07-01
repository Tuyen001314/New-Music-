package com.example.baseprojectandroid.repository.song

import com.example.baseprojectandroid.data.response.InsertSongResponse
import com.example.baseprojectandroid.model.Song
import com.example.baseprojectandroid.model.User
import com.example.baseprojectandroid.utils.DataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody
import javax.inject.Inject

class FakeSongRepositoryImpl @Inject constructor() : SongRepository {
    override fun getSongById(id: String) {
    }

    override fun getAllSong(): Flow<DataState<List<Song>>> {
        return flow {
            emit(
                DataState.Success(
                    data = listOf(
                        Song(
                            name = "Ung qua chung",
                            url = "https://firebasestorage.googleapis.com/v0/b/music-8fef0.appspot.com/o/music%2F%C6%AFng%20Qu%C3%A1%20Ch%E1%BB%ABng%20-%20AMEE.mp3?alt=media&token=3201a2c5-fbd4-4924-940e-dbadb6c91bb0",
                            thumbnailUrl = "https://firebasestorage.googleapis.com/v0/b/music-8fef0.appspot.com/o/image%2Fartworks-c50r8eqmm2lxm78x-qpupzg-t500x500_20230328111338.jpg?alt=media&token=c0eff59c-50c1-4ba7-91dd-748a065904ec",
                        ).setCreator(User(name = "AMEE")),
                        Song(
                            name = "Ex's hate me",
                            url = "https://firebasestorage.googleapis.com/v0/b/music-8fef0.appspot.com/o/music%2FExsHateMe-BRayMasewAMee-5878683.mp3?alt=media&token=03820468-365f-4b5e-9ce4-1b214f766ec9",
                            thumbnailUrl = "https://firebasestorage.googleapis.com/v0/b/music-8fef0.appspot.com/o/image%2F1550063180850.jpg?alt=media&token=6459e61d-ba60-4643-8c25-31d6114316b9",
                        ).setCreator(User(name = "AMEE")),
                        Song(
                            name = "Đen đá không đường",
                            url = "https://firebasestorage.googleapis.com/v0/b/music-8fef0.appspot.com/o/music%2FDenDaKhongDuongRapVersion-AMeeDMex-6009199.mp3?alt=media&token=7d64740f-507f-4a8c-8fa5-40d6e08e604d",
                            thumbnailUrl = "https://firebasestorage.googleapis.com/v0/b/music-8fef0.appspot.com/o/image%2F8c9f583a79f97a92cd585c8c2d526cfc.jpg?alt=media&token=1f96b909-8e6d-42a8-86b4-53c13cc077b4",
                        ).setCreator(User(name = "AMEE")),
                        Song(
                            name = "hai mươi hai",
                            url = "https://firebasestorage.googleapis.com/v0/b/music-8fef0.appspot.com/o/music%2FHaiMuoiHai22-HuaKimTuyenAMEE-7231237.mp3?alt=media&token=c8f90f24-3d21-442a-acaa-c0565c664dd0",
                            thumbnailUrl = "https://firebasestorage.googleapis.com/v0/b/music-8fef0.appspot.com/o/image%2F1653363505428_300.jpg?alt=media&token=2c0daa37-e6ee-4c02-9d0e-e0f1c13ce796",
                        ).setCreator(User(name = "AMEE"))
                    )
                )
            )
        }
    }

    override fun getAllSongLocal(): Flow<DataState<List<Song>>> {
        TODO("Not yet implemented")
    }

    override fun uploadSong(
        name: String,
        image: MultipartBody.Part,
        song: MultipartBody.Part,
        category: Int,
        creator: Int
    ): Flow<DataState<InsertSongResponse>> {
        TODO("Not yet implemented")

    }

    override fun cancelCurrentUploading() {

    }

    override fun likeSong() {
        TODO("Not yet implemented")
    }

}