package com.example.androidbaseapp.data.remote.entity


import com.google.gson.annotations.SerializedName

data class CovidDetailCountryResult(
    @SerializedName("ID")
    val id: String,
    @SerializedName("Active")
    val active: Int,
    @SerializedName("City")
    val city: String,
    @SerializedName("CityCode")
    val cityCode: String,
    @SerializedName("Confirmed")
    val confirmed: Int,
    @SerializedName("Deaths")
    val deaths: Int,
    @SerializedName("Recovered")
    val recovered: Int,
    @SerializedName("Country")
    val country: String,
    @SerializedName("CountryCode")
    val countryCode: String,
    @SerializedName("Date")
    val date: String,
    @SerializedName("Lat")
    val lat: String,
    @SerializedName("Lon")
    val lon: String,
    @SerializedName("Province")
    val province: String
)
