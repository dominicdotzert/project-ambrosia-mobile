package com.projectambrosia.ambrosia.login.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.projectambrosia.ambrosia.databinding.FragmentLoginEmailBinding
import com.projectambrosia.ambrosia.login.factories.EmailViewModelFactory
import com.projectambrosia.ambrosia.login.viewmodels.EmailViewModel

class EmailFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding =  FragmentLoginEmailBinding.inflate(inflater, container, false)
        val viewModel: EmailViewModel by viewModels { EmailViewModelFactory(requireActivity().application) }

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        viewModel.navigateToLogIn.observe(this, Observer {
            if (it) {
                this.findNavController().navigate(EmailFragmentDirections.actionEmailFragmentToPasswordFragment(true))
                viewModel.doneNavigatingToLogin()
            }
        })

        viewModel.navigateToSignUp.observe(this, Observer {
            if (it) {
                this.findNavController().navigate(EmailFragmentDirections.actionEmailFragmentToWelcomeFragment())
                viewModel.doneNavigatingToSignUp()
            }
        })

        return binding.root
    }
}
