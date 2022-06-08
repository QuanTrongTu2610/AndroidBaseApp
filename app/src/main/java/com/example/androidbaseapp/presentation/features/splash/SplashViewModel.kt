package com.example.androidbaseapp.presentation.features.splash

import androidx.lifecycle.LiveData
import com.example.androidbaseapp.domain.interactor.GetRemoteBasicCountriesUseCase
import com.example.androidbaseapp.domain.interactor.SaveLocalBasicCountriesUseCase
import com.example.androidbaseapp.domain.model.BasicCountryModel
import com.example.androidbaseapp.utils.Logger
import com.example.androidbaseapp.utils.livedata.SingleLiveEvent
import com.example.androidbaseapp.presentation.base.BaseViewModel
import com.example.androidbaseapp.utils.ResultWrapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val getRemoteBasicCountriesUseCase: GetRemoteBasicCountriesUseCase,
    private val saveLocalBasicCountriesUseCase: SaveLocalBasicCountriesUseCase
) : BaseViewModel<SplashViewState>() {

    companion object {
        // waiting for 2 seconds as default
        private const val MIN_SPLASH_WAIT_TIME = 1000L
    }

    private val _splashLoadCompleteLiveEvent: SingleLiveEvent<Boolean> = SingleLiveEvent()
    val splashLoadCompleteLiveEvent: LiveData<Boolean>
        get() = _splashLoadCompleteLiveEvent

    override fun initState(): SplashViewState =
        SplashViewState(listBasicCountryModelInformation = emptyList())

    private suspend fun loadDataBefore() {
        Logger.d("getAllBasicCountryDataUseCase = $getRemoteBasicCountriesUseCase")
        getRemoteBasicCountriesUseCase.execute().let { result ->
            when (result) {
                is ResultWrapper.Success -> {
                    withContext(Dispatchers.Main) {
                        dispatchState(SplashViewState(listBasicCountryModelInformation = result.data))
                    }
                    saveFundamentalCovidCountryDatabase(result.data)
                }
                is ResultWrapper.Error -> {
                    Logger.d("Unexpected result: ${result.exception.message}")
                }
            }
        }
    }

    private suspend fun saveFundamentalCovidCountryDatabase(data: List<BasicCountryModel>) {
        when (val result = saveLocalBasicCountriesUseCase.execute(data)) {
            is ResultWrapper.Success -> {
                _splashLoadCompleteLiveEvent.postValue(true)
            }
            is ResultWrapper.Error -> {
                Logger.d("Unexpected result: ${result.exception.message}")
            }
        }
    }

    // fake load authentication :))
    fun loadPrimaryData() {
        defaultScope.launch(Dispatchers.IO) {
            Logger.d("loadPrimaryData: $coroutineContext")
            delay(MIN_SPLASH_WAIT_TIME)
            loadDataBefore()
        }
    }

    override fun onCleared() {
        Logger.d("#onCleared")
        super.onCleared()
    }
}