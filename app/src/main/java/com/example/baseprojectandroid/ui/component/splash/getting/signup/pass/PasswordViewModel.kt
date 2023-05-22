package com.example.baseprojectandroid.ui.component.splash.getting.signup.pass

import android.os.Bundle
import com.example.baseprojectandroid.server.ApiClient
import com.example.baseprojectandroid.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PasswordViewModel @Inject constructor(
    private val apiClient: ApiClient
) : BaseViewModel() {

    lateinit var userName: String

    fun initData(bundle: Bundle) {
        userName = bundle.getString("username", "")
    }
}