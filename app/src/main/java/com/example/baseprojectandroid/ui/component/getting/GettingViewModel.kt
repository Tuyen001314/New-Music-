package com.example.baseprojectandroid.ui.component.getting

import com.example.baseprojectandroid.server.ApiClient
import com.example.baseprojectandroid.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GettingViewModel
@Inject constructor(private val api: ApiClient): BaseViewModel() {
}