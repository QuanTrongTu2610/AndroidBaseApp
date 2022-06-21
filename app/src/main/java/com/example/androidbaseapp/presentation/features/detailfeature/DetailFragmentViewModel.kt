package com.example.androidbaseapp.presentation.features.detailfeature

import androidx.lifecycle.SavedStateHandle
import com.example.androidbaseapp.presentation.BaseViewModel
import com.example.androidbaseapp.common.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailFragmentViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
) : BaseViewModel<DetailFragmentState>() {

    override fun initState(): DetailFragmentState = DetailFragmentState(state = "")

    override fun onCleared() {
        Logger.d("onCleared")
        super.onCleared()
    }
}

