package com.karrar.movieapp.ui.search

import com.karrar.movieapp.ui.search.mediaSearchUIState.MediaUIState
import com.karrar.movieapp.ui.search.mediaSearchUIState.SearchHistoryUIState

sealed class SearchItem(val priority: Int) {

    data class RecentSearch(val item: SearchHistoryUIState, val type: SearchItemType) : SearchItem(0)
    data class RecentlyViewed(val media: List<MediaUIState>, val type: SearchItemType) : SearchItem(1)

}