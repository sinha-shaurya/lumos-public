package com.example.lumos.network.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.lumos.databinding.CategoryItemBinding
import com.example.lumos.network.dataclasses.events.Category

class EventCategoryDataAdapter :
    ListAdapter<Category, EventCategoryDataAdapter.EventCategoryViewHolder>(EventCategoryDiffUtilCallBack()) {

    inner class EventCategoryViewHolder(val binding: CategoryItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Category) {
            binding.apply {
                categoryItemName.text=item.name
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventCategoryViewHolder {
        val binding =
            CategoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EventCategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EventCategoryViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }
}

class EventCategoryDiffUtilCallBack : DiffUtil.ItemCallback<Category>() {
    override fun areItemsTheSame(oldItem: Category, newItem: Category) =
        (oldItem.nameSlug == newItem.nameSlug)

    override fun areContentsTheSame(oldItem: Category, newItem: Category) = (oldItem == newItem)

}