package com.example.baseprojectandroid.repository.user

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.baseprojectandroid.data.response.GetUserResponse
import com.example.baseprojectandroid.local.LocalStorage
import com.example.baseprojectandroid.model.User
import com.example.baseprojectandroid.server.ApiClient
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
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
) : UserRepository {

    override suspend fun getCurrentUser(): User? {
        return localStorage.currentUser
    }

    override suspend fun signIn(userName: String, password: String): Flow<GetUserResponse> = flow {

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