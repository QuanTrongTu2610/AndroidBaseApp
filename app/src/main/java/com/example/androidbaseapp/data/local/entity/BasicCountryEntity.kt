package com.example.androidbaseapp.data.local.entity


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.androidbaseapp.data.local.entity.BasicCountryEntity.Companion.COUNTRY_BASIC_TABLE_NAME

@Entity(tableName = COUNTRY_BASIC_TABLE_NAME)
data class BasicCountryEntity(
    @PrimaryKey
    @ColumnInfo(name = COUNTRY_COLUMN_ID) val id: Int = 0,
    @ColumnInfo(name = COUNTRY_COLUMN_FULL_NAME) val fullName: String = "",
    @ColumnInfo(name = COUNTRY_COLUMN_ABBREVIATION) val abbreviation: String = "",
    @ColumnInfo(name = COUNTRY_COLUMN_SHORT_NAME) val shortName: String = ""
) {
    companion object {
        const val COUNTRY_BASIC_TABLE_NAME = "country"
        const val COUNTRY_COLUMN_ID = "id"
        const val COUNTRY_COLUMN_FULL_NAME = "fullName"
        const val COUNTRY_COLUMN_ABBREVIATION = "abbreviation"
        const val COUNTRY_COLUMN_SHORT_NAME = "shortName"
    }
}