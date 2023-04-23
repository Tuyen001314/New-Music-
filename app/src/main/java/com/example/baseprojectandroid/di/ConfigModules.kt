package com.example.baseprojectandroid.di

import com.example.baseprojectandroid.data.DatabaseInfo
import com.example.baseprojectandroid.local.PreferenceInfo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class ConfigModules {

    @Singleton
    @Provides
    @PreferenceInfo
    fun preferencesName(): String {
        return "config"
    }

    @Singleton
    @Provides
    @DatabaseInfo
    fun databaseName(): String {
        return "my_database.db"
    }

}