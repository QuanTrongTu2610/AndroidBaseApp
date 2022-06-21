package com.example.androidbaseapp.presentation

data class UiState(
    val query: String = "",
    val lastQueryScrolled: String = "",
    val hasNotScrolledForCurrentSearch: Boolean = false
)

sealed class UiAction {
    data class Search(val query: String) : UiAction()
    data class Scroll(val currentQuery: String) : UiAction()
}

object QueryHandler {
    const val ARTICLE_DATA_LAST_QUERY_SCROLLED: String = "article_data_last_query_scrolled"
    const val ARTICLE_DATA_LAST_SEARCH_QUERY: String = "article_data_last_search_query"
    const val ARTICLE_QUERY_KEYWORD: String = "covid"
}
