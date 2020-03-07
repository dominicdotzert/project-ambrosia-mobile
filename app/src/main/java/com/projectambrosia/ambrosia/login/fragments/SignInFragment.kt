package com.projectambrosia.ambrosia.login.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.projectambrosia.ambrosia.LoginGraphDirections
import com.projectambrosia.ambrosia.databinding.FragmentSignInBinding
import com.projectambrosia.ambrosia.login.factories.SignInViewModelFactory
import com.projectambrosia.ambrosia.login.viewmodels.SignInViewModel

class SignInFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentSignInBinding.inflate(inflater, container, false)
        val viewModel: SignInViewModel by viewModels { SignInViewModelFactory(requireActivity().application) }

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        viewModel.navigateToHome.observe(viewLifecycleOwner, Observer {
            if (it) {
                this.findNavController().navigate(LoginGraphDirections.actionGlobalLoadingFragment())
                viewModel.doneNavigatingHome()
            }
        })

        return binding.root
    }
}
