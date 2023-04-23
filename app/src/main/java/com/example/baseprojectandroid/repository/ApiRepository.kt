package com.example.baseprojectandroid.repository

import com.example.baseprojectandroid.server.ApiClient
import javax.inject.Inject

class ApiRepository : ApiClient {
    @Inject
    lateinit var api: ApiClient

    override suspend fun login() {
        api.login()
    }
}