package com.kofigyan.movetracker.util

import android.app.ActivityManager
import android.content.ComponentName
import android.content.Context
import android.content.pm.PackageManager


object AndroidComponentUtil {

    fun toggleComponent(context: Context, componentClass: Class<*>, enable: Boolean) {
        val componentName = ComponentName(context, componentClass)
        val pm = context.packageManager
        pm.setComponentEnabledSetting(
            componentName,
            if (enable)
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED
            else
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
            PackageManager.DONT_KILL_APP
        )
    }

    fun isServiceRunning(context: Context, serviceClass: Class<*>): Boolean {
        val manager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.name == service.service.className) {
                return true
            }
        }
        return false
    }
}