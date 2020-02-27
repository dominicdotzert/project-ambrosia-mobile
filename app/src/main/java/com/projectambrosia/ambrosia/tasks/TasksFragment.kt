package com.projectambrosia.ambrosia.tasks

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.projectambrosia.ambrosia.R
import com.projectambrosia.ambrosia.data.models.Task
import com.projectambrosia.ambrosia.databinding.FragmentTasksBinding
import com.projectambrosia.ambrosia.network.AmbrosiaApi
import com.projectambrosia.ambrosia.network.RequestManager
import com.projectambrosia.ambrosia.network.models.ResponseUserDetails
import com.projectambrosia.ambrosia.utilities.PreferencesHelper
import com.projectambrosia.ambrosia.utilities.Tool
import kotlinx.coroutines.*

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
        val todoAdapter = TaskAdapter(
            TaskListener { task -> task?.taskId?.let { tasksViewModel.markTaskAsComplete(it) } },
            TaskListener { task -> task?.let { navigateToTask(it) } }
        )
        binding.homeTodoList.adapter = todoAdapter
        tasksViewModel.todoList.observe(viewLifecycleOwner, Observer {tasks ->
            todoAdapter.addDatesAndSubmitList(tasks, false)
        })

        val completedAdapter = TaskAdapter(
            TaskListener { task -> task?.let { tasksViewModel.markTaskAsIncomplete(task.taskId) } },
            TaskListener { task -> task?.let { navigateToTask(it) } }
        )
        binding.homeCompletedList.adapter = completedAdapter
        tasksViewModel.completedList.observe(viewLifecycleOwner, Observer { tasks ->
            val todaySelected = tasksViewModel.todaySelected.value ?: true
            completedAdapter.addDatesAndSubmitList(tasks, !todaySelected)
        })

        // Set observer on TodayAllSelector
        binding.tasksTodayAllSelector.todaySelected.observe(viewLifecycleOwner, Observer {
            it?.let {
                tasksViewModel.todaySelected.value = it
            }
        })

        // Set observer on navigation events
        tasksViewModel.navigateToLogin.observe(viewLifecycleOwner, Observer {
            if (it) {
                this.findNavController().navigate(TasksFragmentDirections.actionTasksFragmentToLoginGraph())
                tasksViewModel.doneNavigatingToLogin()
            }
        })

        // Enable options menu
        setHasOptionsMenu(true)

        return binding.root
    }

    // TODO: Update to use events to trigger navigation instead
    private fun navigateToTask(task: Task) {
        when (task.tool) {
            Tool.IEAS -> this.findNavController().navigate(TasksFragmentDirections.actionTasksFragmentToIEASInstructionsFragment(task.taskId))
            Tool.JOURNAL -> this.findNavController().navigate(TasksFragmentDirections.actionTasksFragmentToJournalFragment().setTaskId(task.taskId))
            Tool.HS -> this.findNavController().navigate(R.id.hungerScaleFragment)
            Tool.OTHER -> tasksViewModel.markTaskAsComplete(task.taskId)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.debug_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
//            R.id.debug_menu_user_details -> {
//                val requestManager = RequestManager.getInstance(requireContext())
//                val prefs = PreferencesHelper.getInstance(requireContext())
//                CoroutineScope(Job() + Dispatchers.Main).launch {
//                    val result = requestManager.makeRequestWithAuth {
//                        AmbrosiaApi.retrofitService.getUserDetailsAsync(prefs.accessToken!!)
//                    }
//
//                    if (result is ResponseUserDetails) {
//                        Toast.makeText(requireContext(), "uuid: ${result.data.userId}", Toast.LENGTH_SHORT).show()
//                    }
//                }
//            }
            R.id.debug_menu_refresh_data -> tasksViewModel.debugRefresh()
            R.id.debug_menu_logout -> tasksViewModel.logUserOut()
        }

        return super.onOptionsItemSelected(item)
    }
}