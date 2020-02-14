package com.kofigyan.movetracker.binding

import android.view.View
import androidx.annotation.Nullable
import androidx.databinding.BindingAdapter
import com.kofigyan.movetracker.model.Location

object BindingAdapters {

    @JvmStatic
    @BindingAdapter("visibleGone")
    fun showHide(view: View, show: Boolean) {
        view.visibility = if (show) View.VISIBLE else View.GONE
    }

    @JvmStatic
    @BindingAdapter("visibleGone")
    fun showHide(view: View,@Nullable location:  Location?) {
        view.visibility = if (location != null) View.VISIBLE else View.GONE
    }


}