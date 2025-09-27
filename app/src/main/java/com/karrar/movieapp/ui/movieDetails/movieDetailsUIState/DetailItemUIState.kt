package com.karrar.movieapp.ui.movieDetails.movieDetailsUIState

import com.karrar.movieapp.ui.models.ActorUiState
import com.karrar.movieapp.ui.models.MediaUiState
import com.karrar.movieapp.ui.movieDetails.MediaInfoUiState

sealed class DetailItemUIState(val priority: Int) {
    class Poster(val data: MediaInfoUiState?) : DetailItemUIState(0)

    class Header(val data: MediaInfoUiState?) : DetailItemUIState(1)

    class Cast(val data: List<ActorUiState>) : DetailItemUIState(2)
    class BehindScenes(val data: BehindTheScenesState): DetailItemUIState(3)

    class SimilarMovies(val data: List<MediaUiState>) : DetailItemUIState(4)
    object Promotion : DetailItemUIState(5)

    object ReviewText : DetailItemUIState(6)
    class Comment(val data: ReviewUIState) : DetailItemUIState(7)




}