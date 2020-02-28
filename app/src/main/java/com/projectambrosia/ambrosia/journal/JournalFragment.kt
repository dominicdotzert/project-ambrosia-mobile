package com.projectambrosia.ambrosia.journal

import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.projectambrosia.ambrosia.R
import com.projectambrosia.ambrosia.databinding.DialogJournalEntryBinding
import com.projectambrosia.ambrosia.databinding.FragmentJournalBinding

class JournalFragment : Fragment() {

    private val viewModel: JournalViewModel by viewModels { JournalViewModelFactory(requireActivity().application) }

    private var dialogOpen = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentJournalBinding.inflate(inflater, container, false)
        val args: JournalFragmentArgs by navArgs()

        val journalPromptAdapter = JournalPromptAdapter(JournalPromptListener { prompt ->
            if (!dialogOpen) {
                showJournalPopup(prompt)
            }
        })
        val journalHistoryAdapter = JournalHistoryAdapter()

        binding.lifecycleOwner = this
        binding.journalViewModel = viewModel
        binding.journalPromptList.adapter = journalPromptAdapter
        binding.journalHistory.adapter = journalHistoryAdapter

        // Observe lists
        viewModel.journalTasks.observe(viewLifecycleOwner, Observer {
            journalPromptAdapter.addFreestyleAndSubmitList(it, requireActivity().application)
        })
        viewModel.completedList.observe(viewLifecycleOwner, Observer {
            val todaySelected = viewModel.todaySelected.value ?: true
            journalHistoryAdapter.addDatesAndSubmitList(it, !todaySelected)
        })

        // Observe TodayAllSelector
        binding.journalTodayAllSelector.todaySelected.observe(viewLifecycleOwner, Observer {
            it?.let {
                viewModel.todaySelected.value = it
            }
        })

        // Observe navigation events
        viewModel.openDialog.observe(viewLifecycleOwner, Observer {
            it?.let {
                showJournalPopup(it)
                viewModel.doneOpeningJournalDialog()
            }
        })

        if (args.taskId != -1L) {
            viewModel.openJournalDialog(args.taskId)
        }

        return binding.root
    }

    private fun showJournalPopup(prompt: JournalPrompt) {
        dialogOpen = true

        val dialogBuilder = AlertDialog.Builder(requireActivity())
        val dialogBinding = DialogJournalEntryBinding.inflate(layoutInflater)
        dialogBuilder.setView(dialogBinding.root)

        val dialog = dialogBuilder.create()
        dialog.setCanceledOnTouchOutside(false)
        dialog.window?.apply {
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            attributes.windowAnimations = R.style.DialogAnimation // TODO: Revisit and attempt to set pivot fields to center of card clicked
        }
        dialog.show()

        dialogBinding.prompt = prompt
        dialogBinding.journalDialogCancelButton.setOnClickListener {
            dialog.dismiss()
        }
        dialogBinding.journalDialogSaveButton.setOnClickListener {
            if (dialogBinding.journalDialogEntry.text.isNotBlank()) {
                viewModel.savePrompt(dialogBinding.prompt!!, dialogBinding.journalDialogEntry.text.toString())
                dialog.dismiss()
            }
        }

        dialog.setOnDismissListener {
            dialogOpen = false
        }
    }
}
