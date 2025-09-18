package com.karrar.movieapp.domain.usecases.searchUseCase

import com.karrar.movieapp.data.repository.MovieRepository
import com.karrar.movieapp.domain.mappers.search.SearchHistoryMapper
import com.karrar.movieapp.domain.models.SearchHistory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject


class DeleteAllSearchHistoryUseCase @Inject constructor(
    private val movieRepository: MovieRepository,
) {
    suspend operator fun invoke() = movieRepository.deleteAllSearchHistory()
}