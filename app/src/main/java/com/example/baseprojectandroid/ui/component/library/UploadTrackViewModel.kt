package com.example.baseprojectandroid.ui.component.library

import android.content.Context
import android.net.Uri
import android.util.AtomicFile
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.baseprojectandroid.data.response.InsertSongResponse
import com.example.baseprojectandroid.extension.getFileName
import com.example.baseprojectandroid.model.AccountState
import com.example.baseprojectandroid.repository.song.SongRepository
import com.example.baseprojectandroid.server.ApiClient
import com.example.baseprojectandroid.ui.base.BaseViewModel
import com.example.baseprojectandroid.upload.UploadRequestBody
import com.example.baseprojectandroid.utils.DataState
import com.example.baseprojectandroid.utils.Logger
import com.google.common.util.concurrent.AtomicDouble
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class UploadTrackViewModel @Inject constructor(
    private val songRepository: SongRepository,
    private val apiClient: ApiClient
) : BaseViewModel() {
    var currentImageFile = MutableLiveData<File>()
    var currentSongFile = MutableLiveData<File>()
    var uploadSongResponse = MutableLiveData<DataState<InsertSongResponse>>()

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
        var imageUploadProgress = AtomicDouble(0.0)
        var songUploadProcess = AtomicDouble(0.0)
        var trackProcess: Boolean

        fun startTrackProcess() {
            trackProcess = true
            val executor = Executors.newSingleThreadScheduledExecutor()
            var trackTask: Runnable? = null
            trackTask = Runnable {
                if (trackProcess) {
                    synchronized(this) {
                        val uploadWithImage = currentImageFile.value != null
                        val realImageUploadProgress = if (uploadWithImage) imageUploadProgress.get() else 1.0
                        uploadSongResponse.postValue(
                            DataState.Processing((realImageUploadProgress * songUploadProcess.get()).toFloat() * 100)
                        )
                    }
                    executor.schedule(trackTask, 200, TimeUnit.MILLISECONDS)
                }
            }
            executor.execute(trackTask)
        }

        fun stopTrackProcess() {
            trackProcess = false
        }

        val imageMultipart = currentImageFile.value.let {
            MultipartBody.Part.createFormData(
                "image", it?.name ?: "",
                it?.let {
                    UploadRequestBody(it) {
                        imageUploadProgress.set(it.toDouble())
                    }
                } ?: "".toRequestBody(MultipartBody.FORM)
            )
        }

        val audioMultipart = currentSongFile.value.let {
            MultipartBody.Part.createFormData(
                "song", it?.name ?: "",
                it?.let {
                    UploadRequestBody(it) {
                        songUploadProcess.set(it.toDouble())
                    }
                } ?: "".toRequestBody(MultipartBody.FORM))
        }

        //call api
        startTrackProcess()
        viewModelScope.launchSafe(Dispatchers.Default) {
            songRepository.uploadSong(name, imageMultipart, audioMultipart, category, creator)
                .collect {
                    it.whenFailure {
                        stopTrackProcess()

                        //post value with synchronized block to prevent conflict when postValue
                        //many time when we track processing of uploading
                        synchronized(this@UploadTrackViewModel) {
                            uploadSongResponse.postValue(DataState.Failure(it.message))
                        }
                    }.whenSuccess {
                        stopTrackProcess()

                        //post value with synchronized block to prevent conflict when postValue
                        //many time when we track processing of uploading
                        synchronized(this@UploadTrackViewModel) {
                            uploadSongResponse.postValue(DataState.Success(data = it.data))
                        }
                    }
                }
        }
    }

    fun cancelCurrentUploading() {
        songRepository.cancelCurrentUploading()
    }

    fun retryUpload() {

    }


    fun uploadImage(image: MultipartBody.Part): Flow<AccountState> = flow {
        emit(AccountState.Loading)

        apiClient.uploadImage(image)
        emit(AccountState.Finished)
    }
}