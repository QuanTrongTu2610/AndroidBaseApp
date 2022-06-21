package com.example.androidbaseapp.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = ArticleEntity.ARTICLE_TABLE_NAME)
data class ArticleEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = ARTICLE_COLUMN_ID)
    val id: Int = 0,
    @ColumnInfo(name = ARTICLE_COLUMN_AUTHOR)
    val author: String? = null,
    @ColumnInfo(name = ARTICLE_COLUMN_CONTENT)
    val content: String? = null,
    @ColumnInfo(name = ARTICLE_COLUMN_PUBLISH_DATE)
    val publishedAt: String? = null,
    @ColumnInfo(name = ARTICLE_COLUMN_TITLE)
    val title: String? = null,
    @ColumnInfo(name = ARTICLE_COLUMN_URL)
    val url: String? = null,
    @ColumnInfo(name = ARTICLE_COLUMN_URL_IMAGE)
    val urlToImage: String? = null,
    @ColumnInfo(name = ARTICLE_PAGE_ID)
    val pageId: Int = 1
) {
    companion object {
        const val ARTICLE_TABLE_NAME = "article"
        const val ARTICLE_COLUMN_ID = "id"
        const val ARTICLE_COLUMN_AUTHOR = "author"
        const val ARTICLE_COLUMN_CONTENT = "content"
        const val ARTICLE_COLUMN_PUBLISH_DATE = "publishedAt"
        const val ARTICLE_COLUMN_TITLE = "title"
        const val ARTICLE_COLUMN_URL = "url"
        const val ARTICLE_COLUMN_URL_IMAGE = "urlToImage"
        const val ARTICLE_PAGE_ID = "pageId"
    }
}