package com.projectambrosia.ambrosia.ieas


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.projectambrosia.ambrosia.databinding.FragmentIeasInstructionsBinding

class IEASInstructionsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentIeasInstructionsBinding.inflate(inflater, container, false)

        val args: IEASInstructionsFragmentArgs by navArgs()
        val taskId = args.taskId

        binding.ieasStartButton.setOnClickListener {
            this.findNavController().navigate(IEASInstructionsFragmentDirections.actionIEASInstructionsFragmentToIEASQuestionsFragment(taskId))
        }

        return binding.root
    }
}
