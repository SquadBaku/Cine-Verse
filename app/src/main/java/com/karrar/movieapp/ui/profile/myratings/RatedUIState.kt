package com.karrar.movieapp.ui.profile.myratings

data class RatedUIState(
    val id: Int,
    val title: String,
    val rating: String,
    val posterPath: String,
    var mediaType: String = "",
    val releaseDate: String,
    val duration: String,
    val category: String,
    val userRating: String = ""

)