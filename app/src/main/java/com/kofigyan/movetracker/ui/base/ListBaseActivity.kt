package com.kofigyan.movetracker.ui.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.kofigyan.movetracker.ui.adapter.LocationEventAdapter
import dagger.android.AndroidInjection
import javax.inject.Inject

abstract class ListBaseActivity<VM : ViewModel,B : ViewDataBinding> : AppCompatActivity() {

    protected lateinit var binding : B
    protected abstract val layoutId: Int

    protected lateinit var locationEventAdapter: LocationEventAdapter

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


    protected fun setupRecyclerView(
        recyclerView: RecyclerView
    ) {
        locationEventAdapter = LocationEventAdapter(this)
        with(recyclerView) {
            setHasFixedSize(true)
            adapter = locationEventAdapter
        }
    }



}