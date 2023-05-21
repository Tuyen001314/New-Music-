package com.example.baseprojectandroid.ui.component.getting.signup

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.baseprojectandroid.data.api.ResponseModel
import com.example.baseprojectandroid.server.ApiClient
import com.example.baseprojectandroid.ui.base.BaseViewModel
import com.example.baseprojectandroid.utils.Constants.BASE_URL
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel
@Inject constructor(private val api: ApiClient): BaseViewModel() {

    fun getUser(userName: String): Boolean {
        var checkResponse = false
        viewModelScope.launch(Dispatchers.IO) {
            fetchUser(userName).collect { res ->
                if (res.data.isNullOrBlank()) {
                    withContext(Dispatchers.Main) {
                        checkResponse = true
                    }
                }
                return@collect
            }
        }
        if (checkResponse) return true
        return false
    }

    private fun fetchUser(userName: String): Flow<ResponseModel> = flow {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(ApiClient::class.java)
        //return apiService.getUsers(name)
        val response = apiService.getUser(userName)
        response.enqueue(object : Callback<ResponseModel> {
            override fun onResponse(call: Call<ResponseModel>, response: Response<ResponseModel>) {
                Log.d("cakkkkkkkk", "onResponse: ")
            }

            override fun onFailure(call: Call<ResponseModel>, t: Throwable) {
                Log.d("cakkkkkkkk", "onFail: ")
                t.printStackTrace()
            }

        })
    }

}