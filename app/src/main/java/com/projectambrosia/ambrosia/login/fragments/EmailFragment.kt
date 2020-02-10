package com.projectambrosia.ambrosia.login.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
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

        return binding.root
    }
}
