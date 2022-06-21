package com.example.androidbaseapp.common.livedata

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.distinctUntilChanged
import androidx.lifecycle.map

/**
 * This class used to handle state (ViewModel in general)
 * */
class ViewStateStore<T : Any>(initialState: T) {
    private val stateLiveData = MutableLiveData<T>().apply {
        value = initialState
    }

    val state: T?
        get() = stateLiveData.value

    fun dispatchState(state: T) {
        stateLiveData.value = state
    }

    fun <S> observe(
        owner: LifecycleOwner,
        selector: (T) -> S,
        observer: Observer<S>
    ) {
        stateLiveData
            .map(selector)
            .distinctUntilChanged()
            .observe(owner, observer)
    }

    fun <S> observeAnyWay(
        owner: LifecycleOwner,
        selector: (T) -> S,
        observer: Observer<S>
    ) {
        stateLiveData
            .map(selector)
            .distinctUntilChanged()
            .observe(owner, observer)
    }
}