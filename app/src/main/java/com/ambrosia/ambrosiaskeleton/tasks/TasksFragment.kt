package com.ambrosia.ambrosiaskeleton.tasks

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer

import com.ambrosia.ambrosiaskeleton.R
import com.ambrosia.ambrosiaskeleton.databinding.FragmentTasksBinding

class TasksFragment : Fragment() {

    private lateinit var viewModel: TasksViewModel
    private lateinit var binding: FragmentTasksBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProviders.of(this).get(TasksViewModel::class.java)

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_tasks, container, false)
        binding.taskViewModel = viewModel
        binding.lifecycleOwner = this
        return binding.root
    }
}