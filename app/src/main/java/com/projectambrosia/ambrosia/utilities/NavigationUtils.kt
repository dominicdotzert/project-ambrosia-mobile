package com.projectambrosia.ambrosia.utilities

import androidx.navigation.NavDestination
import com.projectambrosia.ambrosia.R

fun NavDestination.hasBottomNavBar(): Boolean {
    return this.id == R.id.tasksFragment ||
            this.id == R.id.journalFragment ||
            this.id == R.id.hungerScaleFragment
}