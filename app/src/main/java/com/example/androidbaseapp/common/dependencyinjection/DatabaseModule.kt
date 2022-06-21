package com.example.androidbaseapp.common.dependencyinjection

import android.content.Context
import androidx.room.Room
import com.example.androidbaseapp.data.local.dao.BasicCountryDao
import com.example.androidbaseapp.data.local.LocalDatabase
import com.example.androidbaseapp.data.local.dao.ArticleDao
import com.example.androidbaseapp.data.local.migration.Migration1to2
import com.example.androidbaseapp.data.local.dao.DetailCountryDao
import com.example.androidbaseapp.data.local.dao.LoadingKeyDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {
    @Provides
    @Singleton
    fun provideRoomDatabase(
        @ApplicationContext context: Context
    ): LocalDatabase = Room.databaseBuilder(
        context,
        LocalDatabase::class.java,
        "app-base-database"
    )
        .fallbackToDestructiveMigration()
        .addMigrations(Migration1to2)
        .build()

    @Provides
    @Singleton
    fun provideBasicCountryDao(
        database: LocalDatabase
    ): BasicCountryDao = database.getBasicCountryDao()

    @Provides
    @Singleton
    fun provideLoadingKeyDao(
        database: LocalDatabase
    ): LoadingKeyDao = database.getLoadingKeyDao()

    @Provides
    @Singleton
    fun provideDetailCountryDao(
        database: LocalDatabase
    ): DetailCountryDao = database.getDetailCountryDao()

    @Provides
    @Singleton
    fun provideArticleDao(
        database: LocalDatabase
    ): ArticleDao = database.getArticleDao()
}