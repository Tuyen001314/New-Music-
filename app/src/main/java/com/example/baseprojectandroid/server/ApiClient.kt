package com.example.baseprojectandroid.server

import com.example.baseprojectandroid.data.api.ResponseModel
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiClient {

    @POST("")
    suspend fun login()

    @GET("/api/Users/checkUserName")
    suspend fun getUser(@Query("username") userName: String): Call<ResponseModel>

}