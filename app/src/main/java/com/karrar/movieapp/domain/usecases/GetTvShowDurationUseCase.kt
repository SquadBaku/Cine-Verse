package com.karrar.movieapp.domain.usecases
import com.karrar.movieapp.data.repository.SeriesRepository
import javax.inject.Inject

class GetTVShowDurationUseCase @Inject constructor(
    private val seriesRepository: SeriesRepository
) {
    suspend operator fun invoke(tvShowId: Int): Int {
        return seriesRepository.getTVShowDuration(tvId = tvShowId)
    }
}