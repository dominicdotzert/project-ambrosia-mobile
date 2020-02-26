package com.projectambrosia.ambrosia.tasks

import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.projectambrosia.ambrosia.R
import com.projectambrosia.ambrosia.data.models.Task
import com.projectambrosia.ambrosia.databinding.DialogActionTaskEntryBinding
import com.projectambrosia.ambrosia.databinding.FragmentTasksBinding
import com.projectambrosia.ambrosia.network.AmbrosiaApi
import com.projectambrosia.ambrosia.network.RequestManager
import com.projectambrosia.ambrosia.network.models.ResponseData
import com.projectambrosia.ambrosia.network.models.auth.DataUserDetails
import com.projectambrosia.ambrosia.utilities.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class TasksFragment : Fragment() {

    private val tasksViewModel: TasksViewModel by viewModels { TasksViewModelFactory(requireActivity().application) }

    private var dialogOpen = false

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
            TaskListener { task -> task?.let { navigateToTask(it) } }
        )
        binding.homeTodoList.adapter = todoAdapter
        tasksViewModel.todoList.observe(viewLifecycleOwner, Observer {tasks ->
            todoAdapter.addDatesAndSubmitList(tasks, false)
        })

        val completedAdapter = TaskAdapter(
            TaskListener { }
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.debug_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.debug_menu_user_details -> {
                val requestManager = RequestManager.getInstance(requireContext())
                val prefs = PreferencesHelper.getInstance(requireContext())
                CoroutineScope(Job() + Dispatchers.Main).launch {
                    val result = requestManager.makeRequestWithAuth {
                        AmbrosiaApi.retrofitService.getUserDetailsAsync(prefs.accessToken!!)
                    }

                    if (result is ResponseData<*>) {
                        val data = result.data as DataUserDetails
                        Toast.makeText(requireContext(), "uuid: ${data.uuid}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            R.id.debug_menu_get_tasks -> tasksViewModel.requestTasks()
            R.id.debug_menu_logout -> tasksViewModel.logUserOut()
        }

        return super.onOptionsItemSelected(item)
    }

    // TODO: Update to use events to trigger navigation instead
    private fun navigateToTask(task: Task) {
        when (task.tool) {
            IEAS -> this.findNavController().navigate(TasksFragmentDirections.actionTasksFragmentToIEASInstructionsFragment(task.taskId))
            JOURNAL -> this.findNavController().navigate(TasksFragmentDirections.actionTasksFragmentToJournalFragment().setTaskId(task.taskId))
            HUNGER_SCALE -> this.findNavController().navigate(R.id.hungerScaleFragment)
            OTHER -> if (!dialogOpen) showActionTaskDialog(task)
        }
    }

    private fun showActionTaskDialog(task: Task) {
        dialogOpen = true

            val dialogBuilder = AlertDialog.Builder(requireActivity())
            val dialogBinding = DialogActionTaskEntryBinding.inflate(layoutInflater)
            dialogBuilder.setView(dialogBinding.root)

            val dialog = dialogBuilder.create()
            dialog.setCanceledOnTouchOutside(false)
            dialog.window?.apply {
                setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                attributes.windowAnimations = R.style.DialogAnimation
            }
            dialog.show()

            dialogBinding.task = task
            dialogBinding.taskDialogCancelButton.setOnClickListener {
                dialog.dismiss()
            }
            dialogBinding.taskDialogSaveButton.setOnClickListener {
                tasksViewModel.markTaskAsComplete(task.taskId)
                if (dialogBinding.taskDialogEntry.text.isNotBlank()) {
                    tasksViewModel.saveReflectiveEntry(task, dialogBinding.taskDialogEntry.text.toString())
                }
                dialog.dismiss()
            }

            dialog.setOnDismissListener {
                dialogOpen = false
        }
    }
}