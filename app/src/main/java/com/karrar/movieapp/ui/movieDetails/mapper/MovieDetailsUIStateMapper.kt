package com.karrar.movieapp.ui.movieDetails.mapper

import com.karrar.movieapp.domain.mappers.Mapper
import com.karrar.movieapp.domain.models.MovieDetails
import com.karrar.movieapp.ui.movieDetails.MediaInfoUiState
import javax.inject.Inject

data class MovieDuration(val hours: Int, val minutes: Int)

class MovieDetailsUIStateMapper @Inject constructor() : Mapper<MovieDetails, MediaInfoUiState> {
    override fun map(input: MovieDetails): MediaInfoUiState {
        val duration = formatMovieDuration(input.movieDuration)
        return MediaInfoUiState(
            id = input.movieId,
            image = input.movieImage,
            title = input.movieName,
            date = input.movieReleaseDate,
            genres = input.movieGenres,
            hours = duration.hours,
            minutes = duration.minutes,
            overview = input.movieOverview,
        )
    }

    private fun formatMovieDuration(duration: Int): MovieDuration {
        return MovieDuration(hours = duration.div(60), minutes = duration.rem(60))
    }

}

