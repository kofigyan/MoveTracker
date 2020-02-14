package com.kofigyan.movetracker.model

import androidx.annotation.StringRes
import  com.kofigyan.movetracker.model.EventState.READY
import  com.kofigyan.movetracker.model.EventState.ENDED
import  com.kofigyan.movetracker.model.EventState.STARTED
import  com.kofigyan.movetracker.model.EventState.STILL

data class Resource<out T>(val state: EventState, val data: T?, val message: Int?) {

    companion object {

        fun <T> started(@StringRes msg: Int,data: T? = null): Resource<T> {
            return Resource(STARTED, data, msg)
        }

        fun <T> ready(@StringRes msg: Int): Resource<T> {
            return Resource(READY,null,  msg)
        }

        fun <T> ended(@StringRes msg: Int): Resource<T> {
            return Resource(ENDED,null,  msg)
        }

        fun <T> still(@StringRes msg: Int): Resource<T> {
            return Resource(STILL,  null, msg)
        }
    }

}