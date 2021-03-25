package com.example.lumos.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.navOptions
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lumos.R
import com.example.lumos.databinding.FragmentCategoryEventBinding
import com.example.lumos.local.UserDatabase
import com.example.lumos.network.adapters.EventListAdapter
import com.example.lumos.repository.NetworkRepository
import com.example.lumos.utils.viewmodelfactory.CategoryViewModelFactory
import com.example.lumos.viewmodel.CategoryViewModel

class CategoryEventFragment : Fragment() {


    private var _binding: FragmentCategoryEventBinding? = null
    val binding get() = _binding!!

    //setup navigation arguments
    val args: CategoryEventFragmentArgs by navArgs()

    //setup viewmodel
    private val viewModel by activityViewModels<CategoryViewModel> {
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
        _binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_category_event, container, false)
        val view = binding.root
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = EventListAdapter()
        //setup recyclerview
        binding.apply {
            eventList.layoutManager = LinearLayoutManager(requireActivity())
            eventList.adapter = adapter
        }
        val list = viewModel.categoryList.value!!.activeCategory[args.categoryId].events
        adapter.submitList(list)
    }

}