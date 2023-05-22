package com.example.baseprojectandroid.model

sealed class AccountState {
    object Loading: AccountState()
    object Finished : AccountState()
    object Failed : AccountState()
}
