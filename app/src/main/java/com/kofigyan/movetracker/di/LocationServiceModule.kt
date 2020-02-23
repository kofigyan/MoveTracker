package com.kofigyan.movetracker.di

import com.kofigyan.movetracker.service.LocationUpdateService
import dagger.android.ContributesAndroidInjector
import dagger.Module



@Module
internal abstract class LocationServiceModule {

    @ContributesAndroidInjector
    internal abstract fun contributeLocationService(): LocationUpdateService
}