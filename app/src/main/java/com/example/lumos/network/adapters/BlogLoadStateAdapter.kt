package com.example.lumos.network.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.lumos.databinding.BlogFooterBinding

class BlogLoadStateAdapter(private val retry: () -> Unit) :
    LoadStateAdapter<BlogLoadStateAdapter.BlogLoadStateViewHolder>() {

    inner class BlogLoadStateViewHolder(private val binding: BlogFooterBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.footerRetryButton.setOnClickListener {
                retry.invoke()
            }
        }

        fun bind(loadState: LoadState) {
            binding.apply {
                blogLoadingProgress.isVisible = loadState is LoadState.Loading
                footerRetryButton.isVisible = loadState !is LoadState.Loading
                blogErrorText.isVisible = loadState !is LoadState.Loading
                if (blogErrorText.isVisible)
                    blogErrorText.text = "An error occurred"
            }
        }
    }

    override fun onBindViewHolder(holder: BlogLoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): BlogLoadStateViewHolder {
        val binding = BlogFooterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BlogLoadStateViewHolder(binding)
    }
}