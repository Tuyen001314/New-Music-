package com.example.baseprojectandroid.data.response

import com.example.baseprojectandroid.model.User

data class InsertSongResponse(
    val status: String,
    val message: String,
    val data: User
)