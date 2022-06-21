package com.example.androidbaseapp.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = DetailCountryEntity.COUNTRY_DETAIL_TABLE_NAME,
    foreignKeys = [ForeignKey(
        entity = BasicCountryEntity::class,
        parentColumns = [BasicCountryEntity.COUNTRY_COLUMN_ID],
        childColumns = [DetailCountryEntity.COUNTRY_COLUMN_COUNTRY_ID],
        onDelete = ForeignKey.CASCADE
    )]
)
data class DetailCountryEntity(
    @PrimaryKey
    @ColumnInfo(name = COUNTRY_COLUMN_ID) val id: String,
    @ColumnInfo(name = COUNTRY_COLUMN_COUNTRY_ID, index = true) val countryId: Int = 0,
    @ColumnInfo(name = COUNTRY_COLUMN_CITY) val city: String,
    @ColumnInfo(name = COUNTRY_COLUMN_CITY_CODE) val cityCode: String,
    @ColumnInfo(name = COUNTRY_COLUMN_COUNTRY) val country: String,
    @ColumnInfo(name = COUNTRY_COLUMN_COUNTRY_CODE) val countryCode: String,
    @ColumnInfo(name = COUNTRY_COLUMN_COUNTRY_LATITUDE) val lat: String,
    @ColumnInfo(name = COUNTRY_COLUMN_COUNTRY_LONGITUDE) val lon: String,
    @ColumnInfo(name = COUNTRY_COLUMN_COUNTRY_PROVINCE) val province: String,
    @ColumnInfo(name = COUNTRY_COLUMN_COUNTRY_CONFIRMED) val confirmed: Int,
    @ColumnInfo(name = COUNTRY_COLUMN_COUNTRY_ACTIVE) val active: Int,
    @ColumnInfo(name = COUNTRY_COLUMN_COUNTRY_DEATHS) val deaths: Int,
    @ColumnInfo(name = COUNTRY_COLUMN_COUNTRY_RECOVERED) val recovered: Int,
    @ColumnInfo(name = COUNTRY_COLUMN_COUNTRY_DATE) val date: String,
) {
    companion object {
        const val COUNTRY_DETAIL_TABLE_NAME = "child_country_detail"
        const val COUNTRY_COLUMN_ID = "id"
        const val COUNTRY_COLUMN_COUNTRY_ID = "country_id"
        const val COUNTRY_COLUMN_CITY = "country_city"
        const val COUNTRY_COLUMN_CITY_CODE = "country_city_code"
        const val COUNTRY_COLUMN_COUNTRY = "country_country"
        const val COUNTRY_COLUMN_COUNTRY_CODE = "country_country_code"
        const val COUNTRY_COLUMN_COUNTRY_LATITUDE = "country_latitude"
        const val COUNTRY_COLUMN_COUNTRY_LONGITUDE = "country_longitude"
        const val COUNTRY_COLUMN_COUNTRY_PROVINCE = "country_province"
        const val COUNTRY_COLUMN_COUNTRY_CONFIRMED = "country_confirmed"
        const val COUNTRY_COLUMN_COUNTRY_ACTIVE = "country_active"
        const val COUNTRY_COLUMN_COUNTRY_DEATHS = "country_deaths"
        const val COUNTRY_COLUMN_COUNTRY_RECOVERED = "country_recovered"
        const val COUNTRY_COLUMN_COUNTRY_DATE = "country_date"
    }
}
