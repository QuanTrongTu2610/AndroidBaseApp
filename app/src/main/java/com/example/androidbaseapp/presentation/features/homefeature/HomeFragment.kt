package com.example.androidbaseapp.presentation.features.homefeature

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.PagingData
import androidx.recyclerview.widget.RecyclerView
import com.example.androidbaseapp.R
import com.example.androidbaseapp.domain.interactor.types.DailyWorldData
import com.example.androidbaseapp.domain.model.DetailCountryModel
import com.example.androidbaseapp.presentation.RemotePresentationState
import com.example.androidbaseapp.presentation.adapter.LiveCountryByDayAdapter
import com.example.androidbaseapp.presentation.asRemotePresentationState
import com.example.androidbaseapp.presentation.base.BaseFragment
import com.example.androidbaseapp.presentation.customview.ITabBarClickedHandler
import com.example.androidbaseapp.utils.Logger
import com.example.androidbaseapp.utils.toColumnData
import com.example.androidbaseapp.utils.toTabLabelData
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.home_custom_view_bar_chart.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class HomeFragment : BaseFragment() {

    private val homeFragmentViewModel: HomeFragmentViewModel by viewModels()

    override fun getLayoutId() = R.layout.fragment_home

    override fun initData(data: Bundle?) {}

    override fun initViews() {}

    override fun initActions() {}

    override fun initObservers() {
        homeFragmentViewModel.store.observe(
            owner = this,
            selector = object : (HomeFragmentState) -> HomeFragmentState {
                override fun invoke(p1: HomeFragmentState): HomeFragmentState = p1
            },
            observer = {
                if (it.dataWorldWip.isNotEmpty()) {
                    loadDataToCustomBarChart(it.dataWorldWip)
                }
            }
        )

        bindState(
            uiState = homeFragmentViewModel.state,
            pagingData = homeFragmentViewModel.pagingDataFlow,
            uiActions = homeFragmentViewModel.accept
        )
    }

    private fun bindState(
        uiState: StateFlow<UiState>?,
        pagingData: Flow<PagingData<DetailCountryModel>>?,
        uiActions: ((UiAction) -> Unit)?
    ) {
        bindSearch()
        bindRcvList(uiState = uiState, uiActions = uiActions, pagingData = pagingData)
    }

    private fun bindSearch() {
    }

    private fun bindRcvList(
        uiState: StateFlow<UiState>?,
        uiActions: ((UiAction) -> Unit)?,
        pagingData: Flow<PagingData<DetailCountryModel>>?
    ) {
        val adapter = LiveCountryByDayAdapter()
        rcvDetailCountryList.adapter = adapter
        rcvDetailCountryList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy != 0 && uiActions != null) {
                    uiState?.value?.query?.let {
                        uiActions(UiAction.Scroll(currentQuery = it))
                    }
                }
            }
        })

        val notLoading = adapter.loadStateFlow
            .asRemotePresentationState()
            .map { it == RemotePresentationState.PRESENTED }

        val hasNotScrolledForCurrentSearch = uiState
            ?.map { it.hasNotScrolledForCurrentSearch }
            ?.distinctUntilChanged()

        lifecycleScope.launch {
            pagingData?.collectLatest(adapter::submitData)
        }
    }


    private fun loadDataToCustomBarChart(dayModels: List<DailyWorldData>) {
        val tabOptionData = dayModels.toTabLabelData()
        val columnData = dayModels.toColumnData()
        containerBarChart.setData(columnData)
        tabOptions.setData(tabOptionData)
        tabOptions.setCallBack(object : ITabBarClickedHandler {
            override fun onLabelClick(tabItemPos: Int, value: String) {
                containerBarChart.updateColumn(tabItemPos)
            }
        })
    }
}
