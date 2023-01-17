package com.example.androidbaseapp.presentation.features.shortvideofeature

import android.os.Bundle
import androidx.fragment.app.viewModels
import com.example.androidbaseapp.R
import com.example.androidbaseapp.presentation.BaseFragment

class ShortVideoFragment : BaseFragment() {

    private val detailFragmentViewModel: ShortVideoFragmentViewModel by viewModels()

    override fun getLayoutId() = R.layout.fragment_short_video

    override fun initData(data: Bundle?) {}

    override fun initViews() {}

    override fun initActions() {}

    override fun initObservers() {}
}
