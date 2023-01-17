package com.example.androidbaseapp.presentation.features.shortvideofeature

import androidx.lifecycle.SavedStateHandle
import com.example.androidbaseapp.presentation.BaseViewModel
import com.example.androidbaseapp.common.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ShortVideoFragmentViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
) : BaseViewModel<ShortFragmentState>() {

    override fun initState(): ShortFragmentState = ShortFragmentState(state = "")

    override fun onCleared() {
        Logger.d("onCleared")
        super.onCleared()
    }
}

