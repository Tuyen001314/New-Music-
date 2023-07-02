package com.example.baseprojectandroid.ui.component.splash.getting.signin

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.baseprojectandroid.model.AccountState
import com.example.baseprojectandroid.model.User
import com.example.baseprojectandroid.repository.user.UserRepository
import com.example.baseprojectandroid.server.ApiClient
import com.example.baseprojectandroid.ui.base.BaseViewModel
import com.example.baseprojectandroid.utils.DataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel
@Inject constructor(private val userRepository: UserRepository) : BaseViewModel() {

    val signInResponseLiveData = MutableLiveData<DataState<User>>()

    fun signIn(userName: String, password: String) {
        signInResponseLiveData.postValue(DataState.Loading)
        viewModelScope.launchSafe(Dispatchers.IO) {
            userRepository.signIn(userName, password).collect {
                signInResponseLiveData.postValue(it)
            }
        }
    }
}