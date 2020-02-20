package com.kofigyan.movetracker.di

import android.app.Application
import android.app.NotificationManager
import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.kofigyan.movetracker.api.SyncService
import com.kofigyan.movetracker.db.TrackerRoomDatabase
import com.kofigyan.movetracker.db.dao.EventDao
import com.kofigyan.movetracker.db.dao.LocationDao
import com.kofigyan.movetracker.util.LiveDataCallAdapterFactory
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.FusedLocationProviderClient
import com.kofigyan.movetracker.util.PREF_FILE_NAME


@Module(includes = arrayOf(ViewModelModule::class))
class AppModule {


    @Provides
    fun provideContext(application: Application): Context = application.applicationContext


    @Singleton
    @Provides
    fun provideApiService(): SyncService {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(LiveDataCallAdapterFactory())
            .baseUrl("BASE URL")
            .build()
            .create(SyncService::class.java)
    }


    @Singleton
    @Provides
    fun provideDb(app: Application): TrackerRoomDatabase {
        return Room
            .databaseBuilder(app, TrackerRoomDatabase::class.java, "tracker_database.db")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideEventDao(db: TrackerRoomDatabase): EventDao {
        return db.eventDao()
    }

    @Singleton
    @Provides
    fun provideLocationDao(db: TrackerRoomDatabase): LocationDao {
        return db.locationDao()
    }

    @Singleton
    @Provides
    fun provideNotificationManager(app: Application): NotificationManager {
        return app.getSystemService(
            Context.NOTIFICATION_SERVICE
        ) as NotificationManager
    }

    @Provides
    @Singleton
    fun provideFusedLocationProviderClient(context: Context): FusedLocationProviderClient {
        return LocationServices.getFusedLocationProviderClient(context)
    }

    @Singleton
    @Provides
    fun provideSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(
            PREF_FILE_NAME,
            Context.MODE_PRIVATE
        )
    }


}