package com.example.androidbaseapp.data.repositories.mediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.androidbaseapp.data.local.LocalDatabase
import com.example.androidbaseapp.data.local.entity.ArticleEntity
import com.example.androidbaseapp.data.remote.NewspaperDynamicApiService
import com.example.androidbaseapp.domain.repositories.NewspaperRepository
import com.example.androidbaseapp.common.Logger
import com.example.androidbaseapp.common.ResultWrapper
import com.example.androidbaseapp.common.converter.toArticleEntity
import com.example.androidbaseapp.common.converter.toArticleModel
import com.example.androidbaseapp.data.repositories.model.LoadingKeyModel
import java.lang.Exception

@OptIn(ExperimentalPagingApi::class)
class ArticleMediator(
    private val date: String,
    private val query: String,
    private val localDatabase: LocalDatabase,
    private val newspaperRepository: NewspaperRepository,
    private val newspaperDynamicApiService: NewspaperDynamicApiService
) : RemoteMediator<Int, ArticleEntity>() {

    companion object {
        private const val START_DEFAULT_PAGE_INDEX = 1
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, ArticleEntity>
    ): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH -> {
                val loadingKey = getLoadingKeyForClosestCurrentPosition(state)
                Logger.d("loadType: Refresh - loadingKey: $loadingKey")
                loadingKey?.nextKey?.minus(1) ?: START_DEFAULT_PAGE_INDEX
            }
            LoadType.PREPEND -> {
                val loadingKey = getLoadingKeyForFirstItem(state)
                Logger.d("loadType: Prepend - loadingKey: $loadingKey")
                loadingKey?.prevKey ?: run {
                    return MediatorResult.Success(endOfPaginationReached = false)
                }
            }
            LoadType.APPEND -> {
                val loadingKey = getLoadingKeyForLastItem(state)
                Logger.d("loadType: Append - loadingKey: $loadingKey")
                loadingKey?.nextKey ?: run {
                    return MediatorResult.Success(endOfPaginationReached = false)
                }
            }
        }
        return try {
            // query by page
            val apiResponse = newspaperDynamicApiService.getNewspaperByKeyWord(
                keyWord = query,
                page = page.toString(),
                pageSize = state.config.pageSize.toString(),
                date = date
            )
            Logger.d("apiResponse : $apiResponse")
            var apiResponseResult: List<ArticleEntity>? = null
            val endOfPaginationReached = run {
                apiResponseResult = apiResponse.articleResults.map {
                    it.toArticleEntity().copy(pageId = page)
                }
                Logger.d("apiResponse Size : ${apiResponseResult?.size}")
                apiResponse.articleResults.isEmpty()
            }
            localDatabase.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    newspaperRepository.clearLocalLoadingKeys()
                    newspaperRepository.clearLocalArticles()
                }
                val prevKey = if (page == START_DEFAULT_PAGE_INDEX) null else page - 1
                val nextKey = if (endOfPaginationReached) null else page + 1

                newspaperRepository.insertLocalArticles(
                    apiResponseResult?.map { it.toArticleModel() } ?: emptyList()
                )
                newspaperRepository.insertLocalLoadingKey(
                    LoadingKeyModel(
                        loadingKeyId = apiResponseResult?.lastOrNull()?.pageId ?: 0,
                        prevKey = prevKey,
                        nextKey = nextKey
                    )
                )
            }
            MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (exception: Exception) {
            Logger.e(exception)
            MediatorResult.Error(exception)
        }
    }

    private suspend fun getLoadingKeyForClosestCurrentPosition(
        state: PagingState<Int, ArticleEntity>
    ): LoadingKeyModel? = state.anchorPosition?.let {
        state.closestItemToPosition(it)
            ?.let { data ->
                val loadingKey = newspaperRepository.getLocalLoadingKeyPage(data.pageId)
                if (loadingKey is ResultWrapper.Success) {
                    loadingKey.data
                } else null
            }
    }


    private suspend fun getLoadingKeyForFirstItem(
        state: PagingState<Int, ArticleEntity>
    ): LoadingKeyModel? = state.pages.firstOrNull { it.data.isNotEmpty() }
        ?.data
        ?.firstOrNull()
        ?.let { data ->
            val loadingKey = newspaperRepository.getLocalLoadingKeyPage(data.pageId)
            if (loadingKey is ResultWrapper.Success) {
                loadingKey.data
            } else null
        }

    private suspend fun getLoadingKeyForLastItem(
        state: PagingState<Int, ArticleEntity>
    ): LoadingKeyModel? = state.pages.lastOrNull { it.data.isNotEmpty() }
        ?.data
        ?.lastOrNull()
        ?.let { data ->
            val loadingKey = newspaperRepository.getLocalLoadingKeyPage(data.pageId)
            if (loadingKey is ResultWrapper.Success) {
                loadingKey.data
            } else null
        }
}
