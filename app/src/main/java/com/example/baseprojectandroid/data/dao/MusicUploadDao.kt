package com.example.baseprojectandroid.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.example.baseprojectandroid.model.SongUploadEntity

@Dao
interface MusicUploadDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(songEntity: SongUploadEntity)
}