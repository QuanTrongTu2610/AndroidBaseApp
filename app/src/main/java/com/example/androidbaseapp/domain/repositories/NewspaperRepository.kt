package com.example.androidbaseapp.domain.repositories

import androidx.paging.PagingData
import com.example.androidbaseapp.data.repositories.model.LoadingKeyModel
import com.example.androidbaseapp.common.ResultWrapper
import com.example.androidbaseapp.common.types.KeyType
import com.example.androidbaseapp.data.repositories.model.ArticleModel
import kotlinx.coroutines.flow.Flow

interface NewspaperRepository {
    suspend fun getRemoteArticles(
        keyWord: String,
        date: String
    ): ResultWrapper<Flow<PagingData<ArticleModel>>>

    suspend fun insertLocalArticles(data: List<ArticleModel>)

    suspend fun clearLocalArticles()

    suspend fun getLocalLoadingKeyPage(
        dataId: Int,
        keyType: Int = KeyType.KEY_ARTICLE_LOADING_TYPE.value
    ): ResultWrapper<LoadingKeyModel>

    suspend fun insertLocalLoadingKey(
        data: LoadingKeyModel,
        keyType: Int = KeyType.KEY_ARTICLE_LOADING_TYPE.value
    )

    suspend fun clearLocalLoadingKeys(keyType: Int = KeyType.KEY_ARTICLE_LOADING_TYPE.value)
}