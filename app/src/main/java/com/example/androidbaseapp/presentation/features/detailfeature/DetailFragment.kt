package com.example.androidbaseapp.presentation.features.detailfeature

import android.os.Bundle
import androidx.fragment.app.viewModels
import com.example.androidbaseapp.R
import com.example.androidbaseapp.presentation.BaseFragment

class DetailFragment : BaseFragment() {

    private val detailFragmentViewModel: DetailFragmentViewModel by viewModels()

    override fun getLayoutId() = R.layout.fragment_detail

    override fun initData(data: Bundle?) {}

    override fun initViews() {}

    override fun initActions() {}

    override fun initObservers() {}
}
