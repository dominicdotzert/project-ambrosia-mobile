package com.projectambrosia.ambrosia.ieas

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.projectambrosia.ambrosia.databinding.FragmentIeasQuestionsBinding

class IEASQuestionsFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val viewModel: IEASSharedViewModel by viewModels()
        val binding = FragmentIeasQuestionsBinding.inflate(inflater, container, false)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        return binding.root
    }
}
