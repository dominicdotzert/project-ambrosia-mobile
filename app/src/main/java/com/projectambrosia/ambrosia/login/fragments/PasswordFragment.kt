package com.projectambrosia.ambrosia.login.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.projectambrosia.ambrosia.databinding.FragmentPasswordBinding
import com.projectambrosia.ambrosia.login.factories.PasswordViewModelFactory
import com.projectambrosia.ambrosia.login.viewmodels.PasswordViewModel

class PasswordFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding =  FragmentPasswordBinding.inflate(inflater, container, false)
        val args: PasswordFragmentArgs by navArgs()
        val viewModel: PasswordViewModel by viewModels { PasswordViewModelFactory(args.isReturningUser) }

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        viewModel.navigateToCreateAccount.observe(this, Observer {
            if (it) {
                Toast.makeText(requireActivity(), "Create account", Toast.LENGTH_SHORT).show()
                viewModel.doneNavigatingToCreateAccount()
            }
        })

        viewModel.navigateToHome.observe(this, Observer {
            if (it) {
                Toast.makeText(requireActivity(), "Sign in ", Toast.LENGTH_SHORT).show()
                viewModel.doneNavigatingHome()
            }
        })

        return binding.root
    }
}
