package com.example.baseprojectandroid.di

import com.example.baseprojectandroid.repository.song.FakeSongRepositoryImpl
import com.example.baseprojectandroid.repository.song.SongRepository
import com.example.baseprojectandroid.repository.song.SongRepositoryImpl
import com.example.baseprojectandroid.repository.user.UserRepository
import com.example.baseprojectandroid.repository.user.UserRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindSongRepository(
        impl: SongRepositoryImpl
    ): SongRepository

    @Binds
    abstract fun bindUserRepository(
        impl: UserRepositoryImpl
    ): UserRepository
}