package com.karrar.movieapp.domain.usecases.movieDetails

import com.karrar.movieapp.data.repository.MovieRepository
import javax.inject.Inject

class GetMovieDurationUseCase @Inject constructor(
    private val movieRepository: MovieRepository
) {
    suspend operator fun invoke(movieId: Int): Int {
        return movieRepository.getMovieDuration(movieId)
    }
}