package com.example.baseprojectandroid.model


import com.google.gson.annotations.SerializedName

data class Playlists(
    @SerializedName("createdAt")
    var createdAt: String,
    @SerializedName("creatorId")
    var creatorId: Int,
    @SerializedName("favorite")
    var favorite: Boolean,
    @SerializedName("id")
    var id: Int,
    @SerializedName("name")
    var name: String,
    @SerializedName("songs")
    var songs: List<SongResponse>
)