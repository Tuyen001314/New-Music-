package com.example.baseprojectandroid.ui.component.search

import android.provider.ContactsContract.Data
import androidx.lifecycle.viewModelScope
import com.example.baseprojectandroid.local.LocalStorage
import com.example.baseprojectandroid.model.ResponseSearch
import com.example.baseprojectandroid.model.Song
import com.example.baseprojectandroid.repository.user.UserRepository
import com.example.baseprojectandroid.server.ApiClient
import com.example.baseprojectandroid.ui.base.BaseViewModel
import com.example.baseprojectandroid.utils.DataState
import com.example.baseprojectandroid.utils.Logger
import com.skydoves.sandwich.message
import com.skydoves.sandwich.suspendMapSuccess
import com.skydoves.sandwich.suspendOnError
import com.skydoves.sandwich.suspendOnProcedure
import com.skydoves.sandwich.suspendOnSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(private val api: ApiClient,
                                          private val userRepository: UserRepository) : BaseViewModel() {

    fun handleSearch(textSearch: String, userId: Int): Flow<DataState<List<Song>>> = flow {
        api.search(textSearch, userId).suspendOnError {
            emit(DataState.Failure(message()))
        }.suspendMapSuccess {
            map {
                Song(
                    it.createdAt,
                    it.creatorId,
                    it.downloadCount,
                    it.id,
                    it.listenedCount,
                    it.name,
                    it.thumbnailUrl,
                    it.url
                ).apply {
                    userRepository.getUser(it.creatorId)?.let { it1 -> setCreator(it1) }
                }
            }
        }.suspendOnSuccess {
            emit(DataState.Success(data = data))
        }
    }
}