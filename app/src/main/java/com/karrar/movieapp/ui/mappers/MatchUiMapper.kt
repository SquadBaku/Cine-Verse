package com.karrar.movieapp.ui.mappers


import com.karrar.movieapp.domain.mappers.Mapper
import com.karrar.movieapp.domain.models.Media
import com.karrar.movieapp.domain.models.MovieMatch
import com.karrar.movieapp.ui.models.MediaUiState
import javax.inject.Inject

class MatchUiMapper @Inject constructor() : Mapper<MovieMatch, MediaUiState> {
    override fun map(input: MovieMatch): MediaUiState {
        return MediaUiState(
            id = input.movieId,
            imageUrl = input.movieImage,
            title = input.movieName,
            rating = input.movieVoteAverage.take(3)
        )
    }
}