package com.example.baseprojectandroid.ui.component.getting.signup

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.baseprojectandroid.data.api.ResponseModel
import com.example.baseprojectandroid.server.ApiClient
import com.example.baseprojectandroid.ui.base.BaseViewModel
import com.example.baseprojectandroid.utils.DataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel
@Inject constructor(private val api: ApiClient) : BaseViewModel() {

//    fun getUser(userName: String): Boolean {
//        var checkResponse = false
//        viewModelScope.launch(Dispatchers.IO) {
//            api.getUser(userName)
//            fetchUser(userName).collect { res ->
//                if (res.data.isNullOrEmpty()) {
//                    withContext(Dispatchers.Main) {
//                        checkResponse = true
//                    }
//                }
//                return@collect
//            }
//        }
//        if (checkResponse) return true
//        return false
//    }

}