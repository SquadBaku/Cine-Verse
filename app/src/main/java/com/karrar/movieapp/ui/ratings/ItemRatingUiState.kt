
package com.karrar.movieapp.ui.ratings

data class ItemRatingUIState(
    val id: Int,
    val title: String,
    val posterPath: String?,
    val voteAverage: String,
    val date: String?,
    val mediaType: String
)
