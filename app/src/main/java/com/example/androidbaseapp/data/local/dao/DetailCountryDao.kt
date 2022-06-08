package com.example.androidbaseapp.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.androidbaseapp.data.local.entity.DetailCountryEntity

@Dao
interface DetailCountryDao {
    @Insert
    suspend fun insertDetailCountries(detailCountries: List<DetailCountryEntity>)

    @Insert
    suspend fun insertDetailCountry(detailCountry: DetailCountryEntity)

    @Query("SELECT * FROM country_detail")
    fun getPagingSourceDetailCountries(): PagingSource<Int, DetailCountryEntity>

    @Query("DELETE FROM country_detail")
    suspend fun clearDetailCountries()
}