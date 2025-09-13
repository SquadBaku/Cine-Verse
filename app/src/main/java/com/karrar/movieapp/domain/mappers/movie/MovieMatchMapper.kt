package com.karrar.movieapp.domain.mappers.movie

import com.karrar.movieapp.BuildConfig
import com.karrar.movieapp.data.remote.response.MovieDto
import com.karrar.movieapp.domain.mappers.Mapper
import com.karrar.movieapp.domain.models.MovieMatch
import javax.inject.Inject

class MovieMatchMapper @Inject constructor() : Mapper<MovieDto, MovieMatch> {
    override fun map(input: MovieDto): MovieMatch {
        return MovieMatch(
            movieId = input.id ?: 0,
            movieImage = BuildConfig.IMAGE_BASE_PATH + input.posterPath,
            movieName = input.originalTitle ?: "",
            movieGenres = input.genreIds?.map { it.toString() } ?: emptyList(),
            movieReleaseDate = input.releaseDate ?:  " ",
            movieVoteAverage =   input.voteAverage.toString()
        )
    }
}