package com.projectambrosia.ambrosia.login.fragments

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.projectambrosia.ambrosia.MainActivity
import com.projectambrosia.ambrosia.databinding.FragmentCollectUserInfoBinding
import com.projectambrosia.ambrosia.login.CollectUserInfoViewPagerAdapter
import com.projectambrosia.ambrosia.login.factories.CollectUserInfoViewModelFactory
import com.projectambrosia.ambrosia.login.viewmodels.CollectUserInfoViewModel
import com.projectambrosia.ambrosia.utilities.SupportNavigateUpCallback

class CollectUserInfoFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentCollectUserInfoBinding.inflate(layoutInflater, container, false)
        val viewModel: CollectUserInfoViewModel by viewModels { CollectUserInfoViewModelFactory(requireActivity().application) }
        val viewPagerAdapter = CollectUserInfoViewPagerAdapter(viewModel)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        binding.userInfoViewPager.apply {
            isUserInputEnabled = false
            adapter = viewPagerAdapter
        }

        // Observe page change event
        viewModel.currentPage.observe(this, Observer {
            binding.userInfoViewPager.currentItem = it
        })

        // Observer navigation events
        viewModel.navigateToHome.observe(this, Observer {
            if (it) {
                Toast.makeText(requireActivity(), "Registering User", Toast.LENGTH_SHORT).show()
                viewModel.doneNavigatingHome()
            }
        })

        viewModel.navigateBack.observe(this, Observer {
            if (it) {
                this.findNavController().navigateUp()
                viewModel.doneNavigatingBack()
            }
        })

        // Animate progress bar
        viewModel.progress.observe(this, Observer {
            it?.let {
                ObjectAnimator
                    .ofInt(binding.userInfoProgressBar, "progress", it)
                    .setDuration(resources.getInteger(android.R.integer.config_shortAnimTime).toLong())
                    .start()
            }
        })

        // Intercept onBackPressed to only pop the navigation stack if on the first user info page
        requireActivity().onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    viewModel.onBack()
                }
            }
        )

        // Set callback on the parent activity to intercept supportNavigateUp() and pop the navigation
        // stack if on the first user info page.
        if (activity is MainActivity) {
            (activity as MainActivity).supportNavigateUpCallback = SupportNavigateUpCallback {
                viewModel.onBack()
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()

        if (activity is MainActivity) {
            (activity as MainActivity).supportNavigateUpCallback = null
        }
    }
}
