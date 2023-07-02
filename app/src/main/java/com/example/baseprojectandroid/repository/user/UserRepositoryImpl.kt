package com.example.baseprojectandroid.repository.user

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.baseprojectandroid.data.response.GetUserResponse
import com.example.baseprojectandroid.local.LocalStorage
import com.example.baseprojectandroid.model.User
import com.example.baseprojectandroid.server.ApiClient
import com.example.baseprojectandroid.utils.DataState
import com.example.baseprojectandroid.utils.Logger
import com.skydoves.sandwich.message
import com.skydoves.sandwich.suspendOnError
import com.skydoves.sandwich.suspendOnException
import com.skydoves.sandwich.suspendOnSuccess
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class UserRepositoryImpl @Inject constructor(
    private val apiClient: ApiClient,
    private val localStorage: LocalStorage,
    private val currentUserLiveData: LiveData<User>,
    private val io: CoroutineContext
) : UserRepository, CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = io

    private val allUsers = localStorage.allUsers.toMutableList()

    override suspend fun getCurrentUser(): User? {
        return localStorage.currentUser
    }

    override suspend fun getUser(id: Int): User? {
        if (allUsers.isNotEmpty()) {
            val user = allUsers.find { it.id == id }
            if (user != null) return user
        }
        return withContext(io) {
            val user = apiClient.getUserById(id).data
            user?.let {
                allUsers.add(it)
                localStorage.allUsers = allUsers
            }
            user
        }
    }

    override suspend fun signIn(userName: String, password: String): Flow<DataState<User>> = flow {
        apiClient.signIn(userName, password)
            .suspendOnSuccess {
                if (data.data.isNullOrEmpty()) {
                    emit(DataState.Failure("Username or password is incorrect"))
                } else {
                    data.data!![0].let {
                        emit(DataState.Success(data = it))
                        localStorage.currentUser = it
                        (currentUserLiveData as MutableLiveData).postValue(it)
                    }
                }
            }.suspendOnError {
                Logger.d(message())
                emit(DataState.Failure(message()))
            }.suspendOnException {
                Logger.d(message())
                emit(DataState.Failure(message()))
            }
    }

    override suspend fun signUp(
        userName: String,
        password: String,
        name: String,
        image: File?
    ): Flow<GetUserResponse> = flow {
        //check username existed
        val getUserWithUserName = apiClient.getUser(userName)
        if (getUserWithUserName.data != null) {
            emit(GetUserResponse().apply {
                message = "username existed!"
            })
            return@flow
        }

        val imagePart = MultipartBody.Part.createFormData(
            "image", image?.name ?: "",
            image?.asRequestBody(MultipartBody.FORM) ?: "".toRequestBody(MultipartBody.FORM)
        )

        val response = apiClient.insertUser(name, userName, password, imagePart)
        emit(response)
        response.data?.let {
            localStorage.currentUser = it
            (currentUserLiveData as MutableLiveData).postValue(it)
        }
    }


    override suspend fun logOut() {
        localStorage.currentUser = null
        (currentUserLiveData as MutableLiveData).postValue(null)
    }

}