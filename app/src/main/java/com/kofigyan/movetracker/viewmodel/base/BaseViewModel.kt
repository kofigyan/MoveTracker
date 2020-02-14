package com.kofigyan.movetracker.viewmodel.base

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.kofigyan.movetracker.util.*

open class BaseViewModel(application: Application) : AndroidViewModel(application) {

    val dialogMessageDispatcher: UiMessageDispatcher<DialogMessage> =
        UiMessageDispatcher()

    protected val _navigateToAllEvents = MutableLiveData<EventWrapper<String>>()
    val navigateToAllEvents: LiveData<EventWrapper<String>>
        get() = _navigateToAllEvents

    val exitAppDispatcher: UiMessageDispatcher<Int> = UiMessageDispatcher()
    val motionStateMessageDispatcher: UiMessageDispatcher<Int> = UiMessageDispatcher()

}