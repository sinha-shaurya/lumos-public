package com.example.lumos.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lumos.R
import com.example.lumos.databinding.FragmentEventBinding
import com.example.lumos.local.UserDatabase
import com.example.lumos.network.adapters.EventCategoryDataAdapter
import com.example.lumos.repository.NetworkRepository
import com.example.lumos.utils.CategoryViewModelFactory
import com.example.lumos.utils.LoadingStatus
import com.example.lumos.viewmodel.CategoryViewModel

class EventFragment : Fragment(), EventCategoryDataAdapter.onCategoryItemClickListener {

    private var _binding: FragmentEventBinding? = null
    val binding get() = _binding!!
    private val viewModel: CategoryViewModel by activityViewModels<CategoryViewModel> {
        CategoryViewModelFactory(
            NetworkRepository(
                UserDatabase.getDatabase(requireActivity()).userDao()
            )
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = DataBindingUtil.inflate<FragmentEventBinding>(
            inflater,
            R.layout.fragment_event,
            container,
            false
        )
        val view = binding.root
        return view
    }

    @Suppress("WHEN_ENUM_CAN_BE_NULL_IN_JAVA")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = EventCategoryDataAdapter(this)
        //setup recyclerview
        binding.apply {
            categoryList.setHasFixedSize(true)
            categoryList.adapter = adapter
            categoryList.layoutManager = LinearLayoutManager(requireActivity())

        }
        //setup observer for loading status
        viewModel.loadingStatus.observe(viewLifecycleOwner) {
            when (it) {
                //When request is successful display the category list
                LoadingStatus.SUCCESS -> {
                    adapter.submitList(viewModel.categoryList.value?.activeCategory ?: emptyList())
                    binding.apply {
                        eventLoadingProgress.isVisible = false
                        retryButtonEvents.isVisible = false
                        categoryList.isVisible = true
                    }
                }
                //when request has failed, show the retry button
                LoadingStatus.FAILURE -> {
                    //handle failure
                    Toast.makeText(
                        requireActivity(),
                        viewModel.error.value ?: "Error",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                    binding.apply {
                        eventLoadingProgress.isVisible = false
                        retryButtonEvents.isVisible = true
                        categoryList.isVisible = false
                    }
                }
                //when request is being processed , display the progress bar
                LoadingStatus.LOADING -> {
                    binding.apply {
                        eventLoadingProgress.isVisible = true
                        categoryList.isVisible = false
                        retryButtonEvents.isVisible = false
                    }
                }
            }
            //setup on click listener for retry button
            binding.retryButtonEvents.setOnClickListener {
                viewModel.resetToLoading()
                viewModel.getList()
            }
        }
    }

    override fun onItemClick(id: Int) {
        Toast.makeText(requireActivity(), id.toString(), Toast.LENGTH_SHORT).show()
        val action=EventFragmentDirections.actionEventFragmentToCategoryEventFragment(id)
        findNavController().navigate(action)
    }

}