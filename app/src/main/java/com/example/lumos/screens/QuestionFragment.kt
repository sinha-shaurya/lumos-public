package com.example.lumos.screens

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lumos.R
import com.example.lumos.databinding.FragmentQuestionBinding
import com.example.lumos.local.UserDatabase
import com.example.lumos.network.adapters.QuestionAdapter
import com.example.lumos.network.dataclasses.practice.Question
import com.example.lumos.repository.NetworkRepository
import com.example.lumos.utils.LoadingStatus
import com.example.lumos.utils.LoginStatus
import com.example.lumos.utils.viewmodelfactory.LoginViewModelFactory
import com.example.lumos.viewmodel.LoginViewModel

class QuestionFragment : Fragment(), QuestionAdapter.onQuestionItemClickListener {
    private val viewModel: LoginViewModel by activityViewModels {
        LoginViewModelFactory(
            NetworkRepository(
                UserDatabase.getDatabase(requireActivity()).userDao()
            )
        )
    }

    val adapter: QuestionAdapter = QuestionAdapter(this)

    //lateinit var adapter: QuestionAdapter
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
        binding.questionList.adapter = null
        _binding = null
    }

    @Suppress("WHEN_ENUM_CAN_BE_NULL_IN_JAVA")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            questionList.layoutManager = LinearLayoutManager(requireActivity())
            questionList.adapter = adapter
        }


        /*
        Setup observer for loginStatus
         */

        viewModel.loginStatus.observe(viewLifecycleOwner) { status ->
            when (status) {
                //only attempt loading questions when user is logged in
                LoginStatus.SUCCESS -> {
                    viewModel.getQuestions()
                    binding.questionRefresh.isEnabled = true
                    binding.notLoggedInText.isVisible = false
                    //observe for changes in question status
                    questionStatusObserver()
                }
                //display loading status when login status is being fetched
                LoginStatus.LOADING -> binding.apply {
                    questionLoadingProgress.isVisible = true
                    binding.retryButtonQuestion.isVisible = false
                    binding.questionList.isVisible = false
                    binding.notLoggedInText.isVisible = false
                }

                else -> {
                    binding.apply {
                        questionRefresh.isEnabled = false
                        questionLoadingProgress.isVisible = false
                        retryButtonQuestion.isVisible = false
                        questionList.isVisible = false
                        binding.notLoggedInText.isVisible = true

                    }
                }
            }
        }
        //setup retry button to fetch questions in case of any error
        binding.retryButtonQuestion.setOnClickListener {
            viewModel.getQuestions()
            viewModel.questionStatus.value = LoadingStatus.LOADING
        }

        //enable swipe to refresh
        binding.questionRefresh.setOnRefreshListener {
            Log.i(TAG, "SwipeRefresh called")
            viewModel.refreshQuestionList()
        }
    }

    @Suppress("WHEN_ENUM_CAN_BE_NULL_IN_JAVA")
    private fun questionStatusObserver() {
        //val adapter:QuestionAdapter = QuestionAdapter(this)
        viewModel.questionStatus.observe(viewLifecycleOwner) { it ->
            when (it) {
                LoadingStatus.SUCCESS -> {
                    //binding.questionList.adapter=adapter

                    viewModel.questionList.observe(viewLifecycleOwner) {
                        Log.i("QuestionFragment", "Size of list received ${it.size}")
                        adapter.submitList(it)
                        binding.noQuestionsText.isVisible = it.isEmpty()
                    }

                    binding.apply {
                        questionList.isVisible = true
                        questionLoadingProgress.isVisible = false
                        retryButtonQuestion.isVisible = false
                        questionRefresh.isRefreshing = false
                    }

                }
                LoadingStatus.LOADING -> binding.apply {
                    questionLoadingProgress.isVisible = true
                    noQuestionsText.isVisible = false
                    retryButtonQuestion.isVisible = false
                }
                LoadingStatus.FAILURE -> binding.apply {
                    Log.i("QuestionFragment", "Exception occurred")
                    retryButtonQuestion.isVisible = true
                    questionLoadingProgress.isVisible = false
                    noQuestionsText.isVisible = false
                    questionRefresh.isRefreshing = false
                    val error = viewModel.questionError.value
                    if (error != null)
                        Log.i("QuestionFragment", error.message ?: "Default exception")
                }
            }
        }
    }


    /* onClickHandler for every question clicked

     */
    override fun onQuestionItemClick(item: Question, position: Int) {
        Log.i(
            "QuestionFragment",
            "Size of list ${viewModel.questionResponse.value?.questionList?.size}"
        )
        //trigger answer request
        val action = QuestionFragmentDirections.actionQuestionFragmentToAnswerFragment(item)
        findNavController().navigate(action)
    }

    companion object {
        private const val TAG = "QuestionFragment"
    }
}