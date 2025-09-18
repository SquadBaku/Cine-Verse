package com.karrar.movieapp.ui.movieDetails.movieDetailsUIState

import androidx.lifecycle.ViewModel
import com.karrar.movieapp.ui.models.ActorUiState
import com.karrar.movieapp.ui.models.MediaUiState
import com.karrar.movieapp.ui.movieDetails.MediaInfoUiState

sealed class DetailItemUIState(val priority: Int) {

    class Header(val data: MediaInfoUiState?) : DetailItemUIState(0)

    class Cast(val data: List<ActorUiState>) : DetailItemUIState(1)

    class SimilarMovies(val data: List<MediaUiState>) : DetailItemUIState(2)
    object Promotion : DetailItemUIState(3)

    class Comment(val data: ReviewUIState) : DetailItemUIState(6)

    class Rating(val viewModel: ViewModel) : DetailItemUIState(4)

    object ReviewText : DetailItemUIState(5)

    object SeeAllReviewsButton : DetailItemUIState(7)

}