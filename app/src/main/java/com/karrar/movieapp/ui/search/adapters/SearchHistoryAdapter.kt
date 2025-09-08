package com.karrar.movieapp.ui.search.adapters

import com.google.firebase.perf.v1.TransportInfoOrBuilder
import com.karrar.movieapp.R
import com.karrar.movieapp.ui.base.*
import com.karrar.movieapp.ui.search.mediaSearchUIState.SearchHistoryUIState


class SearchHistoryAdapter(items: List<SearchHistoryUIState>, listener: SearchHistoryInteractionListener)
    : BaseAdapter<SearchHistoryUIState>(items,listener){
    override val layoutID: Int = R.layout.item_search_history
}

interface SearchHistoryInteractionListener : BaseInteractionListener {
    fun onClickSearchHistory(name: String)
    fun onClickDeleteSearchHistoryItem(id: Long)
    fun onClickClearAllHistory()
}