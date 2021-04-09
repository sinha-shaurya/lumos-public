package com.example.lumos.network.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.bitmap.TransformationUtils.centerCrop
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.lumos.R
import com.example.lumos.databinding.CategoryItemBinding
import com.example.lumos.network.dataclasses.events.Category
import com.example.lumos.utils.GlideApp

private const val ISTE_BASE_URL = "https://test.istemanipal.com"

class EventCategoryDataAdapter(private val listener: onCategoryItemClickListener) :
    ListAdapter<Category, EventCategoryDataAdapter.EventCategoryViewHolder>(
        EventCategoryDiffUtilCallBack()
    ) {

    inner class EventCategoryViewHolder(val binding: CategoryItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val item = getItem(position)
                    if (item != null)
                        listener.onItemClick(position)
                }
            }
        }

        fun bind(item: Category) {
            val circularProgressDrawable = CircularProgressDrawable(itemView.context)
            circularProgressDrawable.apply {
                centerRadius = 30f
                strokeWidth = 5f
                setColorSchemeColors(ContextCompat.getColor(itemView.context, R.color.colorAccent))
            }
            circularProgressDrawable.start()
            binding.apply {
                categoryItemName.text = item.name
                categoryItemDescription.apply {
                    if (item.description != null)
                        this.text = item.description
                    else
                        this.isVisible = false
                }
                Log.i("EventCategoryDataAdapter", ISTE_BASE_URL + item.posterSlug)
                val url = getImageUrl(item.posterSlug)
                if(item.posterSlug==null)
                    categoryPosterImage.isVisible=false
                if (url != null) {
                    GlideApp.with(itemView)
                        .load(url)
                        .transform(MultiTransformation(CenterCrop(),RoundedCorners(4)))
                        .placeholder(circularProgressDrawable)
                        .error(R.drawable.blog_image_error)
                        .transition(DrawableTransitionOptions().crossFade())
                        .into(categoryPosterImage)
                    categoryPosterImage.isVisible=true
                } else
                    categoryPosterImage.isVisible=false

            }
        }

        private fun getImageUrl(url: String?): String? {
            var finalUrl: String
            if (url != null) {
                if (url.contains("media"))
                    finalUrl = ISTE_BASE_URL + url
                else
                    finalUrl = ISTE_BASE_URL + "/media" + url
                return finalUrl
            } else
                return null
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

    //interface to handle onClicks
    interface onCategoryItemClickListener {
        fun onItemClick(id: Int)
    }
}

//DiffUtil class to calculate difference between the items appearing on screen
class EventCategoryDiffUtilCallBack : DiffUtil.ItemCallback<Category>() {
    override fun areItemsTheSame(oldItem: Category, newItem: Category) =
        (oldItem.nameSlug == newItem.nameSlug)

    override fun areContentsTheSame(oldItem: Category, newItem: Category) = (oldItem == newItem)

}