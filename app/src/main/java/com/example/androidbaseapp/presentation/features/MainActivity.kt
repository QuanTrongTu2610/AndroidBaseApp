package com.example.androidbaseapp.presentation.features

import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.androidbaseapp.R
import com.example.androidbaseapp.common.views.viewBinding
import com.example.androidbaseapp.databinding.ActivityNavHostBinding
import com.example.androidbaseapp.presentation.BaseActivity
import com.example.androidbaseapp.presentation.navigation.NavigationManagerImpl
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : BaseActivity() {

    @Inject
    lateinit var navigationManager: NavigationManagerImpl
    private val navHostBinding: ActivityNavHostBinding by viewBinding()
    private val navController get() = findNavController(R.id.navHostFragment)

    override fun getLayoutId(): Int = R.layout.activity_nav_host

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        navHostBinding
            .bottomNav
            .setupWithNavController(navController)
    }

}