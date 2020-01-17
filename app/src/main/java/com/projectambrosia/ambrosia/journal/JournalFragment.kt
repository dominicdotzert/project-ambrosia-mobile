package com.projectambrosia.ambrosia.journal

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil

import com.projectambrosia.ambrosia.R
import com.projectambrosia.ambrosia.databinding.FragmentJournalBinding
import timber.log.Timber

class JournalFragment : Fragment() {

    private lateinit var viewModel: JournalViewModel
    private lateinit var binding: FragmentJournalBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewModel = ViewModelProviders.of(this).get(JournalViewModel::class.java)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_journal, container, false)
        binding.lifecycleOwner = this
        binding.journalViewModel = viewModel

        binding.button.setOnClickListener {
            Timber.i("Entry value is: ${binding.journalEntry.text}")
        }

        return binding.root
    }
}
