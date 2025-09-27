package com.karrar.movieapp.ui.ratings

data class RatingUIState(
    val statusCode: Int = 0,
    val statusMessage: String = "",
    val error: String? = null,
    val isLoading: Boolean = false,
    val ratings: List<ItemRatingUIState> = emptyList(),
    val isInitialLoad: Boolean = true,
    val hasLoadedOnce: Boolean = false
)