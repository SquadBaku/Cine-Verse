package com.karrar.movieapp.ui.match.matchChoices

import com.karrar.movieapp.domain.models.MovieMatch

data class MatchChoicesUiState (
    val result: List<MovieMatch>? = null,
    val isLoading:Boolean = false,
    val error : List<String> = emptyList(),
)