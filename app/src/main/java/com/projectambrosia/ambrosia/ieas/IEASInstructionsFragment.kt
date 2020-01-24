package com.projectambrosia.ambrosia.ieas


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController

import com.projectambrosia.ambrosia.R
import com.projectambrosia.ambrosia.databinding.FragmentIeasInstructionsBinding

class IEASInstructionsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentIeasInstructionsBinding.inflate(inflater, container, false)

        binding.ieasStartButton.setOnClickListener {
            this.findNavController().navigate(IEASInstructionsFragmentDirections.actionIEASInstructionsFragmentToIEASQuestionsFragment())
        }

        return binding.root
    }
}
