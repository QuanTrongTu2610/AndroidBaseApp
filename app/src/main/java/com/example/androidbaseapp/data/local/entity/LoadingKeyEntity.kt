package com.example.androidbaseapp.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.androidbaseapp.data.local.entity.LoadingKeyEntity.Companion.LOADING_KEY_TABLE_NAME

@Entity(tableName = LOADING_KEY_TABLE_NAME)
data class LoadingKeyEntity(
    @PrimaryKey @ColumnInfo(name = LOADING_KEY_COLUMN_ID) val id: Int,
    @ColumnInfo(name = LOADING_KEY_COLUMN_PRE_KEY) val prevKey: Int?,
    @ColumnInfo(name = LOADING_KEY_COLUMN_NEXT_KEY) val nextKey: Int?,
    @ColumnInfo(name = LOADING_KEY_TYPE) val keyType: Int = 0
) {
    companion object {
        const val LOADING_KEY_TABLE_NAME = "loading_key"
        const val LOADING_KEY_COLUMN_ID = "id"
        const val LOADING_KEY_COLUMN_PRE_KEY = "prev_key"
        const val LOADING_KEY_COLUMN_NEXT_KEY = "next_key"
        const val LOADING_KEY_TYPE = "key_type"
    }
}