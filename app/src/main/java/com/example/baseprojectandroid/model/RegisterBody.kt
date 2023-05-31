package com.example.baseprojectandroid.model

import com.google.gson.annotations.SerializedName
import java.io.File

data class RegisterBody(
    @SerializedName("name")
    var name: String = "",
    @SerializedName("password")
    var password: String = "",
    @SerializedName("username")
    var username: String ="",
    @SerializedName("image")
    var image: String? = null
)
