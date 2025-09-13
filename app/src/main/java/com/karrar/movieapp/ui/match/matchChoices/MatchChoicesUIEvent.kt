package com.karrar.movieapp.ui.match.matchChoices

sealed interface MatchChoicesUIEvent {
    object DoneLoadingDataEvent : MatchChoicesUIEvent
}