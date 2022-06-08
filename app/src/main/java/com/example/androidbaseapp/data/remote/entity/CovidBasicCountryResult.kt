package com.example.androidbaseapp.data.remote.entity


import com.google.gson.annotations.SerializedName

data class CovidBasicCountryResult(
    @SerializedName("Country") val country: String,
    @SerializedName("ISO2") val iSO2: String,
    @SerializedName("Slug") val slug: String
)