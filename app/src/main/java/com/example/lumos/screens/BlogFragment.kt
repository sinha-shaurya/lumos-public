package com.example.lumos.screens

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lumos.R
import com.example.lumos.databinding.FragmentBlogBinding
import com.example.lumos.network.adapters.BlogDataAdapter
import com.example.lumos.network.adapters.BlogLoadStateAdapter
import com.example.lumos.repository.BlogRepository
import com.example.lumos.utils.BlogViewModelFactory
import com.example.lumos.viewmodel.BlogViewModel

class BlogFragment : Fragment(), BlogDataAdapter.onItemClickListener {

    private var _binding: FragmentBlogBinding? = null
    private val binding get() = _binding!!



    private val viewModel:BlogViewModel by activityViewModels<BlogViewModel>{
        BlogViewModelFactory(BlogRepository())
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = DataBindingUtil.inflate<FragmentBlogBinding>(
            inflater,
            R.layout.fragment_blog,
            container,
            false
        )
        BlogViewModelFactory(BlogRepository())

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        //_binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = BlogDataAdapter(this)
        binding.apply {
            blogList.setHasFixedSize(true)
            blogList.adapter =
                adapter.withLoadStateFooter(footer = BlogLoadStateAdapter { adapter.retry() })
            blogList.layoutManager = LinearLayoutManager(requireActivity())

        }
        viewModel.blogPost.observe(viewLifecycleOwner) {
            adapter.submitData(viewLifecycleOwner.lifecycle, it)
        }
        //setup retry button and progress bar
        adapter.addLoadStateListener { combinedLoadStates ->
            binding.apply {
                blogProgress.isVisible = combinedLoadStates.source.refresh is LoadState.Loading
                blogList.isVisible = combinedLoadStates.source.refresh is LoadState.NotLoading
                retryButtonBlog.isVisible = combinedLoadStates.source.refresh is LoadState.Error
                errorTextBlogList.isVisible = combinedLoadStates.source.refresh is LoadState.Error
                //latestStoriesText.isVisible=combinedLoadStates.source.refresh is LoadState.NotLoading

                if (errorTextBlogList.isVisible)
                    errorTextBlogList.text = "An error occured"
            }
            //setup onClick for retry button
            binding.retryButtonBlog.setOnClickListener {
                adapter.refresh()
            }
        }

    }

    override fun onItemClick(id: String) {
        val url = BLOG_BASE_URL + id
        val builder: CustomTabsIntent.Builder = CustomTabsIntent.Builder()
        val customTabsIntent = builder.build()
        customTabsIntent.launchUrl(requireActivity(), Uri.parse(url))
    }

    companion object {
        const val BLOG_BASE_URL = "https://blog.istemanipal.com/articles/single//"
    }
}