package com.example.baseprojectandroid.ui.component.search

import androidx.lifecycle.viewModelScope
import com.example.baseprojectandroid.model.ResponseSearch
import com.example.baseprojectandroid.server.ApiClient
import com.example.baseprojectandroid.ui.base.BaseViewModel
import com.example.baseprojectandroid.utils.DataState
import com.skydoves.sandwich.suspendMapSuccess
import com.skydoves.sandwich.suspendOnError
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(private val api: ApiClient) : BaseViewModel() {

    var listResultSearch: List<ResponseSearch>? = null
    fun handleSearch(textSearch: String, userId: Int): Flow<DataState<List<ResponseSearch>>> = flow {
        api.search(textSearch, userId).suspendMapSuccess {
            listResultSearch = this
        }.suspendOnError {

        }
    }
}