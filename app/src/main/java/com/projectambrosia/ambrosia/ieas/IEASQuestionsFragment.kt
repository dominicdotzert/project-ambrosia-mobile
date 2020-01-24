package com.projectambrosia.ambrosia.ieas

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.projectambrosia.ambrosia.databinding.FragmentIeasQuestionsBinding

// TODO: Add back navigation
class IEASQuestionsFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val viewModel: IEASQuestionsViewModel by viewModels()
        val binding = FragmentIeasQuestionsBinding.inflate(inflater, container, false)
        val adapter = IEASQuestionsAdapter()

        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        binding.ieasQuestions.adapter = adapter

        viewModel.currentQuestions.observe(this, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })

        viewModel.navigateToResults.observe(this, Observer {
            if (it) {
                this.findNavController().navigate(IEASQuestionsFragmentDirections.actionIEASQuestionsFragmentToIEASResultsFragment())
                viewModel.doneNavigating()
            }
        })

        return binding.root
    }
}
