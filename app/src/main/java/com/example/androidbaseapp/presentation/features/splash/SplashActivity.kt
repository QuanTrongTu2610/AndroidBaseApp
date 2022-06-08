package com.example.androidbaseapp.presentation.features.splash

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.example.androidbaseapp.R
import com.example.androidbaseapp.presentation.features.MainActivity
import com.example.androidbaseapp.utils.Logger
import dagger.hilt.android.AndroidEntryPoint


@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    private val splashViewModel: SplashViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        Logger.d("SplashActivity onCreate")

        splashViewModel.loadPrimaryData()
        splashViewModel.store.observe(
            owner = this,
            observer = {
                Logger.d("dataChangeObserver : data loaded - waiting to save")
            },
            selector = object : (SplashViewState) -> SplashViewState {
                override fun invoke(p1: SplashViewState): SplashViewState = p1
            }
        )
        splashViewModel.splashLoadCompleteLiveEvent.observe(this) { result ->
            Logger.d("splashLoadCompleteLiveEvent: $result")
            if (result) {
                startActivity(Intent(this, MainActivity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                })
                finish()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Logger.d("onSplashDestroy")
    }
}
