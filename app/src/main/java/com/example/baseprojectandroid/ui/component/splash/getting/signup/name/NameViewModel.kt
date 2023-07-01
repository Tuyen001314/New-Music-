package com.example.baseprojectandroid.ui.component.splash.getting.signup.name

import android.os.Bundle
import android.telephony.TelephonyCallback.DisplayInfoListener
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.baseprojectandroid.data.response.GetUserResponse
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
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject
import kotlin.math.sign

@HiltViewModel
class NameViewModel @Inject constructor(
    private val userRepository: UserRepository
//    private val apiClient: ApiClient,
) : BaseViewModel() {

    var username: String = ""
    var password: String = ""
    var getUserResponse = GetUserResponse()
    val signInState = MutableLiveData<DataState<User>>()

    fun initData(bundle: Bundle) {
        username = bundle.getString("username", "")
        password = bundle.getString("password", "")
    }

    fun signIn(username: String, password: String, nameUser: String, image: File? = null) {
        signInState.postValue(DataState.Loading)
        viewModelScope.launchSafe(Dispatchers.IO) {
            userRepository.signUp(username, password, nameUser, image)
                .collect {
                    if (it.data == null) {
                        signInState.postValue(DataState.Failure(message = it.message))
                    } else {
                        signInState.postValue(DataState.Success(data = it.data))
                    }
                }
        }
    }
//
//    fun callApiRegister(username: String, password: String, nameUser: String, image: File? = null): Flow<AccountState> = flow {
//        emit(AccountState.Loading)
//        val imagePart = MultipartBody.Part.createFormData("image", "",
//            "".toRequestBody(MultipartBody.FORM)
//        )
//        val response = apiClient.insertUser(nameUser, username = username, password = password, image = imagePart)
////        val response = apiClient.insertUser(nameUser, username, password, image)
//        Log.d("buituyen", response.message + " " + response.status + " " + response.data)
//        if (response.data == null) {
//            emit(AccountState.Failed)
//        } else {
//            emit(AccountState.Finished)
//        }
//    }
}