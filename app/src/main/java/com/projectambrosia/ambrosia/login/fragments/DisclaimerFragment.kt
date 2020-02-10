package com.projectambrosia.ambrosia.login.fragments

import android.graphics.Color
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
import com.projectambrosia.ambrosia.R
import com.projectambrosia.ambrosia.databinding.FragmentLoginDisclaimerBinding
import com.projectambrosia.ambrosia.login.viewmodels.DisclaimerViewModel

class DisclaimerFragment : Fragment() {

    private val viewModel: DisclaimerViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentLoginDisclaimerBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        setDisclaimerExplanationText(binding.disclaimerExplanation)

        binding.disclaimerContinueButton.setOnClickListener {
            Toast.makeText(requireActivity(), "Continue", Toast.LENGTH_SHORT).show()
        }

        return binding.root
    }

    private fun setDisclaimerExplanationText(textView: TextView) {
        val text = resources.getString(R.string.disclaimer_explanation)
        val targetWord = resources.getString(R.string.disclaimer_explanation_link)

        val span = SpannableString(text)
        val clickableSpan = object: ClickableSpan() {
            override fun onClick(p0: View) {
                showScoffPopup()
            }
        }
        span.setSpan(clickableSpan, span.indexOf(targetWord), span.indexOf(targetWord) + targetWord.length, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE)

        textView.text = span
        textView.movementMethod = LinkMovementMethod.getInstance()
        textView.highlightColor = Color.TRANSPARENT
    }

    private fun showScoffPopup() {
        Toast.makeText(requireActivity(), "Popup!", Toast.LENGTH_SHORT).show()
    }
}
