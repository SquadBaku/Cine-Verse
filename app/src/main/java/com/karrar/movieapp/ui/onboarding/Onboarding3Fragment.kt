package com.karrar.movieapp.ui.onboarding

import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.karrar.movieapp.R
import com.karrar.movieapp.databinding.FragmentOnboarding3Binding
import com.karrar.movieapp.ui.base.BaseFragment
import com.karrar.movieapp.utilities.Const.APP_PREF
import com.karrar.movieapp.utilities.Const.ONBOARDING_COMPLETED
import com.karrar.movieapp.utilities.collectLast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Onboarding3Fragment : BaseFragment<FragmentOnboarding3Binding>() {
    override val layoutIdFragment = R.layout.fragment_onboarding3
    override val viewModel: OnboardingViewModel by viewModels()
    override fun onStart() {
        super.onStart()
        setTitle(false)
        collectLast(viewModel.onboardingEvent) { it ->
            it.getContentIfNotHandled()?.let { onEvent(it) }
        }
    }

    private fun onEvent(event: OnboardingUIEvent) {
        when (event) {
            is OnboardingUIEvent.GetStartedEvent -> {
                val sharedPref = requireContext().getSharedPreferences(
                    APP_PREF,
                    AppCompatActivity.MODE_PRIVATE
                )
                sharedPref.edit { putBoolean(ONBOARDING_COMPLETED, true) }

                findNavController().navigate(
                    Onboarding3FragmentDirections.actionOnboarding3FragmentToHomeFragment()
                )
            }

            is OnboardingUIEvent.BackEvent -> {
                findNavController().navigate(Onboarding3FragmentDirections.actionOnboarding3FragmentToOnboarding2Fragment())
            }

            else -> {}
        }
    }


}