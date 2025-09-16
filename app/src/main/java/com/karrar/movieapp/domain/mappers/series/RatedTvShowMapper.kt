package com.karrar.movieapp.domain.mappers.series

import com.karrar.movieapp.BuildConfig
import com.karrar.movieapp.data.remote.response.RatedMoviesDto
import com.karrar.movieapp.data.remote.response.RatedTvShowDto
import com.karrar.movieapp.domain.mappers.Mapper
import com.karrar.movieapp.domain.models.Rated
import com.karrar.movieapp.utilities.Constants
import javax.inject.Inject

class RatedTvShowMapper @Inject constructor() : Mapper<RatedTvShowDto, Rated> {
    override fun map(input: RatedTvShowDto): Rated {
        return Rated(
            id = input.id ?: 0,
            title = input.title ?: "",
            posterPath = Constants.IMAGE_BASE_PATH + (input.backdropPath ?: ""),
            rating = input.rating ?: 0f,
            releaseDate = input.firstAirDate ?: "",
            mediaType = Constants.TV_SHOWS,
            categoryIdList = input.genreIds?.filterNotNull() ?: emptyList(),
            duration = "",
            userRating = input.rating?.toInt()?:0
        )
    }
}