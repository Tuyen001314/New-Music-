package com.example.baseprojectandroid.data.response

import com.example.baseprojectandroid.model.User
import com.google.gson.annotations.SerializedName

class GetUserByUserNameResponse {
    @SerializedName("message")
    var message: String? = null

    @SerializedName("status")
    var status: String? = null

    @SerializedName("data")
    var data: List<User>? = null
}