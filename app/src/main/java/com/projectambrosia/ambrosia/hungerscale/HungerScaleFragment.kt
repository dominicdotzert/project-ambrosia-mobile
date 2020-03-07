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
import com.projectambrosia.ambrosia.databinding.DialogHungerScaleBinding
import com.projectambrosia.ambrosia.databinding.DialogHungerScaleHelpBinding
import com.projectambrosia.ambrosia.databinding.FragmentHungerScaleBinding
import com.projectambrosia.ambrosia.utilities.MAX_TIME_BETWEEN_HS_PAIRS_IN_HOURS
import com.projectambrosia.ambrosia.utilities.PreferencesHelper
import java.util.*

class HungerScaleFragment : Fragment() {

    private val viewModel: HungerScaleViewModel by viewModels { HungerScaleViewModelFactory(requireActivity().application) }

    private var helpDialogOpen = false
    private var hsDialogOpen = false

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

        // Observe completed list
        viewModel.completedList.observe(viewLifecycleOwner, Observer {
            val todaySelected = viewModel.todaySelected.value ?: true
            hsHistoryAdapter.addDatesAndSubmitList(it, !todaySelected)
        })

        // Observe TodayAllSelector
        binding.hungerScaleTodayAllSelector.todaySelected.observe(viewLifecycleOwner, Observer {
            it?.let {
                viewModel.todaySelected.value = it
            }
        })

        // Set OnClickListener on help button
        binding.hungerScaleHelpIcon.setOnClickListener {
            if (!helpDialogOpen) {
                showHelpPopup()
            }
        }

        // Set OnClickListener on hunger scale control
        binding.hungerScaleControlValues.selectedValue.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (!hsDialogOpen) {
                    showHSDialog(it)
                }
            }
        })

        viewModel.openHelpDialog.observe(viewLifecycleOwner, Observer {
            if (it) {
                showHelpPopup()
                viewModel.doneOpeningHelpDialog()
            }
        })

        val prefs = PreferencesHelper.getInstance(requireContext())
        if (prefs.firstTimeUser) {
            prefs.firstTimeUser = false
            viewModel.showHelpDialog()
        }

        return binding.root
    }

    private fun showHelpPopup() {
        helpDialogOpen = true

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
            helpDialogOpen = false
        }
    }

    private fun showHSDialog(selectedValue: Int) {
        hsDialogOpen = true

        val dialogBuilder = AlertDialog.Builder(requireActivity())
        val dialogBinding = DialogHungerScaleBinding.inflate(layoutInflater)
        dialogBuilder.setView(dialogBinding.root)

        val dialog = dialogBuilder.create()
        dialog.setCanceledOnTouchOutside(false)
        dialog.window?.apply {
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//            attributes.windowAnimations = R.style.DialogAnimation
        }
        dialog.show()

        // Set user's selected value
        dialogBinding.hungerScaleValues.selectValue(selectedValue)
        dialogBinding.task = viewModel.currentTask.value

        // Show/hide previous entry views if the previous entry is recent and missing the after value.
        viewModel.completedList.value?.firstOrNull()?.let {
            if (it.after == null) {
                val timeElapsedHours = (Calendar.getInstance().timeInMillis - it.entryDate.timeInMillis) / (1000*60*60)
                if (timeElapsedHours <= MAX_TIME_BETWEEN_HS_PAIRS_IN_HOURS) {
                    dialogBinding.previousEntry = it
                }
            }
        }

        // Keep hungerValue variable in dialogBinding up to date
        dialogBinding.hungerScaleValues.selectedValue.observe(viewLifecycleOwner, Observer{
            dialogBinding.hungerValue = it
        })

        // Setup dialog button onClickListeners
        dialogBinding.hungerScaleHelpIcon.setOnClickListener {
            showHelpPopup()
        }
        dialogBinding.hungerScaleDialogCancelButton.setOnClickListener {
            dialog.dismiss()
        }
        // TODO: Add time picker and read value
        dialogBinding.hungerScaleDialogLogButton.setOnClickListener {
            if (dialogBinding.previousEntry?.after == null && dialogBinding.hungerScalePairCheckbox.isChecked) {
                viewModel.savePairedEntry(dialogBinding.previousEntry!!, dialogBinding.hungerValue!!)
            } else {
                viewModel.saveEntry(dialogBinding.hungerValue!!, Calendar.getInstance(), dialogBinding.hungerScaleNotesEditText.text.toString())
            }
            dialog.dismiss()
        }

        dialog.setOnDismissListener {
            hsDialogOpen = false
        }
    }
}
