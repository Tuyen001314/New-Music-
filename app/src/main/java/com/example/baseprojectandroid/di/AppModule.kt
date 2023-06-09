package com.example.baseprojectandroid.di

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Room
import com.bumptech.glide.Glide
import com.example.baseprojectandroid.data.AppDatabase
import com.example.baseprojectandroid.data.DatabaseInfo
import com.example.baseprojectandroid.local.LocalData
import com.example.baseprojectandroid.local.LocalStorage
import com.example.baseprojectandroid.model.User
import com.example.baseprojectandroid.service.MusicServiceConnector
import com.example.baseprojectandroid.utils.Network
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton
import kotlin.coroutines.CoroutineContext

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    @Singleton
    fun provideLocalRepository(localStorage: LocalData): LocalStorage = localStorage

    @Provides
    @Singleton
    fun provideCoroutineContext(): CoroutineContext {
        return Dispatchers.IO
    }

    @Provides
    @Singleton
    fun provideNetworkConnectivity(netWork: Network) = netWork

    @Provides
    @Singleton
    fun provideMusicServiceConnector(@ApplicationContext context: Context) = MusicServiceConnector(context)

    @Provides
    @Singleton
    fun provideGlide(@ApplicationContext context: Context) = Glide.with(context)

    @Provides
    @Singleton
    fun provideCurrentUserLiveData(localStorage: LocalStorage): LiveData<User> = MutableLiveData(localStorage.currentUser)

    @Singleton
    @Provides
    fun appDatabase(
        @ApplicationContext context: Context,
        @DatabaseInfo dbName: String
    ): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, dbName)
            .build()
    }
}
