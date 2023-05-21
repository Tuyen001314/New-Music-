package com.example.baseprojectandroid.ui.component.getting

import androidx.lifecycle.viewModelScope
import com.example.baseprojectandroid.data.api.ResponseModel
import com.example.baseprojectandroid.server.ApiClient
import com.example.baseprojectandroid.ui.base.BaseViewModel
import com.example.baseprojectandroid.utils.Constants.BASE_URL
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

@HiltViewModel
class GettingViewModel
@Inject constructor(private val api: ApiClient): BaseViewModel() {

}