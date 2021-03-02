package com.example.lumos.network.adapters


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.lumos.databinding.QuestionItemBinding
import com.example.lumos.network.dataclasses.practice.Question

class QuestionAdapter :
    ListAdapter<Question, QuestionAdapter.QuestionViewHolder>(QuestionDiffUtilCallback()) {
    inner class QuestionViewHolder(private val binding: QuestionItemBinding) :
        RecyclerView.ViewHolder(binding.root){

            fun bind(item:Question){
                binding.questionText.text=item.question
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionViewHolder {
        val binding=QuestionItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return QuestionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: QuestionViewHolder, position: Int) {
        val item=getItem(position)
        holder.bind(item)
    }
}

class QuestionDiffUtilCallback : DiffUtil.ItemCallback<Question>() {
    override fun areItemsTheSame(oldItem: Question, newItem: Question) =
        oldItem.primaryKey == newItem.primaryKey

    override fun areContentsTheSame(oldItem: Question, newItem: Question) =
        oldItem == newItem

}