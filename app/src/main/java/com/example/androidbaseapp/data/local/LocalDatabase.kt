package com.example.androidbaseapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.androidbaseapp.data.local.dao.BasicCountryDao
import com.example.androidbaseapp.data.local.dao.DetailCountryDao
import com.example.androidbaseapp.data.local.dao.LoadingKeyDao
import com.example.androidbaseapp.data.local.entity.BasicCountryEntity
import com.example.androidbaseapp.data.local.entity.DetailCountryEntity
import com.example.androidbaseapp.data.local.entity.LoadingKeyEntity

@Database(
    entities = [
        DetailCountryEntity::class,
        BasicCountryEntity::class,
        LoadingKeyEntity::class
    ],
    exportSchema = false,
    version = 1
)
abstract class LocalDatabase : RoomDatabase() {
    abstract fun getBasicCountryDao(): BasicCountryDao

    abstract fun getLoadingKeyDao(): LoadingKeyDao

    abstract fun getDetailCountryDao(): DetailCountryDao
}