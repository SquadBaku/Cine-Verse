package com.karrar.movieapp.ui.adapters

import com.karrar.movieapp.ui.base.BaseAdapter
import com.karrar.movieapp.ui.search.mediaSearchUIState.MediaUIState

class DetailedMediaAdapter(
    items: List<MediaUIState>,
    layout: Int,
    listener: MediaInteractionListener
) :
    BaseAdapter<MediaUIState>(items, listener) {
    override val layoutID: Int = layout
}
