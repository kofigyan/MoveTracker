package com.kofigyan.movetracker.ui

import android.os.Bundle
import com.kofigyan.movetracker.R
import com.kofigyan.movetracker.databinding.ActivityAllEventsBinding
import com.kofigyan.movetracker.ui.base.ListBaseActivity
import com.kofigyan.movetracker.util.observeBridge
import com.kofigyan.movetracker.viewmodel.EventViewModel
import kotlinx.android.synthetic.main.activity_all_events.*

class AllEventsActivity : ListBaseActivity<EventViewModel,ActivityAllEventsBinding>() {

    override val layoutId: Int
        get() = R.layout.activity_all_events

    override val viewModelClass : Class<EventViewModel>
        get() = EventViewModel::class.java



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.lifecycleOwner = this
        binding.viewmodel = viewModel

        setupRecyclerView(recyclerview)

        observeAllEventsWithLocations()
    }

    private fun observeAllEventsWithLocations() {
        viewModel.allEventsWithLocations.observeBridge(this) { eventsLocations ->
            eventsLocations?.let {
                locationEventAdapter.submitList(eventsLocations)
            }
        }
    }
}