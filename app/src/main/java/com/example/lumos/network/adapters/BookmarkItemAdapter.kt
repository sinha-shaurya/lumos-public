package com.example.lumos.network.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.lumos.databinding.BookmarkItemBinding
import com.example.lumos.local.SavedPost

class BookmarkItemAdapter :
    androidx.recyclerview.widget.ListAdapter<SavedPost, BookmarkItemAdapter.BookmarkItemViewHolder>(
        BookmarkDiffUtilCallback()
    ) {

    inner class BookmarkItemViewHolder(val binding: BookmarkItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: SavedPost) {
            binding.apply {
                blogTitle.text = item.title
                postDescription.text = item.descriptionShort
                authorName.text = item.author
            }
        }

    }

    override fun onBindViewHolder(holder: BookmarkItemViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookmarkItemViewHolder {
        val binding =
            BookmarkItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BookmarkItemViewHolder(binding)
    }
}

class BookmarkDiffUtilCallback : DiffUtil.ItemCallback<SavedPost>() {
    override fun areItemsTheSame(oldItem: SavedPost, newItem: SavedPost) =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: SavedPost, newItem: SavedPost): Boolean =
        oldItem == newItem

}