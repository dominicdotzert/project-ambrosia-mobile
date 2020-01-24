package com.projectambrosia.ambrosia.tasks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.projectambrosia.ambrosia.R
import com.projectambrosia.ambrosia.data.Task
import com.projectambrosia.ambrosia.databinding.FragmentTasksBinding
import com.projectambrosia.ambrosia.utilities.Tool

class TasksFragment : Fragment() {

    private val tasksViewModel: TasksViewModel by viewModels()
    private lateinit var binding: FragmentTasksBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTasksBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.taskViewModel = tasksViewModel

        val adapter = TaskAdapter(TaskListener { task ->
            navigateToTask(task)
        })
        binding.homeTodoList.adapter = adapter

        tasksViewModel.todoList.observe(this, Observer {tasks ->
            adapter.submitList(tasks)
        })

        return binding.root
    }

    private fun navigateToTask(task: Task?) {
        task?.let {
            when (task.tool) {
                Tool.JOURNAL -> this.findNavController().navigate(R.id.journalFragment)
                Tool.HS -> this.findNavController().navigate(R.id.hungerScaleFragment)
                Tool.IEAS ->Toast.makeText(activity, "IEAS", Toast.LENGTH_SHORT).show()
                Tool.OTHER -> Toast.makeText(activity, "Other", Toast.LENGTH_SHORT).show()
            }
        }
    }
}