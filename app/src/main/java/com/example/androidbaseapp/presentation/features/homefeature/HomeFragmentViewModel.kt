package com.example.androidbaseapp.presentation.features.homefeature

import android.annotation.SuppressLint
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.androidbaseapp.domain.interactor.GetRemoteLiveCountriesByDayUseCase
import com.example.androidbaseapp.domain.interactor.GetRemoteTotalWorldWipUseCase
import com.example.androidbaseapp.domain.model.DetailCountryModel
import com.example.androidbaseapp.presentation.base.BaseViewModel
import com.example.androidbaseapp.utils.Logger
import com.example.androidbaseapp.utils.ResultWrapper
import com.example.androidbaseapp.utils.TimeUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HomeFragmentViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getRemoteTotalWorldWipUseCase: GetRemoteTotalWorldWipUseCase,
    private val getRemoteLiveCountriesByDayUseCase: GetRemoteLiveCountriesByDayUseCase
) : BaseViewModel<HomeFragmentState>() {

    var state: StateFlow<UiState>? = null
    var accept: ((UiAction) -> Unit)? = null
    var pagingDataFlow: Flow<PagingData<DetailCountryModel>>? = null

    init {
        loadWorldData()
        loadRemoteLiveCountriesData()
    }

    override fun initState(): HomeFragmentState = HomeFragmentState(dataWorldWip = listOf())

    private fun loadWorldData() {
        defaultScope.launch(Dispatchers.IO) {
            getRemoteTotalWorldWipUseCase.execute().let { result ->
                when (result) {
                    is ResultWrapper.Success -> {
                        withContext(Dispatchers.Main) {
                            dispatchState(HomeFragmentState(dataWorldWip = result.data))
                        }
                    }
                    is ResultWrapper.Error -> Logger.d("Unexpected result: ${result.exception.message}")
                }
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @SuppressLint("CheckResult")
    private fun loadRemoteLiveCountriesData() {
        Logger.d("start Load remote live countries")
        val actionStateFlow = MutableSharedFlow<UiAction>()
        val initialQuery: String = savedStateHandle.get(LAST_SEARCH_QUERY) ?: DEFAULT_QUERY
        val lastQueryScrolled = savedStateHandle.get(LAST_QUERY_SCROLLED) ?: DEFAULT_QUERY
        // for search request
        val searches = actionStateFlow
            .filterIsInstance<UiAction.Search>()
            .distinctUntilChanged()
            .onStart {
                Logger.d("emit search query: $initialQuery")
                emit(UiAction.Search(query = initialQuery))
            }
        // for scroll request
        val queriesScrolled = actionStateFlow
            .filterIsInstance<UiAction.Scroll>()
            .distinctUntilChanged()
            .shareIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
                replay = 1
            )
            .onStart {
                Logger.d("emit scroll query: $lastQueryScrolled")
                emit(UiAction.Scroll(currentQuery = lastQueryScrolled))
            }

        pagingDataFlow = searches.flatMapLatest {
            Logger.d("execute loading: $it")
            getRemoteLiveCountriesByDayUseCase.execute(it.query).let { queryResult ->
                when (queryResult) {
                    is ResultWrapper.Success -> queryResult.data
                    else -> flowOf()
                }
            }
        }.cachedIn(viewModelScope)

        // action
        state = combine(searches, queriesScrolled, ::Pair).map { (search, scroll) ->
            UiState(
                query = search.query,
                lastQueryScrolled = scroll.currentQuery,
                // If the search query matches the scroll query, the user has scrolled
                hasNotScrolledForCurrentSearch = search.query != scroll.currentQuery
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
            initialValue = UiState()
        )

        // ui action emit value
        accept = { action -> viewModelScope.launch {
            Logger.d("ui action emit value: $action")
            actionStateFlow.emit(action) }
        }
    }

    override fun onCleared() {
        Logger.d("onCleared")
        savedStateHandle[LAST_SEARCH_QUERY] = state?.value?.query
        savedStateHandle[LAST_QUERY_SCROLLED] = state?.value?.lastQueryScrolled
        super.onCleared()
    }
}

data class UiState(
    val query: String = DEFAULT_QUERY,
    val lastQueryScrolled: String = DEFAULT_QUERY,
    val hasNotScrolledForCurrentSearch: Boolean = false
)

sealed class UiAction {
    data class Search(val query: String) : UiAction()
    data class Scroll(val currentQuery: String) : UiAction()
}

private const val LAST_QUERY_SCROLLED: String = "last_query_scrolled"
private const val LAST_SEARCH_QUERY: String = "last_search_query"
private val DEFAULT_QUERY = TimeUtils.getSpecialCurrentTime(2)