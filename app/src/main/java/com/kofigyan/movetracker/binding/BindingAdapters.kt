package com.kofigyan.movetracker.binding

import android.view.View
import androidx.annotation.Nullable
import androidx.databinding.BindingAdapter
import com.kofigyan.movetracker.model.Location
import com.kofigyan.movetracker.model.ViewState.NoData
import com.kofigyan.movetracker.model.ViewState.HasData
import com.kofigyan.movetracker.model.ViewState

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

    @JvmStatic
    @BindingAdapter("emptyState")
    fun setViewStateForEmptyView(view: View,@Nullable viewState: ViewState?) {
        view.visibility = when (viewState){
            NoData -> View.VISIBLE
            else -> View.GONE
        }
    }

    @JvmStatic
    @BindingAdapter("uiState")
    fun setViewStateForLoadedEvent(view: View,@Nullable viewState: ViewState?) {
        view.visibility = when (viewState){
            HasData -> View.VISIBLE
            else -> View.GONE
        }
    }


}