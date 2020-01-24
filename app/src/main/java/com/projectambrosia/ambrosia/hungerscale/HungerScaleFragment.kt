package com.projectambrosia.ambrosia.hungerscale

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.projectambrosia.ambrosia.R

class HungerScaleFragment : Fragment() {

    private val viewModel: HungerScaleViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_hunger_scale, container, false)
    }
}
