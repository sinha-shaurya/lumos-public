package com.example.lumos.screens.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.lumos.R
import com.example.lumos.databinding.FragmentAccountQuestionsBinding
import com.example.lumos.local.UserDatabase
import com.example.lumos.network.adapters.AnswerListAdapter
import com.example.lumos.repository.NetworkRepository
import com.example.lumos.utils.viewmodelfactory.QuestionViewModelFactory
import com.example.lumos.viewmodel.QuestionViewModel
import com.example.lumos.viewmodel.ToolbarTitleViewModel


class AccountQuestions : Fragment() {

    private val viewModel: QuestionViewModel by activityViewModels {
        QuestionViewModelFactory(
            NetworkRepository(
                UserDatabase.getDatabase(requireActivity()).userDao()
            )
        )
    }

    private val toolbarTitleViewModel: ToolbarTitleViewModel by activityViewModels()

    //data binding variables
    private var _binding: FragmentAccountQuestionsBinding? = null
    private val binding get() = _binding!!

    val adapter = AnswerListAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_account_questions, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.answerList.adapter = null
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //setup recyclerview
        binding.answerList.adapter = adapter


        viewModel.submittedAnswers.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }

    override fun onStart() {
        super.onStart()
        toolbarTitleViewModel.changeTitle("Submissions .")
    }
}