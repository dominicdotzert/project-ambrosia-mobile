package com.projectambrosia.ambrosia.ieas

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.projectambrosia.ambrosia.databinding.ListItemIeasQuestionBinding

class IEASQuestionsAdapter(private val questionListener: IEASQuestionClickListener) : ListAdapter<IEASQuestion, IEASQuestionsAdapter.ViewHolder>(IEASQuestionDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), questionListener)
    }

    class ViewHolder private constructor(val binding: ListItemIeasQuestionBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: IEASQuestion, questionListener: IEASQuestionClickListener) {
            binding.ieasQuestion = item
            binding.questionListener = questionListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemIeasQuestionBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}

class IEASQuestionDiffCallback : DiffUtil.ItemCallback<IEASQuestion>() {
    override fun areItemsTheSame(oldItem: IEASQuestion, newItem: IEASQuestion): Boolean {
        return oldItem.question == newItem.question
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: IEASQuestion, newItem: IEASQuestion): Boolean {
        return oldItem == newItem
    }
}

class IEASQuestionClickListener(val clickListener: (question: CheckBox) -> Unit) {
    fun onClick(question: CheckBox) = clickListener(question)
}