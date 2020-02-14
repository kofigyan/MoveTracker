package com.kofigyan.movetracker.util

import android.content.Context
import androidx.annotation.Nullable
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.kofigyan.movetracker.util.GpsStatus.Disabled
import com.kofigyan.movetracker.util.GpsStatus.Enabled

class NonNullGpaStatusLiveData(context: Context) : GpsStatusLiveData(context) {

    fun observe(owner: LifecycleOwner, observer: GpsStatusMessageObserver) {
        super.observe(owner, object : Observer<GpsStatus> {
            override fun onChanged(@Nullable status: GpsStatus?) {
                if (status == null ) {
                    return
                }

                when(status){
                    is Enabled -> return
                    is Disabled ->  observer.onNewMessage(status)
                }

            }
        })
    }

    interface GpsStatusMessageObserver {
        /**
         * Called when there is a new message to be shown.
         * @param uiMessageResourceId The new message, non-null.
         */
        fun onNewMessage(uiMessageResourceId: GpsStatus)
    }


}