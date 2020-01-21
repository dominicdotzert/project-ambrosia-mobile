package com.projectambrosia.ambrosia.tasks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.projectambrosia.ambrosia.R
import com.projectambrosia.ambrosia.data.Task
import com.projectambrosia.ambrosia.databinding.FragmentTasksBinding
import com.projectambrosia.ambrosia.utilities.Tool
import timber.log.Timber

class TasksFragment : Fragment() {

    private lateinit var tasksViewModel: TasksViewModel
    private lateinit var binding: FragmentTasksBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        tasksViewModel = ViewModelProviders.of(this).get(TasksViewModel::class.java)

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_tasks, container, false)
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
                Tool.JOURNAL -> Toast.makeText(activity, "Journal Tool", Toast.LENGTH_SHORT).show()
                Tool.HS -> Toast.makeText(activity, "Hunger Scale Tool", Toast.LENGTH_SHORT).show()
                Tool.IEAS -> Toast.makeText(activity, "IEAS", Toast.LENGTH_SHORT).show()
                Tool.OTHER -> Toast.makeText(activity, "Other", Toast.LENGTH_SHORT).show()
            }
        }
    }
}