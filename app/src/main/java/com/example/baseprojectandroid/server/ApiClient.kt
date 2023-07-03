package com.example.baseprojectandroid.server

import com.example.baseprojectandroid.data.SongRemote
import com.example.baseprojectandroid.data.response.GetUserByUserNameResponse
import com.example.baseprojectandroid.data.response.GetUserResponse
import com.example.baseprojectandroid.data.response.InsertSongResponse
import com.example.baseprojectandroid.model.ResponseImage
import com.example.baseprojectandroid.model.ResponseSearch
import com.example.baseprojectandroid.model.Song
import com.example.baseprojectandroid.model.User
import com.skydoves.sandwich.ApiResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface ApiClient {

    @POST("")
    suspend fun login()

    @GET("api/Users/checkUserName")
    suspend fun getUser(@Query("username") userName: String): GetUserByUserNameResponse

    @GET("/api/Users/show/{id}")
    suspend fun getUserById(@Path("id") id: Int): GetUserResponse

    @GET("/api/Users/ShowAll")
    suspend fun getAllUser(): List<User>

    @GET("/api/Users/login")
    suspend fun signIn(@Query("username") userName: String, @Query("password") password: String): ApiResponse<GetUserByUserNameResponse>

    @POST("/api/Users/insert")
    @Multipart
    suspend fun insertUser(@Part("name") name: String, @Part("username") username: String, @Part("password") password: String, @Part image: MultipartBody.Part): GetUserResponse

    @Multipart
    @POST("/api/image/upload")
    suspend fun uploadImage(
        @Part file: MultipartBody.Part,
    ): ResponseImage

    @GET("/api/Songs/ShowAll")
    suspend fun getAllSong(): ApiResponse<List<SongRemote>>

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
    ):  ApiResponse<List<Song>>

    @POST("/api/Users/changeAvatar")
    suspend fun changeAvatar(
        @Part("userId") userId: Int,
        @Part("image") image: MultipartBody.Part
    ): Call<InsertSongResponse>
}