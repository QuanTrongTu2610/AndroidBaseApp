package com.example.androidbaseapp.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.androidbaseapp.data.local.entity.DetailCountryEntity

@Dao
interface DetailCountryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDetailCountries(detailCountries: List<DetailCountryEntity>)

    @Query("SELECT * FROM child_country_detail")
    fun getPagingSourceDetailCountries(): PagingSource<Int, DetailCountryEntity>

    @Query("DELETE FROM child_country_detail")
    suspend fun clearDetailCountries()
}