package com.kofigyan.movetracker.service

import android.app.Service
import com.kofigyan.movetracker.util.AndroidComponentUtil
import timber.log.Timber
import android.net.ConnectivityManager
import android.content.Intent
import android.content.BroadcastReceiver
import android.os.IBinder
import android.content.Context
import com.kofigyan.movetracker.api.SyncService
import com.kofigyan.movetracker.util.isNetworkConnected
import dagger.android.AndroidInjection
import javax.inject.Inject


class LocationSyncService : Service() {

//    @Inject
 //   lateinit var syncService: SyncService

    override fun onCreate() {
        AndroidInjection.inject(this)
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        Timber.i("Starting sync...")

        if (!isNetworkConnected(this)) {
            Timber.i("Sync canceled, connection not available")
            AndroidComponentUtil.toggleComponent(this, SyncOnConnectionAvailable::class.java, true)
            stopSelf(startId)
            return START_NOT_STICKY
        }


        stopSelf(startId)
        return START_NOT_STICKY

        //  syncService.syncLocationData()
        //  return START_STICKY
    }

    override fun onDestroy() {
         super.onDestroy()
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    class SyncOnConnectionAvailable : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action == ConnectivityManager.CONNECTIVITY_ACTION && isNetworkConnected(
                    context
                )
            ) {
                Timber.i("Connection is now available, triggering sync...")
                AndroidComponentUtil.toggleComponent(context, this.javaClass, false)
                context.startService(getStartIntent(context))
            }
        }
    }

    companion object {

        fun getStartIntent(context: Context): Intent {
            return Intent(context, LocationSyncService::class.java)
        }

        fun isRunning(context: Context): Boolean {
            return AndroidComponentUtil.isServiceRunning(context, LocationSyncService::class.java)
        }
    }

}