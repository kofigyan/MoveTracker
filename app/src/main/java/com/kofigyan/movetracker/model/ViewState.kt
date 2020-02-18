package com.kofigyan.movetracker.model

import androidx.annotation.StringRes

sealed class ViewState{

    object Loading : ViewState()

    object HasData : ViewState()

    object NoData : ViewState()

    class Error(@StringRes val errorMsg: Int) : ViewState()
}
