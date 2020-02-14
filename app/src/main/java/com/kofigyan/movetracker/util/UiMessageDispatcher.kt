package com.kofigyan.movetracker.util

import androidx.annotation.Nullable
import androidx.annotation.StringRes
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.kofigyan.movetracker.R


/**
 * A SingleLiveEvent used for UI(Snackbar,Dialog,Toast etc) messages. Like a [SingleLiveEvent] but also prevents
 * null messages and uses a custom observer.
 *
 *
 * Note that only one observer is going to be notified of changes.
 */
class UiMessageDispatcher<T> : SingleLiveEvent<T>() {

    fun observe(owner: LifecycleOwner, observer: UiMessageObserver<T>) {
        super.observe(owner, object : Observer<T> {
           override fun onChanged(@Nullable t: T?) {
                if (t == null) {
                    return
                }
                observer.onNewMessage(t)
            }
        })
    }

    interface UiMessageObserver<T> {
        /**
         * Called when there is a new message to be shown.
         * @param uiMessageResourceId The new message, non-null.
         */
        fun onNewMessage(uiMessageResourceId: T)
    }

}

data class DialogMessage(@StringRes val title: Int, @StringRes val body: Int,
                         @StringRes val positiveText: Int = R.string.dialog_postive_ok,
                         @StringRes val negativeText: Int = R.string.dialog_postive_cancel,
                         val action: () -> Unit = {})