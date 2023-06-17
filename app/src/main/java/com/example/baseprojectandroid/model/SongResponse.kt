package com.example.baseprojectandroid.model


import com.google.gson.annotations.SerializedName

data class SongResponse(
    @SerializedName("category")
    var category: Any,
    @SerializedName("createdAt")
    var createdAt: String,
    @SerializedName("creatorId")
    var creatorId: CreatorId,
    @SerializedName("downloadCount")
    var downloadCount: Int,
    @SerializedName("id")
    var id: Int,
    @SerializedName("listenedCount")
    var listenedCount: Int,
    @SerializedName("name")
    var name: String,
    @SerializedName("thumbnailUrl")
    var thumbnailUrl: Any,
    @SerializedName("url")
    var url: String
)