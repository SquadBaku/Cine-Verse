package com.karrar.movieapp.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.karrar.movieapp.R
import com.karrar.movieapp.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setTheme(R.style.Theme_MovieApp)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        installSplashScreen()

        supportActionBar?.hide()
    }

    override fun onResume() {
        super.onResume()
        val navController = findNavController(R.id.nav_host_fragment)
        binding.bottomNavigation.setupWithNavController(navController)


        setBottomNavigationVisibility(navController)
        setNavigationController(navController)
    }

    private fun setBottomNavigationVisibility(navController: NavController) {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            binding.bottomNavigation.isVisible = destination.id == R.id.homeFragment ||
                    destination.id == R.id.exploringFragment ||
                    destination.id == R.id.matchFragment ||
                    destination.id == R.id.profileFragment
        }
    }

    private fun setNavigationController(navController: NavController) {
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            NavigationUI.onNavDestinationSelected(item, navController)
            navController.popBackStack(item.itemId, inclusive = false)
            true
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return findNavController(R.id.nav_host_fragment).navigateUp() || super.onSupportNavigateUp()
    }
}