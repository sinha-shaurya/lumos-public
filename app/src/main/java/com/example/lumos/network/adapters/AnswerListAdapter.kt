package com.example.lumos.network.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.lumos.databinding.AnswerItemBinding
import com.example.lumos.network.dataclasses.practice.AnsweredQuestion

/**
 * Adapter for showing list of answered questions.
 * Takes in a List of objects of class [AnsweredQuestion]
 */
class AnswerListAdapter(private val listener: onAnsweredQuestionClickListener) :
    ListAdapter<AnsweredQuestion, AnswerListAdapter.AnswerViewHolder>(
        AnswerDiffUtilCallback()
    ) {
    inner class AnswerViewHolder(val binding: AnswerItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: AnsweredQuestion) {
            binding.apply {
                questionText.text = item.question.question
                answerText.text = item.answerSubmit
                expectedAnswerText.text = item.question.expectedAnswer
                val pointText = "${item.points ?: 0} Points"
                points.text = pointText
            }

            //set onClick for the button
            binding.readMoreButton.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val answeredQuestionItem = getItem(position)
                    if (answeredQuestionItem != null)
                        listener.buttonItemClick(answeredQuestionItem)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnswerViewHolder {
        val binding =
            AnswerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AnswerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AnswerViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    interface onAnsweredQuestionClickListener {
        fun buttonItemClick(item: AnsweredQuestion)
    }
}

class AnswerDiffUtilCallback : DiffUtil.ItemCallback<AnsweredQuestion>() {
    override fun areItemsTheSame(oldItem: AnsweredQuestion, newItem: AnsweredQuestion): Boolean =
        oldItem.question.equals(newItem.question)


    override fun areContentsTheSame(oldItem: AnsweredQuestion, newItem: AnsweredQuestion): Boolean =
        oldItem == newItem
}