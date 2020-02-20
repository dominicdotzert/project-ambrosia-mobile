package com.projectambrosia.ambrosia.login.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.projectambrosia.ambrosia.R
import com.projectambrosia.ambrosia.utilities.SPLASH_SCREEN_DELAY_TIME_MILLIS
import kotlinx.coroutines.*

class SplashFragment : Fragment() {

    private val job = Job()
    private val scope = CoroutineScope(Dispatchers.Main + job)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val splashScreenCreated = System.currentTimeMillis()
        scope.launch {
            // TODO: Check authentication in background

            // Ensure that splashscreen is shown for constant time
            val timeElapsed = System.currentTimeMillis() - splashScreenCreated
            if (timeElapsed < SPLASH_SCREEN_DELAY_TIME_MILLIS)
                delay(SPLASH_SCREEN_DELAY_TIME_MILLIS - timeElapsed)

            findNavController().navigate(SplashFragmentDirections.actionSplashFragmentToLoginGraph())
        }
    }
}
