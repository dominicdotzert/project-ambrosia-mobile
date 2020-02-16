package com.projectambrosia.ambrosia.login.fragments

import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.projectambrosia.ambrosia.LoginGraphDirections
import com.projectambrosia.ambrosia.R
import com.projectambrosia.ambrosia.databinding.FragmentLoginBinding
import com.projectambrosia.ambrosia.login.factories.LoginViewModelFactory
import com.projectambrosia.ambrosia.login.viewmodels.LoginViewModel

class LoginFragment : Fragment() {

    private var originalMode: Int? = null

    // TODO: Fix screen for keyboard and small screen
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.window?.attributes?.apply {
            originalMode = softInputMode
            softInputMode = WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        activity?.window?.attributes?.softInputMode = originalMode
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding =  FragmentLoginBinding.inflate(inflater, container, false)
        val viewModel: LoginViewModel by viewModels { LoginViewModelFactory(requireActivity().application) }

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        binding.loginCreateAccountText.paintFlags = binding.loginCreateAccountText.paintFlags or Paint.UNDERLINE_TEXT_FLAG

        viewModel.navigateToHome.observe(this, Observer {
            if (it) {
                this.findNavController().navigate(LoginFragmentDirections.actionGlobalTasksFragment())
                viewModel.doneNavigatingToLogin()
            }
        })

        viewModel.navigateToSignUp.observe(this, Observer {
            if (it) {
                this.findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToWelcomeFragment())
                viewModel.doneNavigatingToSignUp()
            }
        })

        return binding.root
    }
}
