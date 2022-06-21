package com.example.androidbaseapp.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.androidbaseapp.data.local.entity.ArticleEntity

@Dao
interface ArticleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArticles(articleEntity: List<ArticleEntity>)

    @Query("SELECT * FROM article")
    fun getPagingSourceArticles(): PagingSource<Int, ArticleEntity>

    @Query("DELETE FROM article")
    suspend fun clearArticles()
}