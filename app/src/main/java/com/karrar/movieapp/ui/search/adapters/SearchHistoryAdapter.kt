package com.karrar.movieapp.ui.search.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.karrar.movieapp.BR
import com.karrar.movieapp.ui.adapters.DetailedMediaAdapter
import com.karrar.movieapp.ui.adapters.MediaInteractionListener
import com.karrar.movieapp.ui.base.BaseAdapter
import com.karrar.movieapp.ui.base.BaseInteractionListener
import com.karrar.movieapp.ui.search.SearchItem


class SearchHistoryAdapter(
    private var searchItems: MutableList<SearchItem>,
    private val listener: BaseInteractionListener
) : BaseAdapter<SearchItem>(searchItems, listener) {
    override val layoutID: Int = 0

    fun setItem(item: SearchItem) {
        val newItems = searchItems.apply {
            removeAt(item.priority)
            add(item.priority, item)
        }
        setItems(newItems)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return ItemViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context), viewType, parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        if (searchItems.isNotEmpty())
            bind(holder as ItemViewHolder, position)
    }

    override fun bind(holder: ItemViewHolder, position: Int) {
        if (position != -1)
            when (val currentItem = searchItems[position]) {
                is SearchItem.RecentSearch -> {
                    holder.binding.apply {
                        setVariable(BR.item, currentItem.item)
                        setVariable(BR.listener, listener as SearchHistoryInteractionListener)
                    }
                }

                is SearchItem.RecentlyViewed -> {
                    holder.binding.apply {
                        setVariable(
                            BR.adapterRecycler,
                            DetailedMediaAdapter(
                                currentItem.media,
                                com.karrar.movieapp.R.layout.recently_viewed_item,
                                listener as MediaInteractionListener
                            )
                        )
                        setVariable(BR.viewModel, listener as SearchHistoryInteractionListener)
                    }
                }
            }
    }

    override fun setItems(newItems: List<SearchItem>) {
        searchItems = newItems.sortedBy { it.priority }.toMutableList()
        super.setItems(searchItems)
    }

    override fun areItemsSame(oldItem: SearchItem, newItem: SearchItem): Boolean {
        return oldItem.priority == newItem.priority
    }

    override fun areContentSame(
        oldPosition: SearchItem,
        newPosition: SearchItem,
    ): Boolean {
        return oldPosition == newPosition
    }

    override fun getItemViewType(position: Int): Int {
        if (searchItems.isNotEmpty()) {
            return when (searchItems[position]) {
                is SearchItem.RecentSearch -> com.karrar.movieapp.R.layout.item_search_history
                is SearchItem.RecentlyViewed -> com.karrar.movieapp.R.layout.search_recently_viewed_list
            }
        }
        return -1
    }
}


interface SearchHistoryInteractionListener : BaseInteractionListener {
    fun onClickSearchHistory(name: String)
    fun onClickDeleteSearchHistoryItem(id: Long)
    fun onClickClearAllHistory()
}