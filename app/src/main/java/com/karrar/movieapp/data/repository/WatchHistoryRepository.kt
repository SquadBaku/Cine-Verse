package com.karrar.movieapp.data.repository

import com.karrar.movieapp.domain.enums.MediaType

interface WatchHistoryRepository {
    suspend fun deleteWatchHistory(id: Int, type: MediaType)
}
