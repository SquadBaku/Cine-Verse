package com.karrar.movieapp.ui.profile.myratings

import com.karrar.movieapp.domain.models.Genre
import com.karrar.movieapp.utilities.GenreUiState

data class MyRateUIState(
    val isLoading: Boolean = false,
    val ratedList: List<RatedUIState> = emptyList(),
    val error: List<String> = emptyList(),
    val genreList: List<Genre> = emptyList(),
    val isListEmpty: Boolean = false,
    val isInitialLoad: Boolean = false,
    val hasLoadedOnce: Boolean = false,
    val userRating: Float = 0f
)
