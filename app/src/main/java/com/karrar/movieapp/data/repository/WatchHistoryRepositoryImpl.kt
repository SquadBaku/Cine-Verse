package com.karrar.movieapp.data.repository

import com.karrar.movieapp.data.remote.service.MovieService
import com.karrar.movieapp.domain.enums.MediaType

import javax.inject.Inject

class WatchHistoryRepositoryImp @Inject constructor(
    private val movieService: MovieService
) : WatchHistoryRepository {

    override suspend fun deleteWatchHistory(id: Int, type: MediaType) {
        when (type) {
            MediaType.MOVIE -> movieService.deleteRating(id)
            MediaType.TV_SHOW    -> movieService.deleteTvShowRating(id)
        }.also { res ->
            if (!res.isSuccessful) {
                throw IllegalStateException("Delete history failed: ${res.code()} ${res.message()}")
            }
        }
    }
}
