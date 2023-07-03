package com.example.baseprojectandroid.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "song_upload_entity")
data class SongUploadEntity(
    @ColumnInfo(name = "id")
    @SerializedName("id")
    var id: String = "",

    @PrimaryKey @ColumnInfo(name = "filePath")
    @SerializedName("filePath")
    var filePath: String = "",

    @SerializedName("thumbnailUrl")
    var thumbnailUrl: String? = "",

    @SerializedName("creatorId")
    var creatorId: Int = 0,

    @SerializedName("name")
    var name: String = "",
)