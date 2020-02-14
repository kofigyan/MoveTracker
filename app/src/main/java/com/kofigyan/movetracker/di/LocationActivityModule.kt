package com.kofigyan.movetracker.di


import com.kofigyan.movetracker.ui.AllEventsActivity
import com.kofigyan.movetracker.ui.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
abstract class LocationActivityModule {

    @ContributesAndroidInjector
    abstract fun contributeMainActivity(): MainActivity

    @ContributesAndroidInjector
    abstract fun contributeAllEventsActivity(): AllEventsActivity

}
