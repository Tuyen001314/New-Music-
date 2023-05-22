package com.example.baseprojectandroid.ui.component.splash.getting.signup.name

import android.os.Bundle
import com.example.baseprojectandroid.local.LocalStorage
import com.example.baseprojectandroid.model.AccountState
import com.example.baseprojectandroid.server.ApiClient
import com.example.baseprojectandroid.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@HiltViewModel
class NameViewModel @Inject constructor(
    private val apiClient: ApiClient,
) : BaseViewModel() {

    var username: String = ""
    var password: String = ""

    fun initData(bundle: Bundle) {
        username = bundle.getString("username", "")
        password = bundle.getString("password", "")
    }

    fun callApiRegister(): Flow<AccountState> = flow {
        emit(AccountState.Loading)
       /* val response = apiClient.getUser()*/
    }
}