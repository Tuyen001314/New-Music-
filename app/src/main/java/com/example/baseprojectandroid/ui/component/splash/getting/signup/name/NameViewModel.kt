package com.example.baseprojectandroid.ui.component.splash.getting.signup.name

import android.os.Bundle
import com.example.baseprojectandroid.data.api.ResponseModel
import com.example.baseprojectandroid.model.AccountState
import com.example.baseprojectandroid.model.RegisterBody
import com.example.baseprojectandroid.server.ApiClient
import com.example.baseprojectandroid.ui.base.BaseViewModel
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.File
import javax.inject.Inject

@HiltViewModel
class NameViewModel @Inject constructor(
    private val apiClient: ApiClient,
) : BaseViewModel() {

    var username: String = ""
    var password: String = ""
    var responseModel = ResponseModel()

    fun initData(bundle: Bundle) {
        username = bundle.getString("username", "")
        password = bundle.getString("password", "")
    }

    fun callApiRegister(username: String, password: String, nameUser: String, image: File? = null): Flow<AccountState> = flow {
        emit(AccountState.Loading)
        val response = apiClient.insertUser(RegisterBody(nameUser, username = username, password = password, image = image))
//        val response = apiClient.insertUser(nameUser, username, password, image)
        if (response.data.isNullOrEmpty()) {
            emit(AccountState.Failed)
        } else {
            responseModel = Gson().fromJson(response.data.toString(), ResponseModel::class.java)
            emit(AccountState.Finished)
        }
    }
}