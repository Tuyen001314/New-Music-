package com.example.baseprojectandroid.ui.component.home

import com.example.baseprojectandroid.model.UploadResponse
import com.example.baseprojectandroid.server.ApiClient
import com.example.baseprojectandroid.ui.base.BaseViewModel
import com.example.baseprojectandroid.upload.UploadRequestBody
import dagger.hilt.android.lifecycle.HiltViewModel
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val api: ApiClient) : BaseViewModel() {
    suspend fun upload(file: File, body: UploadRequestBody) {
        api.uploadImage(
            MultipartBody.Part.createFormData(
                "image",
                file.name,
                body
            ),
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), "json")
        ).enqueue(object : Callback<UploadResponse> {
            override fun onResponse(
                call: Call<UploadResponse>,
                response: Response<UploadResponse>
            ) {
                response.body()?.let {
                    /*layout_root.snackbar(it.message)
                    progress_bar.progress = 100*/
                }
            }

            override fun onFailure(call: Call<UploadResponse>, t: Throwable) {
                /*layout_root.snackbar(t.message!!)
                progress_bar.progress = 0*/
            }

        })
    }
}