package com.karrar.movieapp.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.karrar.movieapp.R
import com.karrar.movieapp.databinding.ActivityMainBinding
import com.karrar.movieapp.utilities.Const.APP_PREF
import com.karrar.movieapp.utilities.Const.ONBOARDING_COMPLETED
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setTheme(R.style.Theme_MovieApp)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)


        installSplashScreen()
        supportActionBar?.hide()

        setupNavigationAndBottomNav()

    }

    private fun setupNavigationAndBottomNav() {
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        val navInflater = navController.navInflater
        val navGraph = navInflater.inflate(R.navigation.movie_navigation)

        val sharedPref = getSharedPreferences(APP_PREF, MODE_PRIVATE)
        val onboardingCompleted = sharedPref.getBoolean(ONBOARDING_COMPLETED, false)

        navGraph.setStartDestination(
            if (onboardingCompleted) R.id.homeFragment else R.id.onboarding1Fragment
        )

        navController.graph = navGraph

        binding.bottomNavigation.setupWithNavController(navController)
        setBottomNavigationVisibility(navController)
        setNavigationController(navController)
    }

    private fun setBottomNavigationVisibility(navController: NavController) {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            binding.bottomNavigation.isVisible =
                destination.id == R.id.homeFragment ||
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
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}