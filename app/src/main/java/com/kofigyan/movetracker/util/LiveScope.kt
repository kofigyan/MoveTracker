package com.kofigyan.movetracker.util

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import kotlinx.coroutines.*

/**
 *  Scope aware lifecycle observer
 */
class LiveScope(val block: suspend CoroutineScope.() -> Unit) : DefaultLifecycleObserver {

    private var scope: CoroutineScope? = null

    override fun onStart(owner: LifecycleOwner) {
        scope = CoroutineScope(Job() + Dispatchers.IO)
        scope?.launch {
            block()
        }
    }

    override fun onStop(owner: LifecycleOwner) {
        scope?.coroutineContext?.cancel()
    }

}

fun LifecycleOwner.liveScope(block: suspend CoroutineScope.() -> Unit) {
    lifecycle.addObserver(LiveScope(block))
}