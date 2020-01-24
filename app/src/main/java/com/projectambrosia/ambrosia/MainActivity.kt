package com.projectambrosia.ambrosia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import com.projectambrosia.ambrosia.databinding.ActivityMainBinding
import com.projectambrosia.ambrosia.utilities.hasBottomNavBar

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        val navController = this.findNavController(R.id.nav_host_fragment)
        val bottomNavBar = binding.navView

        navController.addOnDestinationChangedListener {_, destination, _ ->
            if (destination.hasBottomNavBar()) {
                bottomNavBar.visibility = View.VISIBLE
            } else {
                bottomNavBar.visibility = View.GONE
            }
        }
        NavigationUI.setupWithNavController(bottomNavBar, navController)

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.tasksFragment,
                R.id.journalFragment,
                R.id.hungerScaleFragment)
        )
        supportActionBar?.setDisplayShowTitleEnabled(false)
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.nav_host_fragment)
        return navController.navigateUp()
    }
}