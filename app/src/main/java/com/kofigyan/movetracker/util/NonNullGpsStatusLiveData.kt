package com.kofigyan.movetracker.util

import android.content.Context
import androidx.annotation.Nullable
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.kofigyan.movetracker.util.GpsStatus.Disabled
import com.kofigyan.movetracker.util.GpsStatus.Enabled

class NonNullGpsStatusLiveData(context: Context) : GpsStatusLiveData(context) {

    fun observe(owner: LifecycleOwner, observer: GpsStatusMessageObserver) {
        super.observe(owner, object : Observer<GpsStatus> {
            override fun onChanged(@Nullable status: GpsStatus?) {
                if (status == null ) {
                    return
                }

                when(status){
                    is Enabled -> return
                    is Disabled ->  observer.onNewMessage()
                }

            }
        })
    }

    interface GpsStatusMessageObserver {

        fun onNewMessage()
    }


}