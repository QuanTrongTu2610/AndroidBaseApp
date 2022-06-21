package com.example.androidbaseapp.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidbaseapp.common.Logger
import com.example.androidbaseapp.common.livedata.SingleLiveEvent
import com.example.androidbaseapp.common.livedata.ViewStateStore
import kotlinx.coroutines.CoroutineScope

/**
 * BaseViewModel used to defined general implementations for all view-models
 * */
abstract class BaseViewModel<T : Any> : ViewModel() {

    val defaultScope: CoroutineScope by lazy {
        viewModelScope.also {
            Logger.d("ViewModelScope: ${it.coroutineContext}")
        }
    }

    val store =  ViewStateStore(this.initState())

    val currentState: T? get() = store.state

    private val _errorLiveEvent: SingleLiveEvent<Throwable> = SingleLiveEvent()

    val errorLiveEvent: LiveData<Throwable>
        get() = _errorLiveEvent

    abstract fun initState(): T

    protected fun dispatchError(error: Throwable) {
        _errorLiveEvent.value = error
    }

    protected fun dispatchState(newState: T) {
        Logger.d("newState = $newState")
        store.dispatchState(newState)
    }
}