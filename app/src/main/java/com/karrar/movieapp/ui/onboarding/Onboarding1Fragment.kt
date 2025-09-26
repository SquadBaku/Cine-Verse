package com.karrar.movieapp.ui.onboarding

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.karrar.movieapp.R
import com.karrar.movieapp.databinding.FragmentOnboarding1Binding
import com.karrar.movieapp.ui.base.BaseFragment
import com.karrar.movieapp.utilities.collectLast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Onboarding1Fragment : BaseFragment<FragmentOnboarding1Binding>() {
    override val layoutIdFragment = R.layout.fragment_onboarding1
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
                findNavController().navigate(Onboarding1FragmentDirections.actionOnboarding1FragmentToOnboarding2Fragment())
            }

            else -> {}

        }
    }
}