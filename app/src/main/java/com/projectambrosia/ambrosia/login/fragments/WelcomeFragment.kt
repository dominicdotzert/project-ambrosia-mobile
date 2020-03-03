package com.projectambrosia.ambrosia.login.fragments

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
import com.projectambrosia.ambrosia.databinding.FragmentLoginWelcomeBinding

class WelcomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentLoginWelcomeBinding.inflate(layoutInflater, container, false)
        val args: WelcomeFragmentArgs by navArgs()

        val welcomeText = resources.getString(R.string.sign_up_welcome_message_1)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            binding.loginWelcomeText1.text = Html.fromHtml(welcomeText, Html.FROM_HTML_MODE_COMPACT)
        } else {
            @Suppress("DEPRECATION")
            binding.loginWelcomeText1.text = Html.fromHtml(welcomeText)
        }

        binding.loginWelcomeContinueButton.setOnClickListener {
            this.findNavController().navigate(WelcomeFragmentDirections.actionWelcomeFragmentToDisclaimerFragment(args.email, args.password))
        }

        return binding.root
    }
}
