package com.example.baseprojectandroid.ui.component

import com.example.baseprojectandroid.data.AppDatabase
import com.example.baseprojectandroid.local.LocalStorage
import com.example.baseprojectandroid.server.ApiClient
import com.example.baseprojectandroid.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val apiClient: ApiClient,
    private val localStorage: LocalStorage,
    private val database: AppDatabase
) : BaseViewModel() {

}