package com.example.baseprojectandroid.server

import com.example.baseprojectandroid.data.api.ResponseModel
import com.example.baseprojectandroid.model.RegisterBody
import com.example.baseprojectandroid.model.UploadResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*
import java.io.File

interface ApiClient {

    @POST("")
    suspend fun login()

    @GET("api/Users/checkUserName")
    suspend fun getUser(@Query("username") userName: String): ResponseModel

//    @POST("/api/Users/insert")

//    @FormUrlEncoded
//    suspend fun insertUser(
//        @Header("Content-Type: multipart/form-data") @Body registerBody: RegisterBody,
////        @Query("name") name: String
//    ): ResponseModel


    @POST("/api/Users/insert")
    @Multipart
    suspend fun insertUser(@Part("name") name: String, @Part("username") username: String, @Part("password") password: String, @Part image: MultipartBody.Part): ResponseModel


    @Multipart
    @POST("")
    suspend fun uploadImage(
        @Part image: MultipartBody.Part,
        @Part("desc") desc: RequestBody
    ): Call<UploadResponse>
}