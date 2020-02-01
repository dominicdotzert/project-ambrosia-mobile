package com.projectambrosia.ambrosia.ieas.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.projectambrosia.ambrosia.R
import com.projectambrosia.ambrosia.databinding.FragmentIeasResultsBinding
import com.projectambrosia.ambrosia.ieas.viewmodels.IEASResultsViewModel
import com.projectambrosia.ambrosia.ieas.viewmodels.IEASResultsViewModelFactory

class IEASResultsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val args: IEASResultsFragmentArgs by navArgs()
        val viewModel: IEASResultsViewModel by viewModels {
            IEASResultsViewModelFactory(
                requireActivity().application,
                args.taskId,
                args.responses
            )
        }
        val binding = FragmentIeasResultsBinding.inflate(layoutInflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        viewModel.navigateToHome.observe(this, Observer {
            if (it) {
                this.findNavController().popBackStack(R.id.tasksFragment, false)
                viewModel.onDoneNavigating()
            }
        })

        // Intercept onBackPressed to prevent back navigation.
        requireActivity().onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    viewModel.navigateHome()
                }
            }
        )

        return binding.root
    }
}
