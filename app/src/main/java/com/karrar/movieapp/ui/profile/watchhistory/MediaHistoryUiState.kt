package com.karrar.movieapp.ui.profile.watchhistory

data class MediaHistoryUiState(
    val id: Int,
    var posterPath: String,
    var movieTitle: String,
    var voteAverage: String,
    var releaseDate: String,
    var movieDuration: Int,
    var mediaType: String,
    val category: String
)