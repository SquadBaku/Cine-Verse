package com.karrar.movieapp.ui.home

import com.karrar.movieapp.domain.enums.HomeItemsType
import com.karrar.movieapp.ui.models.ActorUiState
import com.karrar.movieapp.ui.home.homeUiState.PopularUiState
import com.karrar.movieapp.ui.models.MediaUiState
import com.karrar.movieapp.ui.myList.myListUIState.CreatedListUIState
import com.karrar.movieapp.ui.profile.watchhistory.MediaHistoryUiState

sealed class HomeItem(val priority: Int) {

    data class Slider(val items: List<PopularUiState>) : HomeItem(0)
    data class NowStreaming(val items: List<MediaUiState>, val type: HomeItemsType = HomeItemsType.NOW_STREAMING) : HomeItem(1)
    data class Upcoming(val items: List<MediaUiState>, val type: HomeItemsType = HomeItemsType.UPCOMING) : HomeItem(2)
    data class MatchYourVibe(val items : List<MediaUiState> ,  val type: HomeItemsType = HomeItemsType.MATCH_YOUR_VIBE) : HomeItem(3)
    data class TopRatedMovie (val items : List<MediaUiState> , val type: HomeItemsType = HomeItemsType.TOP_RATED_MOVIE) : HomeItem(4)
    data class RecentlyViewed(val items: List<MediaHistoryUiState>, val type: HomeItemsType = HomeItemsType.RECENTLY_VIEWED) : HomeItem(5)

    data class TvShows(val items: List<MediaUiState>) : HomeItem(6)

    data class OnTheAiring(val items: List<MediaUiState>, val type: HomeItemsType = HomeItemsType.ON_THE_AIR) : HomeItem(7)

    data class Trending(val items: List<MediaUiState>, val type: HomeItemsType = HomeItemsType.TRENDING) : HomeItem(8)

    data class AiringToday(val items: List<MediaUiState>) : HomeItem(9)

    data class Mystery(val items: List<MediaUiState>, val type: HomeItemsType = HomeItemsType.MYSTERY) : HomeItem(10)

    data class Adventure(val items: List<MediaUiState>, val type: HomeItemsType = HomeItemsType.ADVENTURE) : HomeItem(11)

    data class Actor(val items: List<ActorUiState>) : HomeItem(12)

    data class Collections(val items: List<CreatedListUIState>, val type: HomeItemsType = HomeItemsType.COLLECTIONS) : HomeItem(13)

    data class WhatShouldIWatch(val position: Int) : HomeItem(position)

    data class NeedMoreToWatch(val position: Int) : HomeItem(position)
}