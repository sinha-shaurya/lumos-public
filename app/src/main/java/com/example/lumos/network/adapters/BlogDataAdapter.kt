package com.example.lumos.network.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.example.lumos.R
import com.example.lumos.databinding.BlogPostItemBinding
import com.example.lumos.network.dataclasses.blog.BlogPost
import com.squareup.picasso.Picasso

class BlogDataAdapter(private val listener: onItemClickListener) :
    PagingDataAdapter<BlogPost, BlogDataAdapter.BlogItemViewHolder>(
        POST_COMPARATOR
    ) {

    inner class BlogItemViewHolder(
        private val binding: BlogPostItemBinding,
        private val context: Context
    ) :
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
            val circularProgressDrawable=CircularProgressDrawable(context)
            circularProgressDrawable.apply {
                centerRadius=30f
                strokeWidth=5f
                setColorSchemeColors(ContextCompat.getColor(context,R.color.colorAccent))
            }
            circularProgressDrawable.start()

            binding.apply {
                blogPostName.text = item.title
                blogPostAuthor.text = item.author
                Picasso.get().load(generateImageUrl(item.imageUrl))
                    .placeholder(circularProgressDrawable)
                    .error(R.drawable.blog_image_error)
                    .fit()
                    .centerCrop()
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
        //asynchronously try to inflate the view
        return BlogItemViewHolder(binding, parent.context)
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