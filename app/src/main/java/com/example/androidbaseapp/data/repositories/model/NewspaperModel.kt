package com.example.androidbaseapp.data.repositories.model

data class NewspaperModel(
    val articles: List<ArticleModel>? = null,
    val status: String? = null,
    val totalResults: Int? = null
)

data class ArticleModel(
    val author: String? = null,
    val content: String? = null,
    val publishedAt: String? = null,
    val title: String? = null,
    val url: String? = null,
    val urlToImage: String? = null,
    val pageId:Int = 0
)
