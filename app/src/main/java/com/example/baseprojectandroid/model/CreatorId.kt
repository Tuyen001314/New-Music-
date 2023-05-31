package com.example.baseprojectandroid.model


import com.google.gson.annotations.SerializedName

data class CreatorId(
    @SerializedName("avatarUrl")
    var avatarUrl: Any,
    @SerializedName("createdAt")
    var createdAt: String,
    @SerializedName("followers")
    var followers: List<User>,
    @SerializedName("followersCount")
    var followersCount: Int,
    @SerializedName("id")
    var id: Int,
    @SerializedName("name")
    var name: String,
    @SerializedName("password")
    var password: String,
    @SerializedName("playlists")
    var playlists: List<Playlists>,
    @SerializedName("userName")
    var userName: String
)