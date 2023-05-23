package com.example.baseprojectandroid.model


import com.google.gson.annotations.SerializedName

data class Playlist(
    @SerializedName("createdAt")
    var createdAt: String = "",
    @SerializedName("creatorId")
    var creatorId: Int = 0,
    @SerializedName("favorite")
    var isFavorite: Boolean = false,
    @SerializedName("id")
    var id: Int = 0,
    @SerializedName("name")
    var name: String = "",
    @SerializedName("songs")
    var songs: List<Song> = listOf()
) {

}