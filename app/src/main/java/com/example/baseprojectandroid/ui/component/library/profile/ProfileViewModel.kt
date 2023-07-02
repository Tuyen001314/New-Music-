package com.example.baseprojectandroid.ui.component.library.profile

import android.content.Context
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.baseprojectandroid.extension.getFileName
import com.example.baseprojectandroid.repository.song.SongRepository
import com.example.baseprojectandroid.server.ApiClient
import com.example.baseprojectandroid.ui.base.BaseViewModel
import com.example.baseprojectandroid.upload.UploadRequestBody
import com.example.baseprojectandroid.utils.DataState
import com.example.baseprojectandroid.utils.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel
@Inject constructor(
    private val songRepository: SongRepository, private val api: ApiClient
) : BaseViewModel() {

    var currentImageFile = MutableLiveData<File>()
    fun onImageUriSelected(context: Context, uri: Uri) {
        currentImageFile.postValue(uri.asFileFromDocumentUri(context))
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

    private val imageMultipart = currentImageFile.value.let {
        MultipartBody.Part.createFormData(
            "image", it?.name ?: "",
            it?.let {
                UploadRequestBody(it) {

                }
            } ?: "".toRequestBody(MultipartBody.FORM)
        )
    }

    fun changeAvatar(userId: Int) {
        viewModelScope.launchSafe(Dispatchers.Default) {
            songRepository.uploadAvatar(userId, imageMultipart)
                .collect {
                    it.whenFailure {

                    }.whenSuccess {

                    }
                }
        }
    }
}