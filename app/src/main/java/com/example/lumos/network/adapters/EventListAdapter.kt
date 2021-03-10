package com.example.lumos.network.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.lumos.databinding.EventItemBinding
import com.example.lumos.network.dataclasses.events.Events
import com.example.lumos.utils.DateTimeConverter
import com.squareup.picasso.Picasso

class EventListAdapter :
    androidx.recyclerview.widget.ListAdapter<Events, EventListAdapter.EventViewHolder>(
        EventDiffUtilCallback()
    ) {
    inner class EventViewHolder(val binding: EventItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        //setup all elements inside the item

        fun bind(item: Events) {
            val date = DateTimeConverter(item.eventDates?.get(0)?.startDate!!)
            val dateText = date.getDay() + " " + date.getMonth()
            val timeText = date.getTime()
            binding.apply {
                eventTitle.text = item.eventName
                eventDate.text = dateText
                eventTime.text = timeText
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val binding = EventItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EventViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

}

class EventDiffUtilCallback : DiffUtil.ItemCallback<Events>() {
    override fun areItemsTheSame(oldItem: Events, newItem: Events) =
        oldItem.eventName == newItem.eventName

    override fun areContentsTheSame(oldItem: Events, newItem: Events) =
        oldItem == newItem
}