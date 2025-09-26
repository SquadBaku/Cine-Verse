package com.karrar.movieapp.domain.usecases

import com.karrar.movieapp.data.repository.MovieRepository
import com.karrar.movieapp.domain.mappers.movie.MovieMatchMapper
import com.karrar.movieapp.domain.models.MovieMatch
import javax.inject.Inject

class GetMatchMovieListUseCase @Inject constructor(
    private val movieRepository: MovieRepository,
    private val movieMatchMapper: MovieMatchMapper
) {

    suspend operator fun invoke(
        genres: List<String>?,
        withRuntimeGte: Int?,
        withRuntimeLte: Int?,
        primaryReleaseDateGte: String?,
        primaryReleaseDateLte: String?,
    ): List<MovieMatch> {
        val response = movieRepository.getMatchListMovie(
            genres = genres,
            withRuntimeLte = withRuntimeLte,
            withRuntimeGte = withRuntimeGte,
            primaryReleaseDateLte = primaryReleaseDateLte,
            primaryReleaseDateGte = primaryReleaseDateGte
        )

        return response?.map {
            movieMatchMapper.map(it)
        } ?: emptyList()
    }
}