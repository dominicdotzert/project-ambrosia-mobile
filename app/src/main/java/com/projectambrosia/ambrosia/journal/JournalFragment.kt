package com.projectambrosia.ambrosia.journal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.projectambrosia.ambrosia.databinding.FragmentJournalBinding

class JournalFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val viewModel: JournalViewModel by viewModels { JournalViewModelFactory(requireActivity().application) }
        val binding = FragmentJournalBinding.inflate(inflater, container, false)
        val adapter = JournalPromptAdapter(JournalPromptListener { prompt ->
            // TODO: open journal dialog to journal prompt (or freestyle)
            viewModel.openPrompt(prompt)
        })

        binding.lifecycleOwner = this
        binding.journalViewModel = viewModel
        binding.journalPromptList.adapter = adapter

        viewModel.journalTasks.observe(this, Observer { prompts ->
            adapter.addFreestyleAndSubmitList(prompts, requireActivity().application)
        })

        return binding.root
    }
}
