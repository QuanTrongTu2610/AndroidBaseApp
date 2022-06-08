package com.example.androidbaseapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.androidbaseapp.data.local.entity.LoadingKeyEntity

@Dao
interface LoadingKeyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLoadingKeys(loadingKeys: List<LoadingKeyEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLoadingKey(loadingKey: LoadingKeyEntity)

    @Query("SELECT * FROM loading_key WHERE id = :dataId")
    suspend fun getLoadingKeyById(dataId: Int): LoadingKeyEntity?

    @Query("DELETE FROM loading_key")
    suspend fun clearLoadingKeys()

}