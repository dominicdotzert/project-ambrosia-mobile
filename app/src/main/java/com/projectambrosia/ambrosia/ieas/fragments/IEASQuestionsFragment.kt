package com.projectambrosia.ambrosia.ieas.fragments

import android.animation.ObjectAnimator
import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.text.Html
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
import com.projectambrosia.ambrosia.R
import com.projectambrosia.ambrosia.databinding.DialogIeasQuestionConfirmationBinding
import com.projectambrosia.ambrosia.databinding.FragmentIeasQuestionsBinding
import com.projectambrosia.ambrosia.ieas.IEASQuestionClickListener
import com.projectambrosia.ambrosia.ieas.IEASQuestionsAdapter
import com.projectambrosia.ambrosia.ieas.viewmodels.IEASQuestionsViewModel
import com.projectambrosia.ambrosia.utilities.SupportNavigateUpCallback

class IEASQuestionsFragment : Fragment() {

    private val viewModel: IEASQuestionsViewModel by viewModels()
    private val args: IEASQuestionsFragmentArgs by navArgs()

    private var dialogOpen = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentIeasQuestionsBinding.inflate(inflater, container, false)
        val adapter = IEASQuestionsAdapter(
            IEASQuestionClickListener { question ->
                question.toggle()
            }
        )

        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        binding.ieasQuestions.adapter = adapter

        // Set instruction text
        val instructionsText = resources.getString(R.string.ieas_questions_instructions)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            binding.ieasQuestionsInstruction.text = Html.fromHtml(instructionsText, Html.FROM_HTML_MODE_COMPACT)
        } else {
            @Suppress("DEPRECATION")
            binding.ieasQuestionsInstruction.text = Html.fromHtml(instructionsText)
        }

        // Update question list when user changes page
        viewModel.currentQuestions.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })

        // Animate progress bar
        viewModel.progress.observe(viewLifecycleOwner, Observer {
            it?.let {
                ObjectAnimator.ofInt(binding.ieasProgressBar, "progress", it)
                    .setDuration(300)
                    .start()
            }
        })

        // Navigate to results page
        viewModel.navigateToResults.observe(viewLifecycleOwner, Observer {
            if (it && !dialogOpen) {
                showConfirmationDialog()
                viewModel.doneNavigatingNext()
            }
        })

        // Navigate back to instructions page
        viewModel.navigateBack.observe(viewLifecycleOwner, Observer {
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

    private fun showConfirmationDialog() {
        dialogOpen = true

        val dialogBuilder = AlertDialog.Builder(requireActivity())
        val dialogBinding = DialogIeasQuestionConfirmationBinding.inflate(layoutInflater)
        dialogBuilder.setView(dialogBinding.root)

        val dialog = dialogBuilder.create()
        dialog.setCanceledOnTouchOutside(false)
        dialog.window?.apply {
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            attributes.windowAnimations = R.style.DialogAnimation
        }
        dialog.show()

        dialogBinding.ieasResultsConfirmationDialogCancelButton.setOnClickListener {
            dialog.dismiss()
        }

        dialogBinding.ieasResultsConfirmationDialogCompleteButton.setOnClickListener {
            dialog.dismiss()
            this.findNavController()
                .navigate(
                    IEASQuestionsFragmentDirections.actionIEASQuestionsFragmentToIEASResultsFragment(
                        args.taskId,
                        viewModel.getResponsesArray()
                    )
                )
        }

        dialog.setOnDismissListener {
            dialogOpen = false
        }
    }
}
