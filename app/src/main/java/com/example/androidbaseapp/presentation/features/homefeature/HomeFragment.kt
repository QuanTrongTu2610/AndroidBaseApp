package com.example.androidbaseapp.presentation.features.homefeature

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.androidbaseapp.R
import com.example.androidbaseapp.domain.interactor.types.DailyWorldData
import com.example.androidbaseapp.presentation.BaseFragment
import com.example.androidbaseapp.presentation.customview.ITabBarClickedHandler
import com.example.androidbaseapp.common.toColumnData
import com.example.androidbaseapp.common.toTabLabelData
import com.example.androidbaseapp.data.repositories.model.ArticleModel
import com.example.androidbaseapp.presentation.UiAction
import com.example.androidbaseapp.presentation.UiState
import com.example.androidbaseapp.presentation.adapter.ArticleAdapter
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HomeFragment : BaseFragment() {

    private val homeFragmentViewModel: HomeFragmentViewModel by viewModels()

    private val tapOptionSelectListener = object : ITabBarClickedHandler {
        override fun onLabelClick(tabItemPos: Int, value: String) {
            containerBarChart.updateColumn(tabItemPos)
        }
    }

    override fun getLayoutId() = R.layout.fragment_home

    override fun initData(data: Bundle?) {}

    override fun initViews() {
        tvBarChartTitle.text = "Corona World Statistic"
    }

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
        pagingData: Flow<PagingData<ArticleModel>>?,
        uiActions: ((UiAction) -> Unit)?
    ) {
        bindRcvList(uiState = uiState, uiActions = uiActions, pagingData = pagingData)
    }

    private fun bindRcvList(
        uiState: StateFlow<UiState>?,
        uiActions: ((UiAction) -> Unit)?,
        pagingData: Flow<PagingData<ArticleModel>>?
    ) {
        val adapter = ArticleAdapter()
        val layoutManager = LinearLayoutManager(this.context)
        rcvArticles.adapter = adapter
        rcvArticles.layoutManager = layoutManager
        rcvArticles.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy != 0 && uiActions != null) {
                    uiState?.value?.query?.let {
                        uiActions(UiAction.Scroll(currentQuery = it))
                    }
                }
            }
        })
        lifecycleScope.launch {
            pagingData?.collectLatest(adapter::submitData)
        }
    }

    private fun loadDataToCustomBarChart(dayModels: List<DailyWorldData>) {
        val tabOptionData = dayModels.toTabLabelData()
        val columnData = dayModels.toColumnData()
        containerBarChart.setData(columnData)
        tabOptions.setData(tabOptionData)
        tabOptions.setCallBack(tapOptionSelectListener)
    }
}
