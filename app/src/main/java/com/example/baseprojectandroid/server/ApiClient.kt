package com.example.baseprojectandroid.server

import com.example.baseprojectandroid.data.api.ResponseModel
import com.example.baseprojectandroid.model.RegisterBody
import com.example.baseprojectandroid.model.UploadResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query
import java.io.File

interface ApiClient {

    @POST("")
    suspend fun login()

    @GET("api/Users/checkUserName")
    suspend fun getUser(@Query("username") userName: String): ResponseModel

    @POST("/api/Users/insert")
    suspend fun insertUser(@Body registerBody: RegisterBody): ResponseModel

/*
    @POST("/api/Users/insert")
    suspend fun insertUser(@Field("name") name: String, @Field("username") username: String, @Field("password") password: String, @Field("image") image: File?): ResponseModel
*/

    @Multipart
    @POST("")
    suspend fun uploadImage(
        @Part image: MultipartBody.Part,
        @Part("desc") desc: RequestBody
    ): Call<UploadResponse>
}