package com.projectambrosia.ambrosia.journal

import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
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
        val adapter = JournalPromptAdapter(JournalPromptListener { prompt ->
            if (!dialogOpen) {
                showJournalPopup(prompt)
            }
        })

        binding.lifecycleOwner = this
        binding.journalViewModel = viewModel
        binding.journalPromptList.adapter = adapter

        viewModel.journalTasks.observe(this, Observer { prompts ->
            adapter.addFreestyleAndSubmitList(prompts, requireActivity().application)
        })

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
                Toast.makeText(requireActivity(), "Entry saved!", Toast.LENGTH_SHORT).show()
                viewModel.savePrompt(dialogBinding.prompt!!, dialogBinding.journalDialogEntry.text.toString())
                dialog.dismiss()
            }
        }

        dialog.setOnDismissListener {
            dialogOpen = false
        }
    }
}
