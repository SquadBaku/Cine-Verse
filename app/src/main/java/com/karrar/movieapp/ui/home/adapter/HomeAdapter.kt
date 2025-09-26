package com.karrar.movieapp.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.karrar.movieapp.BR
import com.karrar.movieapp.R
import com.karrar.movieapp.domain.enums.HomeItemsType
import com.karrar.movieapp.ui.adapters.ActorAdapter
import com.karrar.movieapp.ui.adapters.ActorsInteractionListener
import com.karrar.movieapp.ui.adapters.MediaAdapter
import com.karrar.movieapp.ui.adapters.MediaInteractionListener
import com.karrar.movieapp.ui.adapters.MovieAdapter
import com.karrar.movieapp.ui.adapters.MovieInteractionListener
import com.karrar.movieapp.ui.adapters.SeriesAdapter
import com.karrar.movieapp.ui.adapters.SeriesInteractionListener
import com.karrar.movieapp.ui.base.BaseAdapter
import com.karrar.movieapp.ui.base.BaseInteractionListener
import com.karrar.movieapp.ui.home.HomeInteractionListener
import com.karrar.movieapp.ui.home.HomeItem
import com.karrar.movieapp.ui.models.MediaUiState
import com.karrar.movieapp.ui.myList.CreatedListAdapter
import com.karrar.movieapp.ui.myList.CreatedListInteractionListener
import com.karrar.movieapp.ui.profile.watchhistory.WatchHistoryInteractionListener
import com.karrar.movieapp.utilities.Constants

class HomeAdapter(
    private var homeItems: MutableList<HomeItem>,
    private val listener: BaseInteractionListener,
) : BaseAdapter<HomeItem>(homeItems, listener) {
    override val layoutID: Int = 0

    fun setItem(item: HomeItem) {
        val newItems = homeItems.apply {
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
        if (homeItems.isNotEmpty())
            bind(holder as ItemViewHolder, position)
    }

    override fun bind(holder: ItemViewHolder, position: Int) {
        if (position != -1)
            when (val currentItem = homeItems[position]) {
                is HomeItem.Slider -> {
                    holder.binding.setVariable(
                        BR.adapterRecycler,
                        PopularMovieAdapter(currentItem.items, listener as HomeInteractionListener)
                    )
                }

                is HomeItem.TvShows -> {
                    holder.binding.run {
                        if (currentItem.items.isNotEmpty()) {
                            setVariable(BR.topRated, currentItem.items.first())
                            setVariable(BR.popular, currentItem.items[1])
                            setVariable(BR.latest, currentItem.items.last())
                            setVariable(BR.listener, listener as TVShowInteractionListener)
                        }
                    }
                }

                is HomeItem.Actor -> {
                    holder.binding.run {
                        setVariable(
                            BR.adapterRecycler, ActorAdapter(
                                currentItem.items,
                                R.layout.item_actor_home,
                                listener as ActorsInteractionListener
                            )
                        )
                        setVariable(BR.listener, listener as HomeInteractionListener)
                    }

                }

                is HomeItem.AiringToday -> {
                    holder.binding.run {
                        setVariable(
                            BR.adapterRecycler,
                            MediaAdapter(
                                currentItem.items.take(Constants.MAX_NUMBER_AIRING_TODAY),
                                R.layout.item_airing_today,
                                listener as MediaInteractionListener
                            )
                        )
                        setVariable(BR.count, currentItem.items.size)
                    }
                }

                is HomeItem.Adventure -> {
                    bindMovie(holder, currentItem.items, currentItem.type)
                }

                is HomeItem.Mystery -> {
                    bindMovie(holder, currentItem.items, currentItem.type)
                }

                is HomeItem.NowStreaming -> {
                    bindMovie(holder, currentItem.items, currentItem.type)
                }

                is HomeItem.OnTheAiring -> {
                    holder.binding.run {
                        setVariable(
                            BR.adapterRecycler,
                            TVShowAdapter(currentItem.items, listener as TVShowInteractionListener)
                        )
                        setVariable(BR.movieType, currentItem.type)
                    }
                }

                is HomeItem.Trending -> {
                    bindMovie(holder, currentItem.items, currentItem.type)
                }

                is HomeItem.Upcoming -> {
                    bindMovie(holder, currentItem.items, currentItem.type)
                }

                is HomeItem.RecentlyViewed -> {
                    holder.binding.run {
                        setVariable(
                            BR.adapterRecycler, RecentlyViewedAdapter(
                                currentItem.items,
                                listener as WatchHistoryInteractionListener
                            )
                        )
                        setVariable(BR.listener, listener as HomeInteractionListener)
                        setVariable(BR.isVisible, currentItem.items.isNotEmpty())
                    }
                }

                is HomeItem.Collections -> {
                    holder.binding.run {
                        setVariable(
                            BR.adapterRecycler, CreatedListAdapter(
                                currentItem.items,
                                listener as CreatedListInteractionListener,
                                isFullWidth = true
                            )
                        )
                        setVariable(BR.listener, listener as HomeInteractionListener)
                        setVariable(BR.isVisible, currentItem.items.isNotEmpty())
                    }
                }

                is HomeItem.WhatShouldIWatch -> {
                    holder.binding.setVariable(BR.listener, listener as HomeInteractionListener)
                }

                is HomeItem.NeedMoreToWatch -> {
                    holder.binding.setVariable(BR.listener, listener as HomeInteractionListener)
                }

                is HomeItem.TopRatedMovie -> {
                    bindSeries(holder , currentItem.items,currentItem.type)
                }

                is HomeItem.MatchYourVibe -> {
                    bindMovie(holder, currentItem.items , currentItem.type)
                }
            }
    }

    private fun bindMovie(holder: ItemViewHolder, items: List<MediaUiState>, type: HomeItemsType) {

        holder.binding.run {
            setVariable(
                BR.adapterRecycler,
                MovieAdapter(items, listener as MovieInteractionListener)
            )
            setVariable(BR.movieType, type)
        }


    }

    private fun bindSeries(holder: ItemViewHolder, items: List<MediaUiState>, type: HomeItemsType) {

        holder.binding.setVariable(
            BR.adapterRecycler,
            SeriesAdapter(items, listener as SeriesInteractionListener)
        )
        holder.binding.setVariable(BR.movieType, type)


    }

    override fun setItems(newItems: List<HomeItem>) {
        homeItems = newItems.sortedBy { it.priority }.toMutableList()
        super.setItems(homeItems)
    }

    override fun areItemsSame(oldItem: HomeItem, newItem: HomeItem): Boolean {
        return oldItem.priority == newItem.priority
    }

    override fun areContentSame(
        oldPosition: HomeItem,
        newPosition: HomeItem,
    ): Boolean {
        return oldPosition == newPosition
    }

    override fun getItemViewType(position: Int): Int {
        if (homeItems.isNotEmpty()) {
            return when (homeItems[position]) {
                is HomeItem.Actor -> R.layout.list_actor
                is HomeItem.TvShows -> R.layout.list_tv_shows
                is HomeItem.Slider -> R.layout.list_popular
                is HomeItem.AiringToday -> R.layout.list_airing_today
                is HomeItem.OnTheAiring -> R.layout.list_tvshow
                is HomeItem.WhatShouldIWatch -> R.layout.what_should_i_watch
                is HomeItem.NeedMoreToWatch -> R.layout.need_more_to_watch
                is HomeItem.RecentlyViewed -> R.layout.list_recently_viewed
                is HomeItem.Adventure,
                is HomeItem.Mystery,
                is HomeItem.NowStreaming,
                is HomeItem.Trending,
                is HomeItem.Upcoming,
                    -> R.layout.list_movie

                is HomeItem.Collections -> R.layout.list_home_collections
                is HomeItem.TopRatedMovie -> R.layout.list_series
                is HomeItem.MatchYourVibe -> R.layout.list_movie
            }
        }
        return -1
    }

}