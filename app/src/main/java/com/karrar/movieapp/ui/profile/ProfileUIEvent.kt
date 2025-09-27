package com.karrar.movieapp.ui.profile

sealed interface ProfileUIEvent {
    object LoginEvent : ProfileUIEvent
    object RatedMoviesEvent : ProfileUIEvent
    object DialogLogoutEvent : ProfileUIEvent
    object DialogLanguageEvent : ProfileUIEvent
    object WatchHistoryEvent : ProfileUIEvent
    object MyCollectionsEvent : ProfileUIEvent
    object EditProfileEvent : ProfileUIEvent
}