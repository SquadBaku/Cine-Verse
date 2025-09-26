package com.karrar.movieapp.ui.search.uiStatMapper

import com.karrar.movieapp.domain.mappers.Mapper
import com.karrar.movieapp.domain.models.SearchHistory
import com.karrar.movieapp.ui.search.mediaSearchUIState.SearchHistoryUIState
import javax.inject.Inject


class SearchHistoryUIStateMapper @Inject constructor(): Mapper<SearchHistory, SearchHistoryUIState> {
    override fun map(input: SearchHistory): SearchHistoryUIState {
        return SearchHistoryUIState(
            id = input.id,
            name = input.name
        )
    }
}