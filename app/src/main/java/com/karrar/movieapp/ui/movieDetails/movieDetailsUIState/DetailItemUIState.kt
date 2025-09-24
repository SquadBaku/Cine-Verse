package com.karrar.movieapp.ui.movieDetails.movieDetailsUIState

import androidx.lifecycle.ViewModel
import com.karrar.movieapp.ui.models.ActorUiState
import com.karrar.movieapp.ui.models.MediaUiState
import com.karrar.movieapp.ui.movieDetails.MediaInfoUiState

sealed class DetailItemUIState(val priority: Int) {
    class Poster(val data: MediaInfoUiState?) : DetailItemUIState(0)

    class Header(val data: MediaInfoUiState?) : DetailItemUIState(1)

    class Cast(val data: List<ActorUiState>) : DetailItemUIState(2)

    class SimilarMovies(val data: List<MediaUiState>) : DetailItemUIState(3)
    object Promotion : DetailItemUIState(4)

    class Comment(val data: ReviewUIState) : DetailItemUIState(5)

    class Rating(val viewModel: ViewModel) : DetailItemUIState(6)

    object ReviewText : DetailItemUIState(7)

    object SeeAllReviewsButton : DetailItemUIState(8)

}