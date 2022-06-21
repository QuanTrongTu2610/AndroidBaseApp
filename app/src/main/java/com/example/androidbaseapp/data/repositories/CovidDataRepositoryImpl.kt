package com.example.androidbaseapp.data.repositories

import com.example.androidbaseapp.NetworkConfig
import com.example.androidbaseapp.data.local.dao.BasicCountryDao
import com.example.androidbaseapp.data.local.dao.DetailCountryDao
import com.example.androidbaseapp.data.local.dao.LoadingKeyDao
import com.example.androidbaseapp.data.remote.CovidDynamicApiService
import com.example.androidbaseapp.data.repositories.model.BasicCountryModel
import com.example.androidbaseapp.data.repositories.model.DetailCountryModel
import com.example.androidbaseapp.data.repositories.model.LoadingKeyModel
import com.example.androidbaseapp.data.repositories.model.WorldWipModel
import com.example.androidbaseapp.common.exceptions.DatabaseException
import com.example.androidbaseapp.domain.repositories.CovidDataRepository
import com.example.androidbaseapp.common.Logger
import com.example.androidbaseapp.common.ResultWrapper
import com.example.androidbaseapp.common.converter.toBasicCountryModel
import com.example.androidbaseapp.common.converter.toLoadingKeyEntity
import com.example.androidbaseapp.common.converter.toLoadingKeyModel
import com.example.androidbaseapp.common.converter.toLocalBasicCountryEntity
import com.example.androidbaseapp.common.converter.toLocalDetailCountryEntity
import com.example.androidbaseapp.common.converter.toWorldWipModel
import java.lang.Exception
import javax.inject.Inject

class CovidDataRepositoryImpl @Inject constructor(
    private val apiServiceCovid: CovidDynamicApiService,
    private val basicCountryDao: BasicCountryDao,
    private val detailCountryDao: DetailCountryDao,
    private val loadingKeyDao: LoadingKeyDao,
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
            Logger.d(e)
            ResultWrapper.Error(e)
        }
    }

    override suspend fun getRemoteBasicCountries(): ResultWrapper<List<BasicCountryModel>> {
        return try {
            val apiResponse = apiServiceCovid.getCountries()
            ResultWrapper.Success(apiResponse.map { it.toBasicCountryModel() })
        } catch (e: Exception) {
            Logger.e(e)
            ResultWrapper.Error(e)
        }
    }

    override suspend fun getLocalBasicCountries(): ResultWrapper<List<BasicCountryModel>> {
        return try {
            basicCountryDao
                .getBasicCountries()
                .map { it.toBasicCountryModel() }
                .let { ResultWrapper.Success(it) }
        } catch (e: Exception) {
            Logger.e(e)
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
            Logger.e(e)
            ResultWrapper.Error(e)
        }
    }


    override suspend fun clearLocalBasicCountries() {
        basicCountryDao.clearBasicCountries()
    }

    override suspend fun insertLocalBasicCountries(data: List<BasicCountryModel>) {
        if (data.isNotEmpty()) {
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

    override suspend fun getLocalLoadingKeyPage(dataId: Int, keyType: Int): ResultWrapper<LoadingKeyModel> {
        return try {
            loadingKeyDao.getLoadingKeyById(dataId = dataId, keyType)?.let {
                ResultWrapper.Success(it.toLoadingKeyModel())
            } ?: run {
                ResultWrapper.Error(DatabaseException.NotFoundOrNullEntity("Loading key is null"))
            }
        } catch (e: Exception) {
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