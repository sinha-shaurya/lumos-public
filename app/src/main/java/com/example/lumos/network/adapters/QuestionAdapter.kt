package com.example.lumos.network.adapters


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.lumos.databinding.QuestionItemBinding
import com.example.lumos.network.dataclasses.practice.Question

class QuestionAdapter(private val listener:onQuestionItemClickListener) :
    ListAdapter<Question, QuestionAdapter.QuestionViewHolder>(QuestionDiffUtilCallback()) {
    inner class QuestionViewHolder(private val binding: QuestionItemBinding) :
        RecyclerView.ViewHolder(binding.root){

            init {
                binding.root.setOnClickListener {
                    val position=bindingAdapterPosition
                    if(position!=RecyclerView.NO_POSITION){
                        val item=getItem(position)
                        if(item!=null)
                            listener.onQuestionItemClick(item,position)
                    }
                }
            }
            fun bind(item:Question){
                binding.questionText.text=item.question
                binding.apply {
                    questionText.text=item.question
                    questionAuthorName.text=item.mentor.name
                    questionAuthorCompany.text=item.mentor.company
                }
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

    interface onQuestionItemClickListener{
        fun onQuestionItemClick(item:Question,position: Int)
    }

}

class QuestionDiffUtilCallback() : DiffUtil.ItemCallback<Question>() {
    override fun areItemsTheSame(oldItem: Question, newItem: Question) =
        oldItem.primaryKey == newItem.primaryKey

    override fun areContentsTheSame(oldItem: Question, newItem: Question) =
        oldItem == newItem

}