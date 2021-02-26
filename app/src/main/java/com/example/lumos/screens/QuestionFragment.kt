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
import com.example.lumos.R
import com.example.lumos.databinding.FragmentQuestionBinding
import com.example.lumos.local.UserDatabase
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
        viewModel.loginStatus.observe(viewLifecycleOwner){
            when(it){
                LoginStatus.LOADING->binding.retryButtonQuestion.isVisible=false
                LoginStatus.SUCCESS->viewModel.getQuestions()
                LoginStatus.FAILURE->binding.retryButtonQuestion.isVisible=true
                LoginStatus.NOT_LOGGED_IN->Toast.makeText(requireActivity(),"Not logged in",Toast.LENGTH_SHORT).show()
            }
        }
        binding.retryButtonQuestion.setOnClickListener {
            viewModel.getQuestions()
        }
    }
}