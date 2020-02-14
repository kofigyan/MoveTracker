package com.kofigyan.movetracker.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kofigyan.movetracker.viewmodel.EventViewModel
import com.kofigyan.movetracker.viewmodel.LocationViewModel
import com.kofigyan.movetracker.viewmodel.TrackerViewModelFactory
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
internal abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(LocationViewModel::class)
    abstract fun bindLocationViewModel(viewModel : LocationViewModel) : ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(EventViewModel::class)
    abstract fun bindEventViewModel(viewModel : EventViewModel) : ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: TrackerViewModelFactory): ViewModelProvider.Factory

}