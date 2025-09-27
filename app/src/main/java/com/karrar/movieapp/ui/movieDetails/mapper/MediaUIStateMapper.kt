package com.karrar.movieapp.ui.movieDetails.mapper

import com.karrar.movieapp.domain.mappers.Mapper
import com.karrar.movieapp.domain.models.Media
import com.karrar.movieapp.ui.models.MediaUiState
import java.util.Locale
import javax.inject.Inject

class MediaUIStateMapper @Inject constructor() : Mapper<Media, MediaUiState> {
    override fun map(input: Media): MediaUiState {
        return MediaUiState(
            id = input.mediaID,
            imageUrl = input.mediaImage,
            rating = String.format(Locale.US, "%.1f", input.mediaRate),
            title = input.mediaName
        )
    }
}