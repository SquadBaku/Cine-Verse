package com.karrar.movieapp.ui.profile.watchhistory

import com.karrar.movieapp.R
import com.karrar.movieapp.ui.base.BaseAdapter
import com.karrar.movieapp.ui.base.BaseInteractionListener
import android.view.View


class WatchHistoryAdapter(
    items: List<MediaHistoryUiState>,
    listener: WatchHistoryInteractionListener,
) : BaseAdapter<MediaHistoryUiState>(items, listener) {

    override val layoutID: Int = R.layout.item_watch_history

    fun getItem(position: Int): MediaHistoryUiState? =
        getItems().getOrNull(position)

    override fun removeAt(position: Int): MediaHistoryUiState? =
        super.removeAt(position)


    override fun foregroundOf(holder: BaseViewHolder): View? =
        holder.itemView.findViewById(R.id.foreground)
}


interface WatchHistoryInteractionListener : BaseInteractionListener {
    fun onClickMovie(item: MediaHistoryUiState)
    fun onSwipeDelete(item: MediaHistoryUiState) { }
}