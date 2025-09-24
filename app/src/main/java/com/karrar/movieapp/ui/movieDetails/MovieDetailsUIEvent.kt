package com.karrar.movieapp.ui.movieDetails

sealed interface MovieDetailsUIEvent {
    object ClickBackEvent : MovieDetailsUIEvent
    object ClickPlayTrailerEvent : MovieDetailsUIEvent
    data class ClickSaveEvent(val isLoggedIn: Boolean) : MovieDetailsUIEvent
    object MessageAppear : MovieDetailsUIEvent
    data class RateTheMovie(val isLoggedIn: Boolean): MovieDetailsUIEvent
    object DismissSheet : MovieDetailsUIEvent
    object ClickReviewsEvent : MovieDetailsUIEvent
    data class ClickMovieEvent(val movieID: Int) : MovieDetailsUIEvent
    data class ClickCastEvent(val castID: Int) : MovieDetailsUIEvent

}