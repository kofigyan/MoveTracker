package com.kofigyan.movetracker.ui

import android.Manifest
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.lifecycle.Observer
import com.kofigyan.movetracker.R
import com.kofigyan.movetracker.databinding.ActivityMainBinding
import com.kofigyan.movetracker.ui.base.BaseActivity
import com.kofigyan.movetracker.util.*
import com.kofigyan.movetracker.util.DialogUtil.showAllGenericAlertDialog
import com.kofigyan.movetracker.util.DialogUtil.showSingleGenericAlertDialog
import com.kofigyan.movetracker.viewmodel.LocationViewModel
import com.nabinbhandari.android.permissions.PermissionHandler
import com.nabinbhandari.android.permissions.Permissions
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber
import java.util.*

class MainActivity : BaseActivity<LocationViewModel,ActivityMainBinding>() {

    override val layoutId: Int
        get() = R.layout.activity_main

    override val viewModelClass : Class<LocationViewModel>
        get() = LocationViewModel::class.java



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.lifecycleOwner = this
        binding.viewmodel = viewModel

        subscribeToGpsListener()

        subscribeToLocationPermissionListener()

        setupAllEventsActivityNavigation()

        subscribeToExitAppDispatcher()

        subscribeToMotionStateDispatcher()

        subscribeToDialogMessage()
    }


    private fun subscribeToDialogMessage(){
        viewModel.dialogMessageDispatcher.observe(this, dialogMessageObserver)
    }

    private fun subscribeToMotionStateDispatcher() {
        viewModel.motionStateMessageDispatcher.observe(this, motionStateObserver)
    }


    private fun subscribeToExitAppDispatcher() {
        viewModel.exitAppDispatcher.observe(this, exitAppObserver)
    }

    private fun setupAllEventsActivityNavigation() {
        btnViewAllEvents.setOnClickListener {
            viewModel.navigateToAllEventsActivity(it?.id.toString())
        }

        viewModel.navigateToAllEvents.observe(this, Observer {
            it.getContentIfNotHandled()?.let {
                 selfStartActivity(AllEventsActivity::class.java)
            }
        })
    }


    private fun subscribeToGpsListener() {
        viewModel.gpsStatusLiveData.observe(this, gpsStatusMessageObserver)
    }


    private fun subscribeToLocationPermissionListener() {
        viewModel.permissionStatusLiveData.observe(this, locationPermissionObserver)
    }


    private fun showGpsNotEnabledDialog() {
        showAllGenericAlertDialog(
            this,
            R.string.gps_required_title,
            R.string.gps_required_body,
            positiveText = R.string.action_settings
        ) {
            openGpsSettings()
        }
    }

    private fun openGpsSettings() {
        val intent = Intent().apply {
            action = Settings.ACTION_LOCATION_SOURCE_SETTINGS
        }
        startActivity(intent)
    }

    private fun showLocationPermissionNeededDialog() {
        showSingleGenericAlertDialog(
            this,
            R.string.permission_required_title,
            R.string.permission_required_body,
            cancelable = false
        ) {
            Permissions.check(
                this@MainActivity,
                Manifest.permission.ACCESS_FINE_LOCATION,
                null,
                permissionHandler
            )
        }
    }

    private val gpsStatusMessageObserver = object : NonNullGpsStatusLiveData.GpsStatusMessageObserver {
        override fun onNewMessage() {
            showGpsNotEnabledDialog()
        }
    }

    private val locationPermissionObserver = object : NonNullPermissionStatusLiveData.PermissionStatusObserver {
        override fun onNewMessage() {
            showLocationPermissionNeededDialog()
        }
    }


    private val dialogMessageObserver = object : UiMessageDispatcher.UiMessageObserver<DialogMessage> {
        override fun onNewMessage(dialogMessage: DialogMessage) {
            showAllGenericAlertDialog(
                this@MainActivity,
                dialogMessage.title,
                dialogMessage.body,
                positiveText = dialogMessage.positiveText, negativeText = dialogMessage.negativeText
            ) {
                dialogMessage.action.invoke()
            }
        }
    }


    private val motionStateObserver = object : UiMessageDispatcher.UiMessageObserver<Int> {
        override fun  onNewMessage(uiMessageResourceId: Int) {
            showMotionStateMessage(uiMessageResourceId)
        }
    }

    private val exitAppObserver = object : UiMessageDispatcher.UiMessageObserver<Int> {
        override fun onNewMessage(messageResourceId: Int) {
            finish()
        }
    }


    private val permissionHandler = object : PermissionHandler() {
        override fun onGranted() {
            Timber.i("MainActivity: %s", R.string.permission_status_granted)
        }

        override fun onDenied(context: Context?, deniedPermissions: ArrayList<String>?) {
            Timber.w("MainActivity: %s", R.string.permission_status_denied)
        }

        override fun onJustBlocked(
            context: Context?,
            justBlockedList: ArrayList<String>?,
            deniedPermissions: ArrayList<String>?
        ) {
            Timber.w("MainActivity: %s", R.string.permission_status_blocked)
        }
    }


    override fun onBackPressed() {
        viewModel.onExitAppButtonClicked()
    }

}
