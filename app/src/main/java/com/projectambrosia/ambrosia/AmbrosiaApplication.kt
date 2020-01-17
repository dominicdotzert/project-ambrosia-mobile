package com.projectambrosia.ambrosia

import android.app.Application
import timber.log.Timber

class AmbrosiaApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
}