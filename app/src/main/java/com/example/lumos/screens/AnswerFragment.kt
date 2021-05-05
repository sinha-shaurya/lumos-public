package com.example.lumos.screens

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.lumos.MainActivity
import com.example.lumos.R
import com.example.lumos.databinding.FragmentAnswerBinding
import com.example.lumos.local.UserDatabase
import com.example.lumos.network.dataclasses.practice.Answer
import com.example.lumos.repository.NetworkRepository
import com.example.lumos.utils.LoadingStatus
import com.example.lumos.utils.viewmodelfactory.LoginViewModelFactory
import com.example.lumos.viewmodel.LoginViewModel


class AnswerFragment : Fragment() {

    private var _binding: FragmentAnswerBinding? = null
    val binding get() = _binding!!

    val args: AnswerFragmentArgs by navArgs()

    //initialise viewmodel
    val viewModel by activityViewModels<LoginViewModel> {
        LoginViewModelFactory(
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
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_answer, container, false)
        val view = binding.root
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val question = args.questionItem
        val authorDetailsText = "By ${question.mentor.name}, in ${question.mentor.company}"
        binding.apply {
            questionText.text = question.question
            questionAuthorName.text = authorDetailsText
        }

        binding.submitButton.setOnClickListener {
            Log.i("AnswerFragment", "Submit button clicked")
            val answerText = binding.answerText.text.toString()
            val primaryKey = question.primaryKey
            if (answerText.isNotEmpty()) {
                val answer = Answer(answerText, primaryKey)
                viewModel.submitAnswer(answer)
            }
            viewModel.setAnswerState(LoadingStatus.LOADING)
            observeAnswerStatus()
        }

    }

    private fun observeAnswerStatus() {
        viewModel.answerStatus.observe(viewLifecycleOwner) { status ->
            @Suppress("WHEN_ENUM_CAN_BE_NULL_IN_JAVA")
            when (status) {
                LoadingStatus.SUCCESS -> {
                    viewModel.removeItem(args.questionItem)
                    val navOptionsBuilder = NavOptionsBuilder()
                    navOptionsBuilder.anim {
                        exit = R.anim.fragment_fade_exit
                    }
                    findNavController().popBackStack(R.id.questionFragment, false)
                }
                LoadingStatus.FAILURE -> {
                    viewModel.answerResponse.observe(viewLifecycleOwner) {
                        if (it.errors.equals("Cannot submit again", ignoreCase = true)) {
                            //user has already submitted
                            viewModel.removeItem(args.questionItem)
                            findNavController().popBackStack(R.id.questionFragment, false)
                            Toast.makeText(
                                requireActivity(),
                                "You have already submitted this answer",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else
                            Toast.makeText(requireActivity(), it.errors, Toast.LENGTH_SHORT).show()
                    }

                }
                LoadingStatus.LOADING -> Toast.makeText(
                    requireActivity(),
                    "Submitting",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}