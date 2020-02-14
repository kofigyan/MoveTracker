package com.kofigyan.movetracker.ui.base

import android.os.Bundle
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import com.kofigyan.movetracker.viewmodel.base.BaseViewModel
import dagger.android.AndroidInjection
import javax.inject.Inject

abstract class BaseActivity<VM : BaseViewModel,B : ViewDataBinding> : AppCompatActivity() {

    protected abstract val layoutId: Int
    protected lateinit var binding: B

    protected abstract val viewModelClass : Class<VM>

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    protected val viewModel : VM by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(viewModelClass)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, layoutId)
    }

    protected fun showMotionStateMessage(
        @StringRes message: Int
    ) = Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG).show()


}