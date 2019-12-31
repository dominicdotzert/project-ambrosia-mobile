package com.ambrosia.ambrosiaskeleton.hungerscale

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.ambrosia.ambrosiaskeleton.R

class HungerScaleFragment : Fragment() {

    companion object {
        fun newInstance() = HungerScaleFragment()
    }

    private lateinit var viewModel: HungerScaleViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_hunger_scale, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(HungerScaleViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
