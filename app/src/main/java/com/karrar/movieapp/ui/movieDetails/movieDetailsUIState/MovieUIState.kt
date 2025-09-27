package com.karrar.movieapp.ui.movieDetails.movieDetailsUIState

import com.karrar.movieapp.ui.models.ActorUiState
import com.karrar.movieapp.ui.models.MediaUiState
import com.karrar.movieapp.ui.movieDetails.MediaInfoUiState

data class MovieUIState(
    val movieDetailsResult: MediaInfoUiState = MediaInfoUiState(),
    val movieCastResult: List<ActorUiState> = emptyList(),
    val similarMoviesResult: List<MediaUiState> = emptyList(),
    val movieReview: List<ReviewUIState> = emptyList(),
    val detailItemResult: List<DetailItemUIState> = mutableListOf(),
    val ratingValue: Float = 0F,
    val isLoading: Boolean = false,
    val isLogin: Boolean = false,
    val errorUIStates: List<ErrorUIState> = emptyList(),
)