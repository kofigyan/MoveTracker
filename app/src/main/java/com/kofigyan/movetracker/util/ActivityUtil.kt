package com.kofigyan.movetracker.util

import android.app.Activity
import android.content.Intent

fun <T> Activity.selfStartActivity(clz: Class<T>) {
    startActivity(Intent(this, clz))
}




