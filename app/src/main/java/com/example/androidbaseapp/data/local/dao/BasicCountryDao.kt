package com.example.androidbaseapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.androidbaseapp.data.local.entity.BasicCountryEntity

@Dao
interface BasicCountryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBasicCountry(basicCountryEntity: BasicCountryEntity)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateBasicCountry(basicCountryEntity: BasicCountryEntity)

    @Query("SELECT * FROM country")
    suspend fun getBasicCountries(): List<BasicCountryEntity>

    @Query("SELECT * FROM country WHERE id BETWEEN :startId AND :endId")
    suspend fun getBasicCountries(startId: Int, endId: Int): List<BasicCountryEntity>

    @Query("DELETE FROM country")
    suspend fun clearBasicCountries()

    @Query("SELECT COUNT(id) FROM country")
    suspend fun countBasicCountries(): Int
}