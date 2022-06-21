package com.example.androidbaseapp.domain.interactor

import com.example.androidbaseapp.domain.SingleUseCase
import com.example.androidbaseapp.domain.interactor.types.DailyWorldData
import com.example.androidbaseapp.domain.interactor.types.WorldWipQueryStatus
import com.example.androidbaseapp.data.repositories.model.WorldWipModel
import com.example.androidbaseapp.domain.repositories.CovidDataRepository
import com.example.androidbaseapp.common.ResultWrapper
import com.example.androidbaseapp.common.TimeUtils
import com.example.androidbaseapp.common.TimeUtils.convertFromApiDateTimeToDefaultDateTime
import javax.inject.Inject

class GetRemoteTotalWorldWipUseCase @Inject constructor(
    private val covidDataRepository: CovidDataRepository
) : SingleUseCase<ResultWrapper<List<DailyWorldData>>> {
    override suspend fun execute(): ResultWrapper<List<DailyWorldData>> {
        return covidDataRepository.getRemoteWorldWip(
            startDate = TimeUtils.getSpecialCurrentTime(minusDay = 7),
            endDate = TimeUtils.getSpecialCurrentTime(minusDay = 1)
        ).let {
            when (it) {
                is ResultWrapper.Success -> {
                    ResultWrapper.Success(buildData(it.data))
                }
                is ResultWrapper.Error -> ResultWrapper.Error(it.exception)
            }
        }
    }

    private fun buildData(listRawData: List<WorldWipModel>): List<DailyWorldData> {
        val listDailyWorldData = mutableListOf<DailyWorldData>()
        listRawData.forEach { record ->
            WorldWipQueryStatus.values().forEach { status ->
                listDailyWorldData.add(
                    DailyWorldData(
                        label = convertFromApiDateTimeToDefaultDateTime(record.date),
                        status = status,
                        value = getStatusQuery(status = status, input = record)
                    )
                )
            }
        }
        return listDailyWorldData
    }

    private fun getStatusQuery(status: WorldWipQueryStatus, input: WorldWipModel): Float {
        return when (status) {
            WorldWipQueryStatus.NEW_CONFIRMED -> input.newConfirmed
            WorldWipQueryStatus.NEW_DEATHS -> input.newDeaths
            WorldWipQueryStatus.NEW_RECOVERED -> input.newRecovered
            WorldWipQueryStatus.TOTAL_CONFIRMED -> input.totalConfirmed
            WorldWipQueryStatus.TOTAL_DEATHS -> input.totalRecovered
            else -> 0F
        }.toFloat()
    }
}