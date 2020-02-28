package com.projectambrosia.ambrosia.login.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import com.projectambrosia.ambrosia.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LoadingFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        CoroutineScope(Dispatchers.Main).launch {
            delay(1000)
            findNavController().navigate(LoadingFragmentDirections.actionLoadingFragmentToTasksFragment())
        }

        return inflater.inflate(R.layout.fragment_splash, container, false)
    }
}
