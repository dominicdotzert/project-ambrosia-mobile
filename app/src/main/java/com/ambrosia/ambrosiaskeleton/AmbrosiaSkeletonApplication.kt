package com.ambrosia.ambrosiaskeleton

import android.app.Application
import timber.log.Timber

class AmbrosiaSkeletonApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
}