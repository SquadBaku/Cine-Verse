package com.karrar.movieapp.ui.profile.watchhistory

data class WatchHistoryUiState(
    val allMedia: List<MediaHistoryUiState> = emptyList(),
    val error: List<Error> = emptyList(),
    val loading: Boolean = false,
    val ratedList: List<MediaHistoryUiState> = emptyList()
)

data class Error(
    val code: Int,
    val message: String,
)