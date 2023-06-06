package com.example.baseprojectandroid.model


import android.os.Parcelable
import androidx.core.os.bundleOf
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Song(
    @SerializedName("category")
    var category: String = "",
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
    var thumbnailUrl: String = "",
    @SerializedName("url")
    var url: String = ""
): Parcelable {
    companion object {
        val EMPTY = Song(creator = User.DEFAULT)
    }

    fun defaultState() = SongState(this, SongState.STATE_PAUSE)

    fun toMediaItem() = MediaItem.fromUri(url)
        .buildUpon()
        .setMediaId(id.toString())
        .setMediaMetadata(
            MediaMetadata.Builder()
                .setTitle(name)
                .setArtist(creator.name)
                .setExtras(
                    bundleOf("song" to this )
                )
                .build()
        )
        .build()

    fun toSinglePlaylist() = Playlist(
        songs = listOf(this)
    )
}