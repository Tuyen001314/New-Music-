package com.example.baseprojectandroid.ui.component.library

import android.content.Context
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.baseprojectandroid.data.response.InsertSongResponse
import com.example.baseprojectandroid.extension.getFileName
import com.example.baseprojectandroid.model.AccountState
import com.example.baseprojectandroid.repository.song.SongRepository
import com.example.baseprojectandroid.server.ApiClient
import com.example.baseprojectandroid.ui.base.BaseViewModel
import com.example.baseprojectandroid.utils.DataState
import com.example.baseprojectandroid.utils.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import javax.inject.Inject

@HiltViewModel
class UploadTrackViewModel @Inject constructor(
    private val songRepository: SongRepository,
    private val apiClient: ApiClient
) : BaseViewModel() {
    var currentImageFile = MutableLiveData<File>()
    var currentSongFile = MutableLiveData<File>()
    var uploadSongResponse = MutableLiveData<DataState<InsertSongResponse>>()
    fun uploadSong(trackName: String) {

    }

    fun onImageUriSelected(context: Context, uri: Uri) {
        currentImageFile.postValue(uri.asFileFromDocumentUri(context))
    }

    fun onSongUriSelected(context: Context, uri: Uri) {
        currentSongFile.postValue(uri.asFileFromDocumentUri(context))
    }

    private fun Uri.asFileFromDocumentUri(context: Context): File? {
        val parcelFileDescriptor =
            context.contentResolver.openFileDescriptor(this, "r", null)
                ?: return null
        val inputStream = FileInputStream(parcelFileDescriptor.fileDescriptor)
        val file = File(context.cacheDir, context.contentResolver.getFileName(this))
        val outputStream = FileOutputStream(file)
        inputStream.copyTo(outputStream)
        parcelFileDescriptor.close()
        return file
    }

    fun uploadSong(
        name: String,
        category: Int,
        creator: Int
    ) {
        //Invalid input
        if (name.isEmpty()) {
            uploadSongResponse.postValue(
                DataState.Failure(message = "Track name is empty")
            )
            return
        }

        if (currentSongFile.value == null) {
            uploadSongResponse.postValue(
                DataState.Failure(message = "Track audio is empty")
            )
            return
        }


        //create multipartBody
        val imageMultipart = currentImageFile.value.let {
            MultipartBody.Part.createFormData(
                "image", it?.name ?: "",
                it?.asRequestBody(MultipartBody.FORM) ?: "".toRequestBody(MultipartBody.FORM)
            )
        }

        val audioMultipart = currentSongFile.value.let {
            MultipartBody.Part.createFormData(
                "song", it?.name ?: "",
                it?.asRequestBody(MultipartBody.FORM) ?: "".toRequestBody(MultipartBody.FORM)
            )
        }

        //call api
        uploadSongResponse.postValue(DataState.Loading)
        viewModelScope.launchSafe(Dispatchers.Default) {
            songRepository.uploadSong(name, imageMultipart, audioMultipart, category, creator)
                .collect {
                    it.whenFailure {
                        uploadSongResponse.postValue(DataState.Failure(it.message))
                    }.whenSuccess {
                        uploadSongResponse.postValue(DataState.Success(data = it.data))
                    }
                }
        }
    }


    fun uploadImage(image: MultipartBody.Part): Flow<AccountState> = flow {
        emit(AccountState.Loading)

        apiClient.uploadImage(image)
        emit(AccountState.Finished)
    }
}