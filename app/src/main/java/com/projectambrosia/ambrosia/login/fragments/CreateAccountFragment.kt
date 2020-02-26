package com.projectambrosia.ambrosia.login.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.projectambrosia.ambrosia.databinding.FragmentLoginCreateAccountBinding
import com.projectambrosia.ambrosia.login.factories.CreateAccountViewModelFactory
import com.projectambrosia.ambrosia.login.viewmodels.CreateAccountViewModel

class CreateAccountFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentLoginCreateAccountBinding.inflate(inflater, container, false)
        val viewModel: CreateAccountViewModel by viewModels { CreateAccountViewModelFactory() }

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        viewModel.navigateToCreateAccount.observe(viewLifecycleOwner, Observer {
            if (it) {
                this.findNavController().navigate(
                    CreateAccountFragmentDirections
                        .actionPasswordFragmentToCollectUserInfoFragment(viewModel.email.value!!, viewModel.password.value!!)
                )
                viewModel.doneNavigatingToCreateAccount()
            }
        })

        return binding.root
    }
}
