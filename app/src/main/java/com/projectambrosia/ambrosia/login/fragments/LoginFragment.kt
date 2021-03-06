package com.projectambrosia.ambrosia.login.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.projectambrosia.ambrosia.databinding.FragmentLoginBinding
import com.projectambrosia.ambrosia.login.viewmodels.LoginViewModel

class LoginFragment : Fragment() {

//    private var originalMode: Int? = null
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        activity?.window?.attributes?.apply {
//            originalMode = softInputMode
//            softInputMode = WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN
//        }
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//        activity?.window?.attributes?.softInputMode = originalMode
//    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding =  FragmentLoginBinding.inflate(inflater, container, false)
        val viewModel: LoginViewModel by viewModels()

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        viewModel.navigateToSignIn.observe(viewLifecycleOwner, Observer {
            if (it) {
                this.findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToSignInFragment())
                viewModel.doneNavigatingToSignIn()
            }
        })

        viewModel.navigateToSignUp.observe(viewLifecycleOwner, Observer {
            if (it) {
                this.findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToWelcomeFragment())
                viewModel.doneNavigatingToSignUp()
            }
        })

        return binding.root
    }
}
