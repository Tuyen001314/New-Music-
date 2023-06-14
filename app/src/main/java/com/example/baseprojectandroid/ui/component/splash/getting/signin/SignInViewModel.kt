package com.example.baseprojectandroid.ui.component.splash.getting.signin

import androidx.lifecycle.viewModelScope
import com.example.baseprojectandroid.data.api.ResponseModel
import com.example.baseprojectandroid.model.AccountState
import com.example.baseprojectandroid.server.ApiClient
import com.example.baseprojectandroid.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class SignInViewModel
@Inject constructor(private val api: ApiClient) : BaseViewModel() {

    fun fetchUser(userName: String): Flow<AccountState> = flow {
        emit(AccountState.Loading)
        val response = api.getUser(userName)
        if (response.data != null) {
            emit(AccountState.Finished)
        } else {
            emit(AccountState.Failed)
        }
    }
}