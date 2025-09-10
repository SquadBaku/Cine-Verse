package com.karrar.movieapp.ui.search.mediaSearchUIState

import androidx.paging.PagingData
import com.karrar.movieapp.ui.allMedia.Error
import com.karrar.movieapp.ui.search.SearchItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow


data class MediaSearchUIState(
    val searchInput: String = "",
    val searchTypes: MediaTypes = MediaTypes.MOVIE,
    val searchResult: Flow<PagingData<MediaUIState>> = emptyFlow(),
    val searchHistory: List<SearchHistoryUIState> = emptyList(),
    val recentlyViewed: List<MediaUIState> = emptyList(),
    val items: List<SearchItem> = emptyList(),
    val isLoading : Boolean = false,
    val isEmpty: Boolean = false,
    val error : List<Error> = emptyList(),
)