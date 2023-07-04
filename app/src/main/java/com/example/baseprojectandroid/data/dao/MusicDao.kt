package com.example.baseprojectandroid.data.dao

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.*
import com.example.baseprojectandroid.model.FavoriteEntity
import com.example.baseprojectandroid.model.SongEntity


@Dao
interface MusicDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(songEntity: SongEntity)

    @Query("DELETE FROM song_entity WHERE filePath = :path")
    fun delete(path: String)

    @Query("SELECT * FROM song_entity WHERE filePath = :path")
    fun get(path: String): SongEntity?

    @Query("SELECT * FROM song_entity WHERE id = :id")
    fun getById(id: String): SongEntity?

    @Query("SELECT * FROM song_entity")
    fun getAllSongDownload(): LiveData<List<SongEntity>>
}