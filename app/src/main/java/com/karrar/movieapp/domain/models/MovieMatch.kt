package com.karrar.movieapp.domain.models

data class MovieMatch(
    val movieId: Int,
    val movieImage: String,
    val movieName: String,
    val movieGenres: List<String>,
    val movieReleaseDate: String,
    val movieVoteAverage: String,
)