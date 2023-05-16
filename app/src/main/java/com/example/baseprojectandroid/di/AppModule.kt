package com.example.baseprojectandroid.di

import android.content.Context
import com.example.baseprojectandroid.local.LocalData
import com.example.baseprojectandroid.local.LocalStorage
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
}
