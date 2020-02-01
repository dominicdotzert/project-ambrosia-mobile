package com.projectambrosia.ambrosia.ieas.fragments

import android.animation.ObjectAnimator
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
import com.projectambrosia.ambrosia.MainActivity
import com.projectambrosia.ambrosia.databinding.FragmentIeasQuestionsBinding
import com.projectambrosia.ambrosia.ieas.IEASQuestionClickListener
import com.projectambrosia.ambrosia.ieas.IEASQuestionsAdapter
import com.projectambrosia.ambrosia.ieas.viewmodels.IEASQuestionsViewModel
import com.projectambrosia.ambrosia.utilities.SupportNavigateUpCallback

class IEASQuestionsFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val args: IEASQuestionsFragmentArgs by navArgs()
        val viewModel: IEASQuestionsViewModel by viewModels()
        val binding = FragmentIeasQuestionsBinding.inflate(inflater, container, false)
        val adapter = IEASQuestionsAdapter(
            IEASQuestionClickListener { question ->
                question.toggle()
            })

        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        binding.ieasQuestions.adapter = adapter

        // Update question list when user changes page
        viewModel.currentQuestions.observe(this, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })

        // Animate progress bar
        viewModel.progress.observe(this, Observer {
            it?.let {
                ObjectAnimator.ofInt(binding.ieasProgressBar, "progress", it)
                    .setDuration(300)
                    .start()
            }
        })

        // Navigate to results page
        viewModel.navigateToResults.observe(this, Observer {
            if (it) {
                this.findNavController()
                    .navigate(
                        IEASQuestionsFragmentDirections.actionIEASQuestionsFragmentToIEASResultsFragment(
                            args.taskId,
                            viewModel.getResponsesArray()
                        )
                    )
                viewModel.doneNavigatingNext()
            }
        })

        // Navigate back to instructions page
        viewModel.navigateBack.observe(this, Observer {
            if (it) {
                this.findNavController().navigateUp()
                viewModel.doneNavigatingBack()
            }
        })

        // Intercept onBackPressed to only navigate to the previous fragment if on the first set of questions.
        requireActivity().onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    viewModel.onBack()
                }
            }
        )

        // Set callback on the parent activity to intercept supportNavigateUp() and only navigate
        // to the previous fragment if on the first set of questions.
        if (activity is MainActivity) {
            (activity as MainActivity).supportNavigateUpCallback = SupportNavigateUpCallback {
                viewModel.onBack()
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()

        if (activity is MainActivity) {
            (activity as MainActivity).supportNavigateUpCallback = null
        }
    }
}
