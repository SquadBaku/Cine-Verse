package com.karrar.movieapp.domain.mappers.movie

import com.karrar.movieapp.data.remote.response.RatedMoviesDto
import com.karrar.movieapp.domain.mappers.Mapper
import com.karrar.movieapp.domain.models.Rated
import com.karrar.movieapp.utilities.Constants
import javax.inject.Inject

class RatedMoviesMapper @Inject constructor() : Mapper<RatedMoviesDto, Rated> {
    override fun map(input: RatedMoviesDto): Rated {
        return Rated(
            id = input.id ?: 0,
            title = input.title ?: "",
            posterPath = Constants.IMAGE_BASE_PATH + input.backdropPath,
            userRating = input.rating?.toInt() ?: 0,
            releaseDate = input.releaseDate ?: "",
            mediaType = Constants.MOVIE,
            categoryIdList = input.genreIds?.mapNotNull { it } ?: emptyList(),
            duration = "",
            rating = input.voteAverage?.toFloat() ?: 0f
        )
    }
}