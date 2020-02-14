package com.kofigyan.movetracker.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kofigyan.movetracker.R
import com.kofigyan.movetracker.databinding.RecyclerviewItemBinding
import com.kofigyan.movetracker.model.EventWithLocations
import com.kofigyan.movetracker.util.StaticMapUrlBuilder.buildStaticMapUrl
import com.kofigyan.movetracker.util.applyDateTimeFormatter


class LocationEventAdapter internal constructor(
    val context: Context
) : ListAdapter<EventWithLocations, LocationEventAdapter.LocationEventViewHolder>(
    LocationEventDiffCallback()
) {

    class LocationEventViewHolder constructor(private val binding: RecyclerviewItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: EventWithLocations, context: Context) {

            binding.eventWithLocations = item
            binding.tvDateCreated.text = item.event.dateCreated?.format(applyDateTimeFormatter(context)).orEmpty()

            val imageUrl = buildStaticMapUrl(locations = item.locations)

            Glide.with(context)
                .load(imageUrl)
                .placeholder(R.mipmap.outline_photo_white_48)
                .into(binding.ivMovementPath)

            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): LocationEventViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = RecyclerviewItemBinding.inflate(layoutInflater, parent, false)
                return LocationEventViewHolder(
                    binding
                )
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationEventViewHolder {
        return LocationEventViewHolder.from(
            parent
        )
    }

    override fun onBindViewHolder(holder: LocationEventViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, context)
    }


    class LocationEventDiffCallback : DiffUtil.ItemCallback<EventWithLocations>() {
        override fun areItemsTheSame(
            oldItem: EventWithLocations,
            newItem: EventWithLocations
        ) = oldItem.event.eventId == newItem.event.eventId

        override fun areContentsTheSame(
            oldItem: EventWithLocations,
            newItem: EventWithLocations
        ) = oldItem == newItem
    }


}