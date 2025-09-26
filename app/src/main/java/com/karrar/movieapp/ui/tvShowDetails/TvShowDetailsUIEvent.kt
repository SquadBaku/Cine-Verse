package com.karrar.movieapp.ui.tvShowDetails


sealed interface TvShowDetailsUIEvent {
    object ClickBackEvent : TvShowDetailsUIEvent
    object ClickPlayTrailerEvent : TvShowDetailsUIEvent
    object MessageAppear : TvShowDetailsUIEvent
    object ClickReviewsEvent : TvShowDetailsUIEvent
    data class RateTheMovie(val isLoggedIn: Boolean): TvShowDetailsUIEvent
    object DismissSheet : TvShowDetailsUIEvent
    data class ClickSeasonEvent(val seasonId: Int) : TvShowDetailsUIEvent
    data class ClickCastEvent(val castID: Int) : TvShowDetailsUIEvent
    object ClickShowMoreSeasonsEvent : TvShowDetailsUIEvent
}