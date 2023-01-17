package com.example.androidbaseapp.domain.interactor

import androidx.paging.PagingData
import com.example.androidbaseapp.domain.SingleUseCaseWithParameter
import com.example.androidbaseapp.domain.repositories.NewspaperRepository
import com.example.androidbaseapp.common.ResultWrapper
import com.example.androidbaseapp.common.TimeUtils
import com.example.androidbaseapp.common.exceptions.QueryException
import com.example.androidbaseapp.data.repositories.model.ArticleModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetNewspaperUseCase @Inject constructor(
    private val newspaperRepository: NewspaperRepository
) : SingleUseCaseWithParameter<String, ResultWrapper<Flow<PagingData<ArticleModel>>>> {
    override suspend fun execute(parameter: String?): ResultWrapper<Flow<PagingData<ArticleModel>>> {
        val date = TimeUtils.convertFromApiDateTimeToDefaultDateTime(
            TimeUtils.getSpecialCurrentTime(1)
        )
        return parameter?.let{
            newspaperRepository.getRemoteArticles(keyWord = parameter, date = date)
        } ?: ResultWrapper.Error(QueryException.UndefinedQueryException("undefined query param"))
    }
}