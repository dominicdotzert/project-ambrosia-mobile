package com.projectambrosia.ambrosia.login.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.projectambrosia.ambrosia.R
import com.projectambrosia.ambrosia.network.RequestManager
import com.projectambrosia.ambrosia.network.models.ResponseData
import com.projectambrosia.ambrosia.utilities.PreferencesHelper
import com.projectambrosia.ambrosia.utilities.SPLASH_SCREEN_DELAY_TIME_MILLIS
import kotlinx.coroutines.*

class SplashFragment : Fragment() {

    private val job = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.Main + job)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val requestManager = RequestManager.getInstance(requireActivity().applicationContext)
        val prefs = PreferencesHelper.getInstance(requireActivity().applicationContext)
        val splashScreenCreated = System.currentTimeMillis()

        coroutineScope.launch {
            // Attempt to refresh user access token (if a refresh token exists)
            var authSuccessful = false
            if (prefs.refreshToken != null) {
                val refreshResult = requestManager.refreshUserTokens()
                if (refreshResult is ResponseData<*>) {
                    authSuccessful = true
                }
            }
//            if (prefs.userId != null) authSuccessful = true

            // Ensure that splashscreen is shown for constant time
            val timeElapsed = System.currentTimeMillis() - splashScreenCreated
            if (timeElapsed < SPLASH_SCREEN_DELAY_TIME_MILLIS)
                delay(SPLASH_SCREEN_DELAY_TIME_MILLIS - timeElapsed)

            when (authSuccessful) {
                false -> findNavController().navigate(SplashFragmentDirections.actionSplashFragmentToLoginGraph())
                true -> findNavController().navigate(SplashFragmentDirections.actionSplashFragmentToTasksFragment())
            }
        }
    }
}
