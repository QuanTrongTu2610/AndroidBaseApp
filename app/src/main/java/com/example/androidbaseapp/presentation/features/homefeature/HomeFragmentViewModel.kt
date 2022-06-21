package com.example.androidbaseapp.presentation.features.homefeature

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.androidbaseapp.domain.interactor.GetNewspaperUseCase
import com.example.androidbaseapp.domain.interactor.GetRemoteTotalWorldWipUseCase
import com.example.androidbaseapp.presentation.BaseViewModel
import com.example.androidbaseapp.common.Logger
import com.example.androidbaseapp.common.ResultWrapper
import com.example.androidbaseapp.data.repositories.model.ArticleModel
import com.example.androidbaseapp.data.repositories.model.DetailCountryModel
import com.example.androidbaseapp.presentation.QueryHandler
import com.example.androidbaseapp.presentation.UiAction
import com.example.androidbaseapp.presentation.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.distinctUntilChangedBy
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
    private val getNewspaperUseCase: GetNewspaperUseCase
) : BaseViewModel<HomeFragmentState>() {
    init {
        loadWorldData()
        loadNewspaper()
    }

    var state: StateFlow<UiState>? = null
    var accept: ((UiAction) -> Unit)? = null
    var pagingDataFlow: Flow<PagingData<ArticleModel>>? = null

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
    private fun loadNewspaper() {
        Logger.d("start load article")
        val actionStateFlow = MutableSharedFlow<UiAction>()
        val initialQuery: String = savedStateHandle.get(QueryHandler.ARTICLE_DATA_LAST_SEARCH_QUERY)
            ?: QueryHandler.ARTICLE_QUERY_KEYWORD
        val lastQueryScrolled = savedStateHandle.get(QueryHandler.ARTICLE_DATA_LAST_QUERY_SCROLLED)
            ?: QueryHandler.ARTICLE_QUERY_KEYWORD
        val search = actionStateFlow
            .filterIsInstance<UiAction.Search>()
            .distinctUntilChanged()
            .onStart {
                Logger.d("emit search query: $initialQuery")
                emit(UiAction.Search(query = initialQuery))
            }
        val scroll = actionStateFlow
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

        // action
        state = combine(search, scroll, ::Pair).map { (search, scroll) ->
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

        pagingDataFlow = search.flatMapLatest {
            Logger.d("execute loading: $it")
            getNewspaperUseCase.execute(it.query).let { queryResult ->
                when (queryResult) {
                    is ResultWrapper.Success -> queryResult.data
                    else -> flowOf()
                }
            }
        }.cachedIn(viewModelScope)

        // ui action emit value
        accept = { action ->
            viewModelScope.launch {
                Logger.d("ui action emit value: $action")
                actionStateFlow.emit(action)
            }
        }
    }

    override fun onCleared() {
        Logger.d("onCleared")
        super.onCleared()
    }
}