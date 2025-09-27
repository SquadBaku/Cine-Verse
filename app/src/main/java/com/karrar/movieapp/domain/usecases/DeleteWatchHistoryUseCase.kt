package com.karrar.movieapp.domain.usecases

import com.karrar.movieapp.data.repository.MovieRepository
import com.karrar.movieapp.domain.enums.MediaType
import javax.inject.Inject

class DeleteWatchHistoryUseCase @Inject constructor(
    private val repo: MovieRepository
) {
    suspend operator fun invoke(id: Int, type: MediaType) = repo.deleteWatchHistoryItemById(id)
}
