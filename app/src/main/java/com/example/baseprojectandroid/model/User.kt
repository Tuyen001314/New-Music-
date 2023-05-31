package com.example.baseprojectandroid.model


import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("avatarUrl")
    var avatarUrl: Any = Any(),
    @SerializedName("createdAt")
    var createdAt: String = "",
    @SerializedName("followers")
    var followers: List<Any> = listOf(),
    @SerializedName("followersCount")
    var followersCount: Int = 0,
    @SerializedName("id")
    var id: Int = 0,
    @SerializedName("name")
    var name: String = "",
    @SerializedName("password")
    var password: String = "",
    @SerializedName("playlists")
    var playlists: List<Playlist> = listOf(),
    @SerializedName("userName")
    var userName: String = ""
) {
    companion object {
        val DEFAULT = User()
    }
}