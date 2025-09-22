package com.karrar.movieapp.ui.mappers


import com.karrar.movieapp.domain.mappers.Mapper
import com.karrar.movieapp.domain.models.Media
import com.karrar.movieapp.ui.models.MediaUiState
import javax.inject.Inject

class MediaUiMapper @Inject constructor() : Mapper<Media, MediaUiState> {
    override fun map(input: Media): MediaUiState {
        return MediaUiState(
            id = input.mediaID,
            imageUrl = input.mediaImage,
            title = input.mediaName,
            rating = input.mediaRate.toString().take(3)
        )
    }
}