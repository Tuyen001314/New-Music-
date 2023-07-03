package com.example.baseprojectandroid.data


import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import android.os.Parcelable

/**
 * Song response from api
 */
@Parcelize
data class SongRemote(
//    @SerializedName("category")
//    var category: Int? = null,
    @SerializedName("createdAt")
    var createdAt: String = "",
    @SerializedName("creator")
    var creatorId: Int = 0,
    @SerializedName("downloadCount")
    var downloadCount: Int = 0,
    @SerializedName("id")
    var id: Int = 0,
    @SerializedName("listenedCount")
    var listenedCount: Int = 0,
    @SerializedName("name")
    var name: String = "",
    @SerializedName("thumbnailUrl")
    var thumbnailUrl: String? = "",
    @SerializedName("url")
    var url: String = ""
) : Parcelable