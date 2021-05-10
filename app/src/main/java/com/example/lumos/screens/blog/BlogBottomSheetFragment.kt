package com.example.lumos.screens.blog

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.example.lumos.R
import com.example.lumos.databinding.BlogBottomSheetBinding
import com.example.lumos.local.UserDatabase
import com.example.lumos.repository.BlogRepository
import com.example.lumos.utils.viewmodelfactory.BlogViewModelFactory
import com.example.lumos.viewmodel.BlogViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BlogBottomSheetFragment : BottomSheetDialogFragment() {

    val viewModel by activityViewModels<BlogViewModel> {
        BlogViewModelFactory(BlogRepository(UserDatabase.getDatabase(requireActivity()).userDao()))
    }

    private var _binding: BlogBottomSheetBinding? = null
    val binding
        get() = _binding!!

    val args by navArgs<BlogBottomSheetFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DataBindingUtil.inflate(inflater, R.layout.blog_bottom_sheet, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.blogTitle.text = args.post.title
        viewModel.postId.value = args.post.id
        val post = args.post
        viewModel.searchPost()
        viewModel.exists.observe(viewLifecycleOwner) {
            when (it) {
                true -> {
                    binding.bookmarkButton.setOnClickListener {
                        viewModel.deletePost(post)
                    }
                    binding.bookmarkButton.setIconResource(R.drawable.bookmark_filled_icon)
                    binding.bookmarkButton.text = "Bookmarked"
                }
                false -> {
                    binding.bookmarkButton.setOnClickListener {
                        viewModel.savePost(post)
                    }
                    binding.bookmarkButton.setIconResource(R.drawable.bookmark_icon)
                    binding.bookmarkButton.text = "Bookmark"
                }
            }
        }

        binding.shareButton.setOnClickListener {
            createShareIntent()
        }

        val authorDetails = "Written by ${post.author},${post.aboutAuthor}"
        val description = post.descriptionShort
        binding.authorName.text = authorDetails
        binding.postDescription.text = description
        binding.aboutButton.setOnClickListener {
            binding.apply {
                if (this.authorName.isVisible) {
                    this.postDescription.isVisible = false
                    this.authorName.isVisible = false
                } else {
                    this.postDescription.isVisible = true
                    this.authorName.isVisible = true
                }
            }
        }
    }

    //method to share URL of blog post using Android's share sheet
    private fun createShareIntent() {
        val url = "https://blog.istemanipal.com/articles/single//${args.post.id}"
        Log.d(TAG, url)
        val share = Intent.createChooser(Intent().apply {
            action = Intent.ACTION_SEND
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, url)
            putExtra(
                Intent.EXTRA_TITLE,
                args.post.title
            )//put title of post as title of android share sheet
            flags = Intent.FLAG_GRANT_READ_URI_PERMISSION

        }, null)
        startActivity(share)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        const val TAG = "BlogBottomSheetFragment"
    }
}