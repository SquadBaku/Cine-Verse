package com.karrar.movieapp.ui.search.adapters

import androidx.recyclerview.widget.DiffUtil
import com.karrar.movieapp.R
import com.karrar.movieapp.ui.adapters.MediaInteractionListener
import com.karrar.movieapp.ui.base.*
import com.karrar.movieapp.ui.search.mediaSearchUIState.MediaUIState
import com.karrar.movieapp.utilities.ViewMode


class MediaSearchAdapter(
    listener: MediaInteractionListener,
    private var viewMode: ViewMode = ViewMode.GRID
)
    : BasePagingAdapter<MediaUIState>(MediaSearchComparator, listener){
    override val layoutID: Int
        get() = if (viewMode == ViewMode.GRID) {
            R.layout.item_explore_grid
        } else {
            R.layout.item_explore_list
        }

    override fun getItemViewType(position: Int): Int {
        return layoutID
    }

    fun setViewMode(mode: ViewMode) {
        viewMode = mode
        notifyDataSetChanged()
    }

    object MediaSearchComparator : DiffUtil.ItemCallback<MediaUIState>(){
        override fun areItemsTheSame(oldItem: MediaUIState, newItem: MediaUIState) =
            oldItem.mediaID == newItem.mediaID

        override fun areContentsTheSame(oldItem: MediaUIState, newItem: MediaUIState) =
            oldItem == newItem
    }
}

interface MediaSearchInteractionListener : BaseInteractionListener {
    fun onClickMediaResult(media: MediaUIState)
}