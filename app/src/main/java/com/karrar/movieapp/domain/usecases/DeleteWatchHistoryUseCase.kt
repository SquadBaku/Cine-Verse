package com.karrar.movieapp.domain.usecases

import com.karrar.movieapp.data.repository.WatchHistoryRepository
import com.karrar.movieapp.domain.enums.MediaType
import javax.inject.Inject

class DeleteWatchHistoryUseCase @Inject constructor(
    private val repo: WatchHistoryRepository
) {
    suspend operator fun invoke(id: Int, type: MediaType) = repo.deleteWatchHistory(id, type)
}
