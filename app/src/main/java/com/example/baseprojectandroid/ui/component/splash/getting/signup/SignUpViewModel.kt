package com.example.baseprojectandroid.ui.component.splash.getting.signup

import com.example.baseprojectandroid.model.AccountState
import com.example.baseprojectandroid.server.ApiClient
import com.example.baseprojectandroid.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel
@Inject constructor(private val api: ApiClient) : BaseViewModel() {

    fun fetchUser(userName: String): Flow<AccountState> = flow {
        emit(AccountState.Loading)
        val response = api.getUser(userName)
        if (response.data == null) {
            emit(AccountState.Finished)
        } else {
            emit(AccountState.Failed)
        }
    }
}