package com.projectambrosia.ambrosia.ieas

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.projectambrosia.ambrosia.databinding.FragmentIeasResultsBinding

// TODO: Pass question responses
class IEASResultsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentIeasResultsBinding.inflate(layoutInflater, container, false)
        val viewModel: IEASResultsViewModel by viewModels()
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        return binding.root
    }
}
