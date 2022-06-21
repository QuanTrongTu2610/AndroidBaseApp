package com.example.androidbaseapp.data.repositories.model

data class LoadingKeyModel(
    val loadingKeyId: Int = 0,
    val prevKey: Int? = 0,
    val nextKey: Int? = 0,
    val keyType: Int = 0
)