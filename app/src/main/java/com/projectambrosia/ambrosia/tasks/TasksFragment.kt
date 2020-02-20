package com.projectambrosia.ambrosia.tasks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.projectambrosia.ambrosia.R
import com.projectambrosia.ambrosia.data.models.Task
import com.projectambrosia.ambrosia.databinding.FragmentTasksBinding
import com.projectambrosia.ambrosia.utilities.Tool

class TasksFragment : Fragment() {

    private val tasksViewModel: TasksViewModel by viewModels { TasksViewModelFactory(requireActivity().application) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentTasksBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.taskViewModel = tasksViewModel

        // Set RecyclerView adapters and observe lists
        val todoAdapter = TaskAdapter(TaskListener { task ->
            navigateToTask(task)
        })
        binding.homeTodoList.adapter = todoAdapter
        tasksViewModel.todoList.observe(this, Observer {tasks ->
            todoAdapter.addDatesAndSubmitList(tasks, false)
        })

        val completedAdapter = TaskAdapter(TaskListener { task ->
            task?.let {
                tasksViewModel.markTaskAsIncomplete(task.taskId)
            }
        })
        binding.homeCompletedList.adapter = completedAdapter
        tasksViewModel.completedList.observe(this, Observer { tasks ->
            val todaySelected = tasksViewModel.todaySelected.value ?: true
            completedAdapter.addDatesAndSubmitList(tasks, !todaySelected)
        })

        // Set observer on TodayAllSelector
        binding.tasksTodayAllSelector.todaySelected.observe(this, Observer {
            it?.let {
                tasksViewModel.todaySelected.value = it
            }
        })

        return binding.root
    }

    // TODO: Update to use events to trigger navigation instead
    private fun navigateToTask(task: Task?) {
        task?.let {
            when (task.tool) {
                Tool.IEAS -> this.findNavController().navigate(TasksFragmentDirections.actionTasksFragmentToIEASInstructionsFragment(task.taskId))
                Tool.HS -> this.findNavController().navigate(R.id.hungerScaleFragment)
                Tool.JOURNAL -> this.findNavController().navigate(R.id.journalFragment)
                Tool.OTHER -> tasksViewModel.markTaskAsComplete(task.taskId)
            }
        }
    }
}