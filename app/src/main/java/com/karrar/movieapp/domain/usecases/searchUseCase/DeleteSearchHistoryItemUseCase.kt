package com.karrar.movieapp.domain.usecases.searchUseCase

import com.karrar.movieapp.data.repository.MovieRepository
import javax.inject.Inject

class DeleteSearchHistoryItemUseCase @Inject constructor(
    private val movieRepository: MovieRepository,
) {
    suspend operator fun invoke(id: Long) =
        movieRepository.deleteSearchHistoryItemById(id = id)
}