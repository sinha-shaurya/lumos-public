package com.example.lumos.network.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.lumos.databinding.BookmarkItemBinding
import com.example.lumos.local.SavedPost

class BookmarkItemAdapter(private val listener: onBookmarkItemClickListener) :
    ListAdapter<SavedPost, BookmarkItemAdapter.BookmarkItemViewHolder>(
        BookmarkDiffUtilCallback()
    ) {

    inner class BookmarkItemViewHolder(val binding: BookmarkItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: SavedPost) {
            binding.apply {
                blogTitle.text = item.title
                //remove trailing newlines,spaces,tabs ,or carriage returns
                postDescription.text = item.descriptionShort.trimEnd('\n', ' ', '\t', '\r')
                authorName.text = item.author
            }

            //set onClick for entire card
            binding.root.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val bookmarkItem = getItem(position)
                    if (bookmarkItem != null)
                        listener.onBookmarkItemClick(bookmarkItem)
                }
            }

            //set on Click for delete icon
            binding.deletePostIcon.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val bookmarkItem = getItem(position)
                    if (bookmarkItem != null)
                        listener.onDeleteClick(bookmarkItem)
                }
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

    interface onBookmarkItemClickListener {
        fun onBookmarkItemClick(item: SavedPost)

        fun onDeleteClick(item: SavedPost)
    }

    companion object {
        private const val TAG = "BookmarkItemAdapter"
    }
}

class BookmarkDiffUtilCallback : DiffUtil.ItemCallback<SavedPost>() {
    override fun areItemsTheSame(oldItem: SavedPost, newItem: SavedPost) =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: SavedPost, newItem: SavedPost): Boolean =
        oldItem == newItem

}