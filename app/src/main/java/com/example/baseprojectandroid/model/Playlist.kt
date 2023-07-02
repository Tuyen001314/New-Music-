package com.example.baseprojectandroid.model


import android.os.Parcelable
import com.example.baseprojectandroid.data.SongRemote
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Playlist(
    @SerializedName("createdAt")
    var createdAt: String = "",
    @SerializedName("creator")
    var creatorId: Int = 0,
    @SerializedName("favorite")
    var isFavorite: Boolean = false,
    @SerializedName("id")
    var id: Int = 0,
    @SerializedName("name")
    var name: String = "",
    @SerializedName("songs")
    var songs: List<Song> = listOf()
): Parcelable {

}