package com.example.baseprojectandroid.server

import com.example.baseprojectandroid.data.response.GetUserByUserNameResponse
import com.example.baseprojectandroid.data.response.GetUserResponse
import com.example.baseprojectandroid.data.response.InsertSongResponse
import com.example.baseprojectandroid.model.ResponseImage
import com.example.baseprojectandroid.model.ResponseSearch
import com.example.baseprojectandroid.model.Song
import com.skydoves.sandwich.ApiResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface ApiClient {

    @POST("")
    suspend fun login()

    @GET("api/Users/checkUserName")
    suspend fun getUser(@Query("username") userName: String): GetUserByUserNameResponse

//    @POST("/api/Users/insert")

//    @FormUrlEncoded
//    suspend fun insertUser(
//        @Header("Content-Type: multipart/form-data") @Body registerBody: RegisterBody,
////        @Query("name") name: String
//    ): ResponseModel


    @POST("/api/Users/insert")
    @Multipart
    suspend fun insertUser(@Part("name") name: String, @Part("username") username: String, @Part("password") password: String, @Part image: MultipartBody.Part): GetUserResponse

    @Multipart
    @POST("/api/image/upload")
    suspend fun uploadImage(
        @Part file: MultipartBody.Part,
    ): ResponseImage

    @GET("/api/Songs/ShowAll")
    suspend fun getAllSong(): ApiResponse<List<Song>>

    @Multipart
    @POST("/api/Songs/insertbyuser")
    fun uploadSong(
        @Part("name") name: String,
        @Part image: MultipartBody.Part,
        @Part song: MultipartBody.Part,
        @Part("category") category: Int,
        @Part("creator") creator: Int
    ): Call<InsertSongResponse>


    @GET("/api/Search")
    suspend fun search(
        @Query("searchText") searchText: String ,
        @Query("userId") userId: Int
    ):  ApiResponse<List<ResponseSearch>>
}