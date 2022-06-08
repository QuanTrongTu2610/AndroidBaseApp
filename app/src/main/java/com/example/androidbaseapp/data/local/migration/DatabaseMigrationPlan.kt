package com.example.androidbaseapp.data.local.migration

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.androidbaseapp.data.local.entity.BasicCountryEntity
import com.example.androidbaseapp.data.local.entity.LoadingKeyEntity
import com.example.androidbaseapp.data.local.entity.DetailCountryEntity
import com.example.androidbaseapp.utils.Logger

/**
 * Migration to version 2
 * */
object Migration1to2 : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        Logger.d("Database.version:  ${database.version}")
        database.execSQL("""DROP TABLE ${BasicCountryEntity.COUNTRY_BASIC_TABLE_NAME}""".trimIndent())
        database.execSQL("""DROP TABLE ${DetailCountryEntity.COUNTRY_DETAIL_TABLE_NAME}""".trimIndent())
        database.execSQL("""DROP TABLE ${LoadingKeyEntity.LOADING_KEY_TABLE_NAME}""".trimIndent())
    }
}

