package com.example.baseprojectandroid.model


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
    @SerializedName("avatarUrl")
    var avatarUrl: String? = "",
    @SerializedName("createdAt")
    var createdAt: String = "",

    @Transient
    @SerializedName("followers")
    var followers: List<User> = listOf(),
    @SerializedName("followersCount")
    var followersCount: Int = 0,
    @SerializedName("id")
    var id: Int = 0,
    @SerializedName("name")
    var name: String = "",
    @SerializedName("password")
    var password: String = "",

    @Transient
    @SerializedName("playlists")
    var playlists: List<Playlist> = listOf(),
    @SerializedName("userName")
    var userName: String = ""
): Parcelable {
    companion object {
        val DEFAULT = User()
    }
}