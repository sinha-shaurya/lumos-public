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
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lumos.R
import com.example.lumos.databinding.FragmentQuestionBinding
import com.example.lumos.local.UserDatabase
import com.example.lumos.network.adapters.QuestionAdapter
import com.example.lumos.repository.NetworkRepository
import com.example.lumos.utils.LoadingStatus
import com.example.lumos.utils.LoginStatus
import com.example.lumos.utils.LoginViewModelFactory
import com.example.lumos.utils.QuestionViewModelFactory
import com.example.lumos.viewmodel.LoginViewModel
import com.example.lumos.viewmodel.QuestionViewModel

class QuestionFragment : Fragment() {
    private val viewModel: LoginViewModel by activityViewModels {
        LoginViewModelFactory(
            NetworkRepository(
                UserDatabase.getDatabase(requireActivity()).userDao()
            )
        )
    }
    private var _binding: FragmentQuestionBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = DataBindingUtil.inflate<FragmentQuestionBinding>(
            inflater,
            R.layout.fragment_question,
            container,
            false
        )
        val view = binding.root
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    @Suppress("WHEN_ENUM_CAN_BE_NULL_IN_JAVA")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.loginStatus.observe(viewLifecycleOwner){status->
            when(status){
                //only attempt loading questions when user is logged in
                LoginStatus.SUCCESS->{
                    viewModel.getQuestions()
                    //observe for changes in question status
                    questionStatusObserver()
                }
                //display loading status when login status is being fetched
                LoginStatus.LOADING-> binding.apply {
                        questionLoadingProgress.isVisible=true
                        binding.retryButtonQuestion.isVisible=false
                        binding.questionList.isVisible=false
                    }

                else->{
                    binding.apply {
                        questionLoadingProgress.isVisible=false
                        retryButtonQuestion.isVisible=false
                        questionList.isVisible=false
                        Toast.makeText(requireActivity(),"try loggin in",Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
        binding.retryButtonQuestion.setOnClickListener {
            viewModel.getQuestions()
            viewModel.questionStatus.value=LoadingStatus.LOADING
        }
    }

    private fun questionStatusObserver(){
        viewModel.questionStatus.observe(viewLifecycleOwner){
            when(it){
                LoadingStatus.SUCCESS->{
                    //setup recyclerview
                    recyclerViewSetup()
                    Toast.makeText(requireActivity(),"${viewModel.questionResponse.value!!.questionList!!.size} loaded",Toast.LENGTH_SHORT).show()
                    binding.questionList.isVisible=true
                    binding.questionLoadingProgress.isVisible=false
                    binding.retryButtonQuestion.isVisible=false
                }
                LoadingStatus.LOADING->binding.apply {
                    questionLoadingProgress.isVisible=true
                    retryButtonQuestion.isVisible=false
                }
                LoadingStatus.FAILURE-> binding.apply {
                    retryButtonQuestion.isVisible=true
                    questionLoadingProgress.isVisible=false
                }
            }
        }
    }

    private fun recyclerViewSetup(){
        val adapter=QuestionAdapter()
        binding.apply {
            questionList.layoutManager=LinearLayoutManager(requireActivity())
            questionList.adapter=adapter
        }
        val list=viewModel.questionResponse.value?.questionList
        adapter.submitList(list)
    }

}