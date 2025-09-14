package com.karrar.movieapp.ui.movieDetails

data class MediaInfoUiState(
    val id: Int = 0,
    val image: String = "",
    val mediaType: String = "",
    val title: String = "",
    val genres: String = "",
    val rating: String = "",
    val date: String = "",
    val hours: Int = 0,
    val minutes: Int = 0,
    val formattedDuration: String = "${hours}h ${minutes}m",
)
