package com.example.baseprojectandroid.ui.component.setting

import androidx.lifecycle.viewModelScope
import com.example.baseprojectandroid.repository.user.UserRepository
import com.example.baseprojectandroid.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val userRepository: UserRepository
) : BaseViewModel() {

    fun logout() {
        viewModelScope.launch(Dispatchers.IO) {
            userRepository.logOut()
        }
    }
}