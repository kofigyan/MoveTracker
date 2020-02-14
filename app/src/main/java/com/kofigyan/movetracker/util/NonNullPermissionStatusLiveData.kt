package com.kofigyan.movetracker.util

import android.content.Context
import androidx.annotation.Nullable
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.kofigyan.movetracker.util.PermissionStatus.Denied
import com.kofigyan.movetracker.util.PermissionStatus.Granted

class NonNullPermissionStatusLiveData(context: Context) : PermissionStatusLiveData(context) {

    fun observe(owner: LifecycleOwner, observer: PermissionStatusObserver) {
        super.observe(owner, object : Observer<PermissionStatus> {
            override fun onChanged(@Nullable status: PermissionStatus?) {
                if (status == null ) {
                    return
                }

                when(status){
                    is Granted -> return
                    is Denied ->  observer.onNewMessage()
                }

            }
        })
    }

    interface PermissionStatusObserver {

        fun onNewMessage()
    }


}