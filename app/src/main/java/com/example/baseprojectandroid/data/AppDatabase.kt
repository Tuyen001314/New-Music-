package com.example.baseprojectandroid.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.baseprojectandroid.data.dao.FavoriteDao
import com.example.baseprojectandroid.data.dao.MusicDao
import com.example.baseprojectandroid.data.dao.MusicUploadDao
import com.example.baseprojectandroid.model.FavoriteEntity
import com.example.baseprojectandroid.model.SongEntity
import com.example.baseprojectandroid.model.SongUploadEntity

@Database(
    entities = [FavoriteEntity::class, SongEntity::class, SongUploadEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favoriteDao(): FavoriteDao
    abstract fun musicDao(): MusicDao
    abstract fun musicUploadDao(): MusicUploadDao

}