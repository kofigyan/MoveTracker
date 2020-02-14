package com.kofigyan.movetracker.di

import android.app.Application
import androidx.room.Room
import com.kofigyan.movetracker.api.SyncService
import com.kofigyan.movetracker.db.dao.EventDao
import com.kofigyan.movetracker.db.dao.LocationDao
import com.kofigyan.movetracker.db.TrackerRoomDatabase
import com.kofigyan.movetracker.util.LiveDataCallAdapterFactory
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module(includes = arrayOf(ViewModelModule::class))
class AppModule {

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

}