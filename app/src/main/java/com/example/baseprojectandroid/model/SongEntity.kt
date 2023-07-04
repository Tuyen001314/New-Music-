package com.example.baseprojectandroid.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "song_entity")
data class SongEntity(
    @ColumnInfo(name = "id")
    @SerializedName("id")
    var id: String = "",

    @PrimaryKey @ColumnInfo(name = "filePath")
    @SerializedName("filePath")
    var filePath: String = "",

    @SerializedName("thumbnailUrl")
    var thumbnailUrl: String? = "",

    @SerializedName("creatorName")
    var creatorName: String = "",

    @SerializedName("name")
    var name: String = "",
) {
    fun asSong() = Song(
    name = this.name,
    thumbnailUrl = this.thumbnailUrl,
    url = this.filePath
    )
}