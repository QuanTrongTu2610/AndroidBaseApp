package com.example.androidbaseapp.data.repositories

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.example.androidbaseapp.NetworkConfig
import com.example.androidbaseapp.data.local.LocalDatabase
import com.example.androidbaseapp.data.local.dao.BasicCountryDao
import com.example.androidbaseapp.data.local.dao.DetailCountryDao
import com.example.androidbaseapp.data.local.dao.LoadingKeyDao
import com.example.androidbaseapp.data.remote.CovidDynamicApiService
import com.example.androidbaseapp.data.remote.entity.CovidDetailCountryResult
import com.example.androidbaseapp.data.remotemediator.CovidDataMediator
import com.example.androidbaseapp.data.remotemediator.PagerConfig.DEFAULT_PAGE_SIZE
import com.example.androidbaseapp.domain.exceptions.DatabaseException
import com.example.androidbaseapp.domain.model.BasicCountryModel
import com.example.androidbaseapp.domain.model.DetailCountryModel
import com.example.androidbaseapp.domain.model.LoadingKeyModel
import com.example.androidbaseapp.domain.model.WorldWipModel
import com.example.androidbaseapp.domain.repositories.CovidDataRepository
import com.example.androidbaseapp.utils.Logger
import com.example.androidbaseapp.utils.ResultWrapper
import com.example.androidbaseapp.utils.converter.toBasicCountryModel
import com.example.androidbaseapp.utils.converter.toDetailCountryModel
import com.example.androidbaseapp.utils.converter.toLoadingKeyEntity
import com.example.androidbaseapp.utils.converter.toLoadingKeyModel
import com.example.androidbaseapp.utils.converter.toLocalBasicCountryEntity
import com.example.androidbaseapp.utils.converter.toLocalDetailCountryEntity
import com.example.androidbaseapp.utils.converter.toWorldWipModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.lang.Exception
import javax.inject.Inject

class CovidDataRepositoryImpl @Inject constructor(
    private val apiServiceCovid: CovidDynamicApiService,
    private val basicCountryDao: BasicCountryDao,
    private val detailCountryDao: DetailCountryDao,
    private val loadingKeyDao: LoadingKeyDao,
    private val localDatabase: LocalDatabase,
) : CovidDataRepository {

    init {
        apiServiceCovid.updateApiDomain(NetworkConfig.COVID_API_DOMAIN)
    }

    override suspend fun getRemoteWorldWip(
        startDate: String,
        endDate: String
    ): ResultWrapper<List<WorldWipModel>> {
        return try {
            val apiResponse = apiServiceCovid.getWorkWip(startDate = startDate, endDate = endDate)
            return ResultWrapper.Success(apiResponse.map { it.toWorldWipModel() })
        } catch (e: Exception) {
            ResultWrapper.Error(e)
        }
    }

    override suspend fun getRemoteBasicCountries(): ResultWrapper<List<BasicCountryModel>> {
        return try {
            val apiResponse = apiServiceCovid.getCountries()
            ResultWrapper.Success(apiResponse.map { it.toBasicCountryModel() })
        } catch (e: Exception) {
            ResultWrapper.Error(e)
        }
    }

    override suspend fun getRemoteDetailCountries(date: String): ResultWrapper<Flow<PagingData<DetailCountryModel>>> {
        Logger.d("date query: $date")
        val pagerConfig = PagingConfig(
            pageSize = basicCountryDao.countBasicCountries() / DEFAULT_PAGE_SIZE,
            enablePlaceholders = false
        )
        val remoteMediator = CovidDataMediator(
            apiServiceCovid = apiServiceCovid,
            localDatabase = localDatabase,
            covidDataRepository = this,
            query = date,
        )
        val pagingSource = { detailCountryDao.getPagingSourceDetailCountries() }

        @OptIn(ExperimentalPagingApi::class)
        return ResultWrapper.Success(
            Pager(
                config = pagerConfig,
                remoteMediator = remoteMediator,
                pagingSourceFactory = pagingSource
            ).flow.map { detailCountryEntity -> detailCountryEntity.map { it.toDetailCountryModel() } }
        )
    }

    /*--local--*/
    override suspend fun getLocalBasicCountries(): ResultWrapper<List<BasicCountryModel>> {
        return try {
            basicCountryDao
                .getBasicCountries()
                .map { it.toBasicCountryModel() }
                .let { ResultWrapper.Success(it) }
        } catch (e: Exception) {
            ResultWrapper.Error(e)
        }
    }

    override suspend fun getLocalBasicCountries(
        startId: Int,
        endId: Int
    ): ResultWrapper<List<BasicCountryModel>> {
        return try {
            basicCountryDao.getBasicCountries(startId = startId, endId = endId)
                .map { it.toBasicCountryModel() }
                .let { ResultWrapper.Success(it) }
        } catch (e: Exception) {
            ResultWrapper.Error(e)
        }
    }


    override suspend fun clearLocalBasicCountries() {
        basicCountryDao.clearBasicCountries()
    }

    override suspend fun insertLocalBasicCountries(data: List<BasicCountryModel>) {
        // do not to save duplicate
        if (data.isNotEmpty()) {
            basicCountryDao.clearBasicCountries()
            data.forEachIndexed { index, basicCountryModel ->
                basicCountryDao.insertBasicCountry(
                    basicCountryModel.copy(id = index).toLocalBasicCountryEntity()
                )
            }
        }
    }

    override suspend fun clearLocalDetailCountries() {
        detailCountryDao.clearDetailCountries()
    }

    override suspend fun insertLocalDetailCountries(data: List<DetailCountryModel>) {
        detailCountryDao.insertDetailCountries(data.map { it.toLocalDetailCountryEntity() })
    }

    override suspend fun countNumberOfBasicCountry(): Int {
        return basicCountryDao.countBasicCountries()
    }

    override suspend fun getLocalLoadingKeyPage(dataId: Int): ResultWrapper<LoadingKeyModel> {
        return try {
            loadingKeyDao.getLoadingKeyById(dataId = dataId)?.let {
                ResultWrapper.Success(it.toLoadingKeyModel())
            } ?: run {
                ResultWrapper.Error(DatabaseException.NotFoundOrNullEntity("Loading key is null"))
            }
        } catch (e: Exception) {
            ResultWrapper.Error(e)
        }
    }

    override suspend fun insertLocalLoadingKey(data: LoadingKeyModel) {
        loadingKeyDao.insertLoadingKey(data.toLoadingKeyEntity())
    }

    override suspend fun clearLocalLoadingKeys() {
        loadingKeyDao.clearLoadingKeys()
    }
}