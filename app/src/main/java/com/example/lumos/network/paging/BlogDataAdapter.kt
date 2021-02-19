package com.example.lumos.network.paging

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.lumos.R
import com.example.lumos.databinding.BlogPostItemBinding
import com.example.lumos.network.dataclasses.blog.BlogPost

class BlogDataAdapter(private val listener: onItemClickListener) :
    PagingDataAdapter<BlogPost, BlogDataAdapter.BlogItemViewHolder>(
        POST_COMPARATOR
    ) {

    inner class BlogItemViewHolder(private val binding: BlogPostItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        //setup itemclicks on the entire card
        init {
            binding.root.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val item = getItem(position)
                    if (item != null) {
                        listener.onItemClick(item.id)
                    }
                }
            }
        }

        fun bind(item: BlogPost) {
            binding.apply {
                blogPostName.text = item.title
                blogPostAuthor.text = item.author
                Glide.with(itemView).load(generateImageUrl(item.imageUrl)).centerCrop()
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .placeholder(R.drawable.blog_image_placeholder)
                    .error(R.drawable.blog_image_error)
                    .into(blogPostImage)
                blogPostTime.text =
                    if (item.readTime == 1) "1 min" else item.readTime.toString() + " min"
            }
        }

        private fun generateImageUrl(imageUrl: String): String {
            val baseUrl = "https://blog.istemanipal.com/mobile/"
            return baseUrl + imageUrl
        }
    }

    override fun onBindViewHolder(holder: BlogItemViewHolder, position: Int) {
        val currentItem = getItem(position)
        if (currentItem != null) {
            holder.bind(currentItem)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BlogItemViewHolder {
        val binding =
            BlogPostItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BlogItemViewHolder(binding)
    }

    companion object {
        private val POST_COMPARATOR = object : DiffUtil.ItemCallback<BlogPost>() {
            override fun areItemsTheSame(oldItem: BlogPost, newItem: BlogPost) =
                oldItem.id.equals(newItem.id)

            override fun areContentsTheSame(oldItem: BlogPost, newItem: BlogPost) =
                (oldItem == newItem)
        }
    }

    //interface to handle on clicks
    interface onItemClickListener {
        fun onItemClick(id: String)
    }
}