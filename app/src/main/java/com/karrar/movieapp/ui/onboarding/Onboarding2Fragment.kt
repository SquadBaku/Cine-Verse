package com.karrar.movieapp.ui.onboarding

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.karrar.movieapp.R
import com.karrar.movieapp.databinding.FragmentOnboarding2Binding
import com.karrar.movieapp.ui.base.BaseFragment
import com.karrar.movieapp.utilities.collectLast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Onboarding2Fragment : BaseFragment<FragmentOnboarding2Binding>() {
    override val layoutIdFragment = R.layout.fragment_onboarding2
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
            is OnboardingUIEvent.NextEvent -> {
                findNavController().navigate(Onboarding2FragmentDirections.actionOnboarding2FragmentToOnboarding3Fragment())
            }

            is OnboardingUIEvent.BackEvent -> {
                findNavController().navigate(Onboarding2FragmentDirections.actionOnboarding2FragmentToOnboarding1Fragment())
            }

            else -> {}

        }
    }

}