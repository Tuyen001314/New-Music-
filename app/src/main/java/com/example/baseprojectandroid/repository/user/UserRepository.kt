package com.example.baseprojectandroid.repository.user

import com.example.baseprojectandroid.data.response.GetUserResponse
import com.example.baseprojectandroid.model.ResponseImage
import com.example.baseprojectandroid.model.User
import kotlinx.coroutines.flow.Flow
import java.io.File

interface UserRepository {
    suspend fun getCurrentUser(): User?

    suspend fun signIn(userName: String, password: String): Flow<GetUserResponse>

    suspend fun signUp(userName: String, password: String, name: String, image: File? = null): Flow<GetUserResponse>

    suspend fun logOut()
}