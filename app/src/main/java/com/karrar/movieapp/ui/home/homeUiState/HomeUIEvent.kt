package com.karrar.movieapp.ui.home.homeUiState

import com.karrar.movieapp.domain.enums.AllMediaType
import com.karrar.movieapp.ui.myList.myListUIState.CreatedListUIState

sealed interface HomeUIEvent {
    object ClickSeeAllActorEvent : HomeUIEvent
    object ClickSeeAllRecentlyViewed : HomeUIEvent
    object ClickSeeAllCollections : HomeUIEvent
    object ClickWhatShouldIWatch : HomeUIEvent
    data class ClickMovieEvent(val movieID: Int) : HomeUIEvent
    data class ClickActorEvent(val actorID: Int) : HomeUIEvent
    data class ClickSeriesEvent(val seriesID: Int) : HomeUIEvent
    data class ClickSeeAllMovieEvent(val mediaType: AllMediaType) : HomeUIEvent
    data class ClickSeeAllTVShowsEvent(val mediaType: AllMediaType) : HomeUIEvent
    data class ClickListEvent(val createdListUIState: CreatedListUIState) : HomeUIEvent
}