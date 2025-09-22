package com.karrar.movieapp.ui.onboarding

sealed class OnboardingUIEvent {
    object NextEvent : OnboardingUIEvent()
    object BackEvent : OnboardingUIEvent()
    object GetStartedEvent : OnboardingUIEvent()
}