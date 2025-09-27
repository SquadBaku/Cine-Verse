package com.karrar.movieapp.ui.tvShowDetails.tvShowUIMapper

import com.karrar.movieapp.domain.mappers.Mapper
import com.karrar.movieapp.domain.models.TvShowDetails
import com.karrar.movieapp.ui.movieDetails.MediaInfoUiState
import javax.inject.Inject

class TvShowDetailsResultUIMapper @Inject constructor() : Mapper<TvShowDetails, MediaInfoUiState> {
    override fun map(input: TvShowDetails): MediaInfoUiState {
        return MediaInfoUiState(
            id = input.tvShowId,
            image = input.tvShowImage,
            title = input.tvShowName,
            date = input.tvShowReleaseDate,
            genres = input.tvShowGenres,
            overview = input.tvShowOverview,
            userRating = input.userRating

        )
    }
}
