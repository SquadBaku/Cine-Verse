package com.karrar.movieapp.ui.search

import com.karrar.movieapp.ui.search.mediaSearchUIState.MediaUIState

sealed interface SearchUIEvent {
    data class ClickMediaEvent(val mediaUIState: MediaUIState) : SearchUIEvent
    data class ClickActorEvent(val actorID: Int) : SearchUIEvent
    data class ClickMovieEvent(val movieID: Int) : SearchUIEvent
    object ClickBackEvent : SearchUIEvent
    object ClickRetryEvent : SearchUIEvent
}
