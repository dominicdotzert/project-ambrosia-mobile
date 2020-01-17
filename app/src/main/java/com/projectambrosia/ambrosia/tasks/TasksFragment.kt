package com.projectambrosia.ambrosia.tasks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.projectambrosia.ambrosia.R
import com.projectambrosia.ambrosia.databinding.FragmentTasksBinding

class TasksFragment : Fragment() {

    private lateinit var viewModel: TasksViewModel
    private lateinit var binding: FragmentTasksBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProviders.of(this).get(TasksViewModel::class.java)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_tasks, container, false)
        binding.lifecycleOwner = this
        binding.taskViewModel = viewModel
        return binding.root
    }
}