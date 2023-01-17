package com.example.androidbaseapp.data.repositories

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.example.androidbaseapp.NetworkConfig
import com.example.androidbaseapp.data.local.dao.LoadingKeyDao
import com.example.androidbaseapp.data.remote.NewspaperDynamicApiService
import com.example.androidbaseapp.data.repositories.model.LoadingKeyModel
import com.example.androidbaseapp.domain.repositories.NewspaperRepository
import com.example.androidbaseapp.common.Logger
import com.example.androidbaseapp.common.ResultWrapper
import com.example.androidbaseapp.common.converter.toArticleEntity
import com.example.androidbaseapp.common.converter.toArticleModel
import com.example.androidbaseapp.common.converter.toLoadingKeyEntity
import com.example.androidbaseapp.common.converter.toLoadingKeyModel
import com.example.androidbaseapp.data.local.LocalDatabase
import com.example.androidbaseapp.data.local.dao.ArticleDao
import com.example.androidbaseapp.data.repositories.mediator.ArticleMediator
import com.example.androidbaseapp.data.repositories.mediator.PagerConfig.DEFAULT_ARTICLE_PAGE_SIZE
import com.example.androidbaseapp.data.repositories.model.ArticleModel
import com.example.androidbaseapp.common.exceptions.DatabaseException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class NewspaperRepositoryImpl @Inject constructor(
    private val newspaperDynamicApiService: NewspaperDynamicApiService,
    private val articleDao: ArticleDao,
    private val loadingKeyDao: LoadingKeyDao,
    private val localDatabase: LocalDatabase
) : NewspaperRepository {

    init {
        newspaperDynamicApiService.updateApiDomain(NetworkConfig.NEWSPAPER_API_DOMAIN)
    }

    @OptIn(ExperimentalPagingApi::class)
    override suspend fun getRemoteArticles(
        keyWord: String,
        date: String
    ): ResultWrapper<Flow<PagingData<ArticleModel>>> {
        val config = PagingConfig(
            pageSize = DEFAULT_ARTICLE_PAGE_SIZE,
            enablePlaceholders = false
        )
        val articleRemoteMediator = ArticleMediator(
            date = date,
            query = keyWord,
            localDatabase = localDatabase,
            newspaperRepository = this,
            newspaperDynamicApiService = newspaperDynamicApiService
        )
        return ResultWrapper.Success(
            Pager(
                remoteMediator = articleRemoteMediator,
                config = config,
                pagingSourceFactory = { articleDao.getPagingSourceArticles() }
            ).flow.map { articleEntity -> articleEntity.map { it.toArticleModel() } }
        )
    }

    override suspend fun insertLocalArticles(data: List<ArticleModel>) {
        articleDao.insertArticles(data.map { it.toArticleEntity() })
    }

    override suspend fun clearLocalArticles() {
        articleDao.clearArticles()
    }

    override suspend fun getLocalLoadingKeyPage(
        dataId: Int,
        keyType: Int
    ): ResultWrapper<LoadingKeyModel> {
        return try {
            loadingKeyDao.getLoadingKeyById(dataId = dataId, keyType)?.let {
                ResultWrapper.Success(it.toLoadingKeyModel())
            } ?: run {
                ResultWrapper.Error(DatabaseException.NotFoundOrNullEntity("Loading key is null"))
            }
        } catch (e: java.lang.Exception) {
            Logger.e(e)
            ResultWrapper.Error(e)
        }
    }

    override suspend fun insertLocalLoadingKey(data: LoadingKeyModel, keyType: Int) {
        loadingKeyDao.insertLoadingKey(data.toLoadingKeyEntity().copy(keyType = keyType))
    }

    override suspend fun clearLocalLoadingKeys(keyType: Int) {
        loadingKeyDao.clearLoadingKeys(keyType)
    }
}