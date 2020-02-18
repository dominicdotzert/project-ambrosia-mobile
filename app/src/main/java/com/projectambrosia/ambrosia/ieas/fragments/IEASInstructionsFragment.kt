package com.projectambrosia.ambrosia.ieas.fragments

import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.projectambrosia.ambrosia.R
import com.projectambrosia.ambrosia.R.color.salmon
import com.projectambrosia.ambrosia.databinding.FragmentIeasInstructionsBinding

class IEASInstructionsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentIeasInstructionsBinding.inflate(inflater, container, false)

        val args: IEASInstructionsFragmentArgs by navArgs()
        val taskId = args.taskId

        // Set text colour
        val instructionsColour = String.format("#%06X", 0xFFFFFF and resources.getColor(salmon, null))
        val instructionsText = resources.getString(R.string.ieas_full_instructions_2, instructionsColour, instructionsColour)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            binding.ieasInstructions2.text = Html.fromHtml(instructionsText, Html.FROM_HTML_MODE_COMPACT)
        } else {
            @Suppress("DEPRECATION")
            binding.ieasInstructions2.text = Html.fromHtml(instructionsText)
        }

        // Start button listener
        binding.ieasStartButton.setOnClickListener {
            this.findNavController().navigate(
                IEASInstructionsFragmentDirections.actionIEASInstructionsFragmentToIEASQuestionsFragment(
                    taskId
                )
            )
        }

        return binding.root
    }
}
