package com.example.lumos.network.adapters

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.lumos.databinding.ItemEventBinding
import com.example.lumos.network.dataclasses.events.Events
import com.example.lumos.utils.DateTimeConverter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EventListAdapter :
    androidx.recyclerview.widget.ListAdapter<Events, EventListAdapter.EventViewHolder>(
        EventDiffUtilCallback()
    ) {
    inner class EventViewHolder(val binding: ItemEventBinding) :
        RecyclerView.ViewHolder(binding.root) {
        //setup all elements inside the item

        fun bind(item: Events) {
            val date = DateTimeConverter(item.eventDates?.get(0)?.startDate!!)
            val dateText = date.getDay() + " " + date.getMonth()
            val timeText = date.getTime()
            binding.apply {
                eventName.text = item.eventName
                eventDescription.text = item.description


                /*
                val dates=getDates(item)
                if(dates.isEmpty())
                    eventDate.isVisible=false
                else
                    eventDate.text=dates

                 */
                getDates(item)
                getLocations(item)

                if(item.registrationLink!=null)
                    openRegistration(item.registrationLink)
                else
                    registrationLinkButton.isVisible=false
            }
        }

        fun getDates(item: Events) {
            var dateList=""
            CoroutineScope(Dispatchers.Default).launch{
                for(i in item.eventDates?: emptyList()){
                    val date=DateTimeConverter(i.startDate!!)
                    dateList= "$dateList${date.getDay()} ${date.getMonth()},"
                }
                val length=dateList.length
                if(dateList[length-1]==',')
                {
                    withContext(Dispatchers.Main){
                        binding.eventDate.text=dateList.dropLast(1)
                    }

                }
                else
                    withContext(Dispatchers.Main){
                        binding.eventDate.text=dateList
                    }

            }

        }

        fun getLocations(item:Events)=
            CoroutineScope(Dispatchers.Default).launch{
                var locationList=""
                for(i in item.eventDates?: emptyList()){
                    locationList=locationList+i.venue+","
                }
                if(locationList[locationList.length-1]==',')
                    locationList=locationList.dropLast(1)
                withContext(Dispatchers.Main){
                    binding.eventLocation.text=locationList
                }
            }


        fun openRegistration(url:String)=binding.registrationLinkButton.setOnClickListener {
            val uri= Uri.parse(url)
            val intent= Intent(Intent.ACTION_VIEW,uri)
            startActivity(itemView.context,intent,null)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val binding = ItemEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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