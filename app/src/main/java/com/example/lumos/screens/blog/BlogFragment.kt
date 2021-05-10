package com.example.lumos.screens.blog

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lumos.R
import com.example.lumos.databinding.FragmentBlogBinding
import com.example.lumos.local.UserDatabase
import com.example.lumos.network.adapters.BlogDataAdapter
import com.example.lumos.network.adapters.BlogLoadStateAdapter
import com.example.lumos.network.dataclasses.blog.BlogPost
import com.example.lumos.repository.BlogRepository
import com.example.lumos.utils.viewmodelfactory.BlogViewModelFactory
import com.example.lumos.viewmodel.BlogViewModel
import com.example.lumos.viewmodel.ToolbarTitleViewModel

class BlogFragment : Fragment(), BlogDataAdapter.onItemClickListener {

    private var _binding: FragmentBlogBinding? = null
    private val binding get() = _binding!!


    private val viewModel: BlogViewModel by activityViewModels {
        BlogViewModelFactory(BlogRepository(UserDatabase.getDatabase(requireActivity()).userDao()))
    }

    private val toolbarTitleViewModel: ToolbarTitleViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_blog,
            container,
            false
        )
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = BlogDataAdapter(this)

        binding.blogList.apply {
            this.adapter =
                adapter.withLoadStateFooter(footer = BlogLoadStateAdapter { adapter.retry() })
            val layoutManager = LinearLayoutManager(this.context)
            this.layoutManager = layoutManager
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

    /**
     * Handle ItemClick for each item in recyclerview
     * Launch Intent, and send item received
     */
    override fun onItemClick(item: BlogPost) {
        val id = item.id
        /*
        val url = BLOG_BASE_URL + id
        val builder: CustomTabsIntent.Builder = CustomTabsIntent.Builder()
        val customTabsIntent = builder.build()

        customTabsIntent.launchUrl(requireActivity(), Uri.parse(url))

         */

        val intent = Intent(requireActivity(), BlogArticleViewActivity::class.java)
        intent.putExtra("postItem", item)
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(activity).toBundle())
    }

    override fun onMenuItemClick(item: BlogPost) {
        val args = item
        val action = BlogFragmentDirections.actionBlogFragmentToBlogBottomSheetFragment(args)
        findNavController().navigate(action)
    }

    override fun onStart() {
        super.onStart()
        toolbarTitleViewModel.changeTitle("Technical Prophet.")
    }

    companion object {
        const val BLOG_BASE_URL = "https://blog.istemanipal.com/articles/single//"
    }
}