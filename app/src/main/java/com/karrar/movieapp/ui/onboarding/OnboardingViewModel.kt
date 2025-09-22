package com.karrar.movieapp.ui.onboarding

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.karrar.movieapp.utilities.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    state: SavedStateHandle
) : ViewModel() {

    private val _onboardingEvent = MutableStateFlow<Event<OnboardingUIEvent?>>(Event(null))
    val onboardingEvent = _onboardingEvent.asStateFlow()

    fun onClickNext() {
        _onboardingEvent.update { Event(OnboardingUIEvent.NextEvent) }
    }

    fun onClickBack() {
        _onboardingEvent.update { Event(OnboardingUIEvent.BackEvent) }
    }

    fun onClickGetStarted() {
        _onboardingEvent.update { Event(OnboardingUIEvent.GetStartedEvent) }
    }

}
