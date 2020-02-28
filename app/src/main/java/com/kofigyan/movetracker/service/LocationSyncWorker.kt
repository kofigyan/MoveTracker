package com.kofigyan.movetracker.service

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import timber.log.Timber


class LocationSyncWorker(ctx: Context, params: WorkerParameters) : CoroutineWorker(ctx, params) {

    //    @Inject
    //   lateinit var syncService: SyncService


    override suspend fun doWork(): Result {

        return try {


            //  syncService.syncLocationData()

            Result.success()
        } catch (throwable: Throwable) {
            Timber.e(throwable)
            Result.failure()
        }
    }
}