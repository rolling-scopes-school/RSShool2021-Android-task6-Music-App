package com.anarut.mplayer.activity

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@InstallIn(ActivityComponent::class)
@Module
abstract class MainScreenModule {
    @Binds
    abstract fun bindActivity(activity: MainActivity): MainScreenInterface.View

    @Binds
    abstract fun bindPresenter(impl: MainScreenPresenter): MainScreenInterface.Presenter
}