package com.example.baseprojectandroid.ui.component.library

import androidx.lifecycle.viewModelScope
import com.example.baseprojectandroid.model.AccountState
import com.example.baseprojectandroid.repository.song.SongRepository
import com.example.baseprojectandroid.server.ApiClient
import com.example.baseprojectandroid.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class UploadTrackViewModel
@Inject constructor(private val songRepository: SongRepository, private val apiClient: ApiClient) : BaseViewModel() {

    fun uploadSong(
        name: String,
        image: MultipartBody.Part,
        song: MultipartBody.Part,
        category: Int,
        creator: Int
    ) {
        val handle = CoroutineExceptionHandler { _, e ->
            e as HttpException
            e.printStackTrace()
        }
        val exceptionHandler = SupervisorJob() + handle
        viewModelScope.launch(exceptionHandler) {
            songRepository.uploadSong(name, image, song, category, creator)
        }
    }


    fun uploadImage(image: MultipartBody.Part): Flow<AccountState> = flow {
        emit(AccountState.Loading)

        apiClient.uploadImage(image)
        emit(AccountState.Finished)
    }
}