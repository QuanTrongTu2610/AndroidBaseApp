package com.example.androidbaseapp.data.remotemediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.androidbaseapp.data.exceptions.NetworkConnectionException
import com.example.androidbaseapp.data.local.LocalDatabase
import com.example.androidbaseapp.data.local.entity.DetailCountryEntity
import com.example.androidbaseapp.data.remote.CovidDynamicApiService
import com.example.androidbaseapp.data.remote.type.QueryStatus
import com.example.androidbaseapp.data.remotemediator.PagerConfig.DEFAULT_PAGE_SIZE
import com.example.androidbaseapp.domain.model.LoadingKeyModel
import com.example.androidbaseapp.domain.repositories.CovidDataRepository
import com.example.androidbaseapp.utils.Logger
import com.example.androidbaseapp.utils.ResultWrapper
import com.example.androidbaseapp.utils.TimeUtils.convertDateToAdequateFormat
import com.example.androidbaseapp.utils.converter.toDetailCountryEntity
import com.example.androidbaseapp.utils.converter.toDetailCountryModel
import java.lang.Exception

@OptIn(ExperimentalPagingApi::class)
class CovidDataMediator(
    private val localDatabase: LocalDatabase,
    private val apiServiceCovid: CovidDynamicApiService,
    private val covidDataRepository: CovidDataRepository,
    private val query: String,
) : RemoteMediator<Int, DetailCountryEntity>() {

    companion object {
        private const val DEFAULT_PAGE_INDEX = 1
    }

    override suspend fun initialize(): InitializeAction {
        // load refresh action without waiting trigger refresh
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, DetailCountryEntity>
    ): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH -> {
                val loadingKey = getLoadingKeyForClosestCurrentPosition(state)
                Logger.d("loadType: Refresh - loadingKey: $loadingKey")
                loadingKey?.nextKey?.minus(1) ?: DEFAULT_PAGE_INDEX
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
            val apiResponse = getRemoteLiveCountryFromDateByPage(
                pageNumberOfRecord = DEFAULT_PAGE_SIZE,
                page = page,
                queryStatus = QueryStatus.ACTIVE,
                date = convertDateToAdequateFormat(query)
            )
            Logger.d("apiResponse : $apiResponse")
            var apiResponseResult: List<DetailCountryEntity>? = null
            val endOfPaginationReached = when (apiResponse) {
                is ResultWrapper.Success -> {
                    apiResponseResult = apiResponse.data
                    Logger.d("apiResponse Size : ${apiResponse.data.size}")
                    apiResponseResult.isEmpty()
                }
                is ResultWrapper.Error -> true
            }
            localDatabase.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    covidDataRepository.clearLocalLoadingKeys()
                    covidDataRepository.clearLocalDetailCountries()
                }
                val prevKey = if (page == DEFAULT_PAGE_INDEX) null else page - 1
                val nextKey = if (endOfPaginationReached) null else page + 1

                covidDataRepository.insertLocalDetailCountries(
                    apiResponseResult?.map { it.toDetailCountryModel() } ?: emptyList()
                )
                covidDataRepository.insertLocalLoadingKey(
                    LoadingKeyModel(
                        loadingKeyId = apiResponseResult?.lastOrNull()?.countryId ?: 0,
                        prevKey = prevKey,
                        nextKey = nextKey
                    )
                )
            }
            MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (exception: Exception) {
            MediatorResult.Error(exception)
        }
    }

    private suspend fun getLoadingKeyForLastItem(
        state: PagingState<Int, DetailCountryEntity>
    ): LoadingKeyModel? = state.pages.lastOrNull { it.data.isNotEmpty() }
        ?.data
        ?.lastOrNull()
        ?.let { data ->
            val loadingKey = covidDataRepository.getLocalLoadingKeyPage(data.countryId)
            if (loadingKey is ResultWrapper.Success) {
                loadingKey.data
            } else null
        }

    private suspend fun getLoadingKeyForFirstItem(
        state: PagingState<Int, DetailCountryEntity>
    ): LoadingKeyModel? = state.pages.firstOrNull { it.data.isNotEmpty() }
        ?.data
        ?.firstOrNull()
        ?.let { data ->
            val loadingKey = covidDataRepository.getLocalLoadingKeyPage(data.countryId)
            if (loadingKey is ResultWrapper.Success) {
                loadingKey.data
            } else null
        }

    private suspend fun getLoadingKeyForClosestCurrentPosition(
        state: PagingState<Int, DetailCountryEntity>
    ): LoadingKeyModel? = state.anchorPosition?.let {
        state.closestItemToPosition(it)
            ?.let { data ->
                val loadingKey = covidDataRepository.getLocalLoadingKeyPage(data.countryId)
                if (loadingKey is ResultWrapper.Success) {
                    loadingKey.data
                } else null
            }
    }

    /**
     * This function separates page by basic country in database
     * */
    private suspend fun getRemoteLiveCountryFromDateByPage(
        pageNumberOfRecord: Int,
        page: Int,
        queryStatus: QueryStatus,
        date: String
    ): ResultWrapper<List<DetailCountryEntity>> {
        // get list basic county in database
        val countryNameList = covidDataRepository.getLocalBasicCountries(
            startId = page * pageNumberOfRecord - pageNumberOfRecord,
            endId = page * pageNumberOfRecord
        )
        Logger.d("countryNameList: $countryNameList")
        val response: MutableList<DetailCountryEntity?> = mutableListOf()
        if (countryNameList is ResultWrapper.Success) {
            countryNameList.data.forEach {
                var fetchSuccess = false
                while (!fetchSuccess) {
                    try {
                        val apiResponse = apiServiceCovid.getLiveCountryByStatusFromDate(
                            countryName = it.shortName,
                            status = queryStatus.label,
                            date = date
                        )
                        response.add(
                            apiResponse.firstOrNull()?.toDetailCountryEntity(countryId = it.id)
                        )
                        fetchSuccess = true
                    } catch (e: Exception) {
                        when (e) {
                            is NetworkConnectionException.TooManyRequestException -> {
                                apiServiceCovid.recreateService()
                                fetchSuccess = false
                            }
                            else -> {
                                e.printStackTrace()
                            }
                        }
                    }
                }
            }
        }
        return ResultWrapper.Success(response.filterNotNull())
    }
}

object PagerConfig {
    const val DEFAULT_PAGE_SIZE = 20
}