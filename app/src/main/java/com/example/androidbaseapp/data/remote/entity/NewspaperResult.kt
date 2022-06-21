package com.example.androidbaseapp.data.remote.entity

import com.google.gson.annotations.SerializedName

data class NewspaperResult(
    @SerializedName("articles") val articleResults: List<ArticleResult>,
    @SerializedName("status") val status: String,
    @SerializedName("totalResults") val totalResults: Int
)

data class ArticleResult(
    @SerializedName("author") val author: String,
    @SerializedName("content") val content: String,
    @SerializedName("description") val description: String,
    @SerializedName("publishedAt") val publishedAt: String,
    @SerializedName("title") val title: String,
    @SerializedName("url") val url: String,
    @SerializedName("urlToImage") val urlToImage: String
)