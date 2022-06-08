package com.example.androidbaseapp.utils.dependencyinjection

import com.example.androidbaseapp.presentation.navigation.INavigationManager
import com.example.androidbaseapp.presentation.navigation.NavigationManagerImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
abstract class NavigationModule {

    @Binds
    abstract fun bindNavigationManager(
        navigationManagerImpl: NavigationManagerImpl
    ): INavigationManager
}