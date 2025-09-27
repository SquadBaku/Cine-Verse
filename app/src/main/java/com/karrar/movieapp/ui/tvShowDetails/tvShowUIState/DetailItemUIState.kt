package com.karrar.movieapp.ui.tvShowDetails.tvShowUIState

import com.karrar.movieapp.ui.models.ActorUiState
import com.karrar.movieapp.ui.models.MediaUiState
import com.karrar.movieapp.ui.movieDetails.MediaInfoUiState
import com.karrar.movieapp.ui.movieDetails.movieDetailsUIState.BehindTheScenesState

sealed class DetailItemUIState(val priority: Int) {
    class Poster(val data: MediaInfoUiState) : DetailItemUIState(1)
    class Header(val data: MediaInfoUiState) : DetailItemUIState(2)

    class Seasons(val data: List<SeasonUIState>) : DetailItemUIState(3)
    class Cast(val data: List<ActorUiState>) : DetailItemUIState(4)

    class BehindScenes(val data: BehindTheScenesState) : DetailItemUIState(5)

    class SimilarTvShows(val data: List<MediaUiState>) : DetailItemUIState(6)
    object Promotion : DetailItemUIState(7)

    object ReviewText : DetailItemUIState(8)

    class Comment(val data: ReviewUIState) : DetailItemUIState(9)

}
