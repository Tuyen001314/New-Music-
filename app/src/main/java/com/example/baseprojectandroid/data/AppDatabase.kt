package com.example.baseprojectandroid.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.baseprojectandroid.data.dao.FavoriteDao
import com.example.baseprojectandroid.model.FavoriteEntity

@Database(
    entities = [FavoriteEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favoriteDao(): FavoriteDao
}