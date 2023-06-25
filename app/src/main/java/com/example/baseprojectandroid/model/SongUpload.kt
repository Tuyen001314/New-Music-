package com.example.baseprojectandroid.model

import okhttp3.MultipartBody
import java.io.File

data class SongUpload (
    var name: String = "",
    var image: MultipartBody.Part? = null,
    var song: MultipartBody.Part? = null,
    var category: String = "",
    var creator: String = ""
)