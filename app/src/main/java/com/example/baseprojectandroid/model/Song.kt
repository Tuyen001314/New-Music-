package com.example.baseprojectandroid.model


import androidx.media3.common.MediaItem
import com.google.gson.annotations.SerializedName

data class Song(
    @SerializedName("category")
    var category: Any = Any(),
    @SerializedName("createdAt")
    var createdAt: String = "",
    @SerializedName("creatorId")
    var creator: User = User.DEFAULT,
    @SerializedName("downloadCount")
    var downloadCount: Int = 0,
    @SerializedName("id")
    var id: Int = 0,
    @SerializedName("listenedCount")
    var listenedCount: Int = 0,
    @SerializedName("name")
    var name: String = "",
    @SerializedName("thumbnailUrl")
    var thumbnailUrl: Any = Any(),
    @SerializedName("url")
    var url: String = ""
) {
    companion object {
        val EMPTY = Song(creator = User.DEFAULT)
    }

    fun defaultState() = SongState(this, SongState.STATE_PAUSE)

    fun toMediaItem() = MediaItem.fromUri(url)

    fun toSinglePlaylist() = Playlist(
        songs = listOf(this)
    )
}