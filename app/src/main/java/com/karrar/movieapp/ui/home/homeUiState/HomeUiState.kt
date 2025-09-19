package com.karrar.movieapp.ui.home.homeUiState

import com.karrar.movieapp.ui.home.HomeItem

data class HomeUiState (
    val popularMovies: HomeItem = HomeItem.Slider(emptyList()),
    val nowStreamingMovies: HomeItem = HomeItem.NowStreaming(emptyList()),
    val upcomingMovies: HomeItem = HomeItem.Upcoming(emptyList()),
    val matchVibeMovie: HomeItem = HomeItem.MatchYourVibe(emptyList()),
    val topRatedMovie: HomeItem =  HomeItem.TopRatedMovie(emptyList()),
    val recentlyViewed: HomeItem = HomeItem.RecentlyViewed(emptyList()),

    val trendingMovies: HomeItem = HomeItem.Trending(emptyList()),
    val adventureMovies: HomeItem = HomeItem.Adventure(emptyList()),
    val mysteryMovies: HomeItem = HomeItem.Mystery(emptyList()),
    val onTheAiringSeries: HomeItem = HomeItem.OnTheAiring(emptyList()),
    val airingTodaySeries: HomeItem = HomeItem.AiringToday(emptyList()),
    val tvShowsSeries: HomeItem = HomeItem.TvShows(emptyList()),
    val actors: HomeItem = HomeItem.Actor(emptyList()),
    val collections: HomeItem = HomeItem.Collections(emptyList()),
    val isLoading:Boolean = false,
    val error : List<String> = emptyList(),
)