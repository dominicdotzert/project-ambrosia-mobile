package com.projectambrosia.ambrosia.login.fragments

import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.projectambrosia.ambrosia.R
import com.projectambrosia.ambrosia.databinding.DialogDisclaimerBinding
import com.projectambrosia.ambrosia.databinding.DialogDisclaimerResultsBinding
import com.projectambrosia.ambrosia.databinding.FragmentLoginDisclaimerBinding
import com.projectambrosia.ambrosia.login.viewmodels.DisclaimerViewModel
import kotlin.system.exitProcess

class DisclaimerFragment : Fragment() {

    private val viewModel: DisclaimerViewModel by viewModels()

    private var dialogOpen = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentLoginDisclaimerBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        setDisclaimerExplanationText(binding.disclaimerExplanation)

        viewModel.navigatingToPassword.observe(this, Observer {
            if (it) {
                this.findNavController().navigate(DisclaimerFragmentDirections.actionDisclaimerFragmentToPasswordFragment())
                viewModel.doneNavigatingToPassword()
            }
        })

        return binding.root
    }

    private fun setDisclaimerExplanationText(textView: TextView) {
        val text = resources.getString(R.string.disclaimer_explanation)
        val targetWord = resources.getString(R.string.disclaimer_explanation_link)

        val span = SpannableString(text)
        val clickableSpan = object : ClickableSpan() {
            override fun onClick(view: View) {
                if (!dialogOpen) showScoffPopup()
            }
        }
        span.setSpan(
            clickableSpan,
            span.indexOf(targetWord),
            span.indexOf(targetWord) + targetWord.length,
            SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        textView.text = span
        textView.movementMethod = LinkMovementMethod.getInstance()
        textView.highlightColor = Color.TRANSPARENT
    }

    private fun showScoffPopup() {
        dialogOpen = true

        val dialogBuilder = AlertDialog.Builder(requireActivity())
        val dialogBinding = DialogDisclaimerBinding.inflate(layoutInflater)
        dialogBuilder.setView(dialogBinding.root)

        val dialog = dialogBuilder.create()
        dialog.setCanceledOnTouchOutside(false)
        dialog.window?.apply {
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            attributes.windowAnimations = R.style.DialogAnimation
        }
        dialog.show()

        dialogBinding.disclaimerDialogCancelButton.setOnClickListener {
            dialog.dismiss()
        }

        dialogBinding.disclaimerDialogCompleteButton.setOnClickListener {
            val responses = listOf(
                dialogBinding.disclaimerQuestion1.checked,
                dialogBinding.disclaimerQuestion2.checked,
                dialogBinding.disclaimerQuestion3.checked,
                dialogBinding.disclaimerQuestion4.checked,
                dialogBinding.disclaimerQuestion5.checked
            )
            dialog.dismiss()
            parseResponses(responses)
        }

        dialog.setOnDismissListener {
            dialogOpen = false
        }
    }

    private fun parseResponses(responses: List<Boolean>) {
        val yesCount = responses.count { response -> response }

        if (yesCount <= 1) {
            // TODO: Follow up on designs for "passing" SCOFF test.
            Toast.makeText(requireActivity(), "Please continue at your own risk", Toast.LENGTH_SHORT).show()
        } else {
            showResultsDialog()
        }
    }

    private fun showResultsDialog() {
        val dialogBuilder = AlertDialog.Builder(requireActivity())
        val dialogBinding = DialogDisclaimerResultsBinding.inflate(layoutInflater)
        dialogBuilder.setView(dialogBinding.root)

        val dialog = dialogBuilder.create()
        dialog.setCanceledOnTouchOutside(false)
        dialog.window?.apply {
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            attributes.windowAnimations = R.style.DialogAnimation
        }
        dialog.show()

        dialogBinding.disclaimerDialogResultsButton.setOnClickListener {
           dialog.dismiss()
        }

        dialog.setOnDismissListener {
            closeApp()
        }
    }

    private fun closeApp() {
        requireActivity().finish()
        exitProcess(1)
    }
}
