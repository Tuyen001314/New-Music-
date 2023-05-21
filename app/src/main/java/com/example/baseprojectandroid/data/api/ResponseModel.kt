package com.example.baseprojectandroid.data.api

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

class ResponseModel {

    @SerializedName("message")
    var message: String? = null

    @SerializedName("status")
    var status: String? = null

    @SerializedName("data")
    var data: String? = null
}