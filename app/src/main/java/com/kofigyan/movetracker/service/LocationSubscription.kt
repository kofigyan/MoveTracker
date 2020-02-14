package com.kofigyan.movetracker.service

interface LocationSubscription {
    fun subscribe()
    fun subscribeForeground()
    fun unsubscribe()

}