package com.example.lumos.screens.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.navArgs
import com.example.lumos.R
import com.example.lumos.databinding.FragmentAnswerDetailsBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class AnswerDetails : BottomSheetDialogFragment() {

    val args by navArgs<AnswerDetailsArgs>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    private var _binding: FragmentAnswerDetailsBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_answer_details, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val answeredQuestion = args.answeredQuestion
        binding.apply {
            questionText.text = answeredQuestion.question.question
            answerText.text = answeredQuestion.answerSubmit
            expectedAnswerText.text = answeredQuestion.question.expectedAnswer
            authorName.text = answeredQuestion.question.mentor.name
        }


        //setup about author
        val aboutAuthor = answeredQuestion.question.mentor.mentorDescription
        binding.aboutAuthor.text =
            if (aboutAuthor.length > 100) "${aboutAuthor.subSequence(0, 100)}..." else aboutAuthor

        binding.readMore.isVisible = aboutAuthor.length > 100
        binding.readMore.setOnClickListener {
            binding.aboutAuthor.text = aboutAuthor
            it.isVisible = false
        }

        binding.closeFragmentIcon.setOnClickListener {
            activity?.onBackPressed()
        }
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()

    }
}