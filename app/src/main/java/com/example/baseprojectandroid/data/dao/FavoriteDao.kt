package com.example.baseprojectandroid.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.baseprojectandroid.model.FavoriteEntity


@Dao
interface FavoriteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(favoriteEntity: FavoriteEntity): Long

    @Query("DELETE FROM favorite_entity WHERE path = :path")
    fun delete(path: String)

    @Update
    fun update(cacheModel: FavoriteEntity)

    @Query("SELECT * FROM favorite_entity WHERE path = :path")
    fun get(path: String): FavoriteEntity?

    @Query("SELECT * FROM favorite_entity WHERE id = :id")
    suspend fun getFavoriteById(id: String): FavoriteEntity?

    @Query("DELETE FROM favorite_entity WHERE id = :id")
    suspend fun deleteFavoriteById(id: Long)

    @Query("SELECT * FROM favorite_entity")
    suspend fun getAllSongFavorite(): List<FavoriteEntity>

    @Query("SELECT * FROM favorite_entity")
    fun selectAllFavoriteLiveData(): LiveData<List<FavoriteEntity>>

    @Query("SELECT * FROM favorite_entity WHERE id = :id")
    fun getFavoriteByIdLiveData(id: Long): LiveData<FavoriteEntity?>

}