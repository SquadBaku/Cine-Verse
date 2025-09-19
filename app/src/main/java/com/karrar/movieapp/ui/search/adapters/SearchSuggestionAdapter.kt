package com.karrar.movieapp.ui.search.adapters

import com.karrar.movieapp.R
import com.karrar.movieapp.ui.base.BaseAdapter
import com.karrar.movieapp.ui.search.mediaSearchUIState.SearchHistoryUIState

class SearchSuggestionAdapter(items: List<SearchHistoryUIState>, listener: SearchHistoryInteractionListener)
    : BaseAdapter<SearchHistoryUIState>(items,listener){
    override val layoutID: Int = R.layout.item_search_suggestion
}
