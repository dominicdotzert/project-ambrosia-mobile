package com.projectambrosia.ambrosia.hungerscale

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
import com.projectambrosia.ambrosia.R
import com.projectambrosia.ambrosia.databinding.DialogHungerScaleHelpBinding
import com.projectambrosia.ambrosia.databinding.FragmentHungerScaleBinding

class HungerScaleFragment : Fragment() {

    private val viewModel: HungerScaleViewModel by viewModels { HungerScaleViewModelFactory(requireActivity().application) }

    private var dialogOpen = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentHungerScaleBinding.inflate(inflater, container, false)
        val hsHistoryAdapter = HungerScaleHistoryAdapter()

        binding.lifecycleOwner = this
        binding.hungerScaleViewModel = viewModel
        binding.hsHistory.adapter = hsHistoryAdapter

        // Observer completed list
        viewModel.completedList.observe(this, Observer {
            val todaySelected = viewModel.todaySelected.value?: true
            hsHistoryAdapter.addDatesAndSubmitList(it, !todaySelected)
        })

        // Set observer on TodayAllSelector
        binding.hungerScaleTodayAllSelector.todaySelected.observe(this, Observer {
            it?.let {
                viewModel.todaySelected.value = it
            }
        })

        // Set OnClickListener on help button
        binding.hungerScaleHelpIcon.setOnClickListener {
            if (!dialogOpen) {
                showHelpPopup()
            }
        }

        return binding.root
    }

    private fun showHelpPopup() {
        dialogOpen = true

        val dialogBuilder = AlertDialog.Builder(requireActivity())
        val dialogBinding = DialogHungerScaleHelpBinding.inflate(layoutInflater)
        dialogBuilder.setView(dialogBinding.root)

        val dialog = dialogBuilder.create()
        dialog.window?.apply {
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            attributes.windowAnimations = R.style.DialogAnimation
        }
        dialog.show()

        dialogBinding.scaleIndices.text = (1..10).joinToString("\n")

        val values = listOf(
            getString(R.string.hs_level_1),
            getString(R.string.hs_level_2),
            getString(R.string.hs_level_3),
            getString(R.string.hs_level_4),
            getString(R.string.hs_level_5),
            getString(R.string.hs_level_6),
            getString(R.string.hs_level_7),
            getString(R.string.hs_level_8),
            getString(R.string.hs_level_9),
            getString(R.string.hs_level_10)
        ).joinToString("\n")
        dialogBinding.scaleValues.text = values

        dialogBinding.hungerScaleHelpCloseButton.setOnClickListener {
            dialog.dismiss()
        }

        dialog.setOnDismissListener {
            dialogOpen = false
        }
    }
}
