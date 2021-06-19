package com.example.lumos.utils.developers

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.lumos.R
import com.example.lumos.databinding.DeveloperItemBinding
import com.example.lumos.utils.GlideApp

class DeveloperListAdapter(private val listener: onLinkClickListener) :
    ListAdapter<Developer, DeveloperListAdapter.DeveloperItemViewHolder>(DeveloperDiffUtilCallback()) {

    inner class DeveloperItemViewHolder(val binding: DeveloperItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Developer) {
            binding.apply {
                developerName.text = item.name
                developerRole.text = item.role
                githubLinkIcon.isVisible = item.gitHub.isNotEmpty()
                linkedinLinkIcon.isVisible = item.linkedIn.isNotEmpty()
                instagramLinkIcon.isVisible = item.instagram.isNotEmpty()
                personalWebsiteLinkIcon.isVisible = item.personalWebsite.isNotEmpty()
                twitterLinkIcon.isVisible = item.twitter.isNotEmpty()
                emailLinkIcon.isVisible = item.email.isNotEmpty()
                loadImage(item.name)
                onLinkClickListener()
            }
        }

        private fun loadImage(item: String) {
            var id = R.drawable.user_account
            if (item.contains("Shaurya", ignoreCase = true)) id = R.drawable.shaurya
            else if (item.contains("Omkar", ignoreCase = true)) id = R.drawable.omkar
            else if (item.contains("Shubham", ignoreCase = true)) id = R.drawable.shubham
            else if (item.contains("Shravya", ignoreCase = true)) id = R.drawable.shravya
            else if (item.contains("Tinku", ignoreCase = true)) id = R.drawable.tinku

            GlideApp.with(itemView)
                .load(id)
                .transform(
                    MultiTransformation(
                        CenterCrop(),
                        CircleCrop()
                    )
                )
                .error(R.drawable.blog_image_error)
                .transition(DrawableTransitionOptions().crossFade())
                .into(binding.developerImage)
        }

        private fun onLinkClickListener() {
            val position = bindingAdapterPosition
            if (position != RecyclerView.NO_POSITION) {
                val item = getItem(position)
                if (item != null) {
                    //start setting up onClick Listeners

                    binding.githubLinkIcon.setOnClickListener { listener.onLinkClick(item.gitHub) }
                    binding.linkedinLinkIcon.setOnClickListener { listener.onLinkClick(item.linkedIn) }
                    binding.instagramLinkIcon.setOnClickListener { listener.onLinkClick(item.instagram) }
                    binding.personalWebsiteLinkIcon.setOnClickListener { listener.onLinkClick(item.personalWebsite) }
                    binding.twitterLinkIcon.setOnClickListener { listener.onLinkClick(item.twitter) }
                    binding.emailLinkIcon.setOnClickListener {
                        listener.onLinkClick(
                            link = item.email,
                            linkType = "email"
                        )
                    }
                }
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeveloperItemViewHolder {
        val binding =
            DeveloperItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DeveloperItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DeveloperItemViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    interface onLinkClickListener {
        fun onLinkClick(link: String, linkType: String = "website")
    }
}

class DeveloperDiffUtilCallback : DiffUtil.ItemCallback<Developer>() {
    override fun areItemsTheSame(oldItem: Developer, newItem: Developer): Boolean =
        oldItem.name == newItem.name

    override fun areContentsTheSame(oldItem: Developer, newItem: Developer): Boolean =
        oldItem == newItem

}