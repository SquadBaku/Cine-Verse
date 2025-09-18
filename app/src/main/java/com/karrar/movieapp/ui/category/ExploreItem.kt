package com.karrar.movieapp.ui.category

import com.karrar.movieapp.ui.category.uiState.GenreUIState
import com.karrar.movieapp.ui.search.mediaSearchUIState.MediaUIState

sealed class ExploreItem(val priority: Int) {
    data class GenreItem(
        val genres: List<GenreUIState>,
        val selectedId: Int
    ) : ExploreItem(priority = 0)

    data class MediaGrid(
        val mediaList: List<MediaUIState>
    ) : ExploreItem(priority = 1)
}