package com.example.baseprojectandroid.server

import retrofit2.http.POST

interface ApiClient {

    @POST("")
    suspend fun login()

}