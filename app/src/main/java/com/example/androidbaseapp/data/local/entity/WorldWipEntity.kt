package com.example.androidbaseapp.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.androidbaseapp.data.local.entity.WorldWipEntity.Companion.WORLD_TABLE_NAME

@Entity(tableName = WORLD_TABLE_NAME)
data class WorldWipEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = WORLD_COLUMN_ID) val worldId: Int,
    @ColumnInfo(name = WORLD_COLUMN_NEW_CONFIRMED_STATUS) val newConfirmed: Int,
    @ColumnInfo(name = WORLD_COLUMN_TOTAL_CONFIRMED_STATUS) val totalConfirmed: Int,
    @ColumnInfo(name = WORLD_COLUMN_NEW_DEATHS_STATUS) val newDeaths: Int,
    @ColumnInfo(name = WORLD_COLUMN_NEW_RECOVERED_STATUS) val newRecovered: Int,
    @ColumnInfo(name = WORLD_COLUMN_TOTAL_RECOVERED_STATUS) val totalRecovered: Int,
    @ColumnInfo(name = WORLD_COLUMN_DATE) val date: Int,
) {
    companion object {
        const val WORLD_TABLE_NAME = "world"
        const val WORLD_COLUMN_ID = "world_column_id"
        const val WORLD_COLUMN_NEW_CONFIRMED_STATUS = "world_column_new_confirmed_status"
        const val WORLD_COLUMN_TOTAL_CONFIRMED_STATUS = "world_column_total_confirmed_status"
        const val WORLD_COLUMN_NEW_DEATHS_STATUS = "world_column_new_deaths_status"
        const val WORLD_COLUMN_NEW_RECOVERED_STATUS = "world_column_new_recovered_status"
        const val WORLD_COLUMN_TOTAL_RECOVERED_STATUS = "world_column_total_recovered_status"
        const val WORLD_COLUMN_DATE = "world_column_date"
    }
}