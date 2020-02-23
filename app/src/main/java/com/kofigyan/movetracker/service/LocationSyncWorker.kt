package com.kofigyan.movetracker.service

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import timber.log.Timber


class LocationSyncWorker(ctx: Context, params: WorkerParameters) : Worker(ctx, params) {

    //    @Inject
    //   lateinit var syncService: SyncService


    override fun doWork(): Result {

        return try {


            //  syncService.syncLocationData()

            Result.success()
        } catch (throwable: Throwable) {
            Timber.e(throwable)
            Result.failure()
        }
    }
}