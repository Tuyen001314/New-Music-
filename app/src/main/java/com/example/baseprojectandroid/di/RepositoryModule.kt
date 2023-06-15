package com.example.baseprojectandroid.di

import com.example.baseprojectandroid.repository.song.FakeSongRepositoryImpl
import com.example.baseprojectandroid.repository.song.SongRepository
import com.example.baseprojectandroid.repository.song.SongRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindSongRepository(
        impl: FakeSongRepositoryImpl
    ): SongRepository
}