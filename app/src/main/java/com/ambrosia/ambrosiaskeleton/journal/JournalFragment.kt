package com.ambrosia.ambrosiaskeleton.journal

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.ambrosia.ambrosiaskeleton.R

class JournalFragment : Fragment() {

    companion object {
        fun newInstance() = JournalFragment()
    }

    private lateinit var viewModel: JournalViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_journal, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(JournalViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
