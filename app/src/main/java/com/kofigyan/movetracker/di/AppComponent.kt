package com.kofigyan.movetracker.di

import android.app.Application
import com.kofigyan.movetracker.MoveTrackerApplication
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton


@Singleton
@Component(
    modules = [
        AndroidInjectionModule::class,
        AppModule::class,
        LocationActivityModule::class, LocationServiceModule::class]
)
interface AppComponent {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }

    fun inject(moveTrackerApplication: MoveTrackerApplication)

}