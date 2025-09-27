package com.karrar.movieapp.ui.tvShowDetails

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.karrar.movieapp.BR
import com.karrar.movieapp.R
import com.karrar.movieapp.ui.adapters.*
import com.karrar.movieapp.ui.base.BaseAdapter
import com.karrar.movieapp.ui.base.BaseInteractionListener
import com.karrar.movieapp.ui.movieDetails.DetailInteractionListener
import com.karrar.movieapp.ui.tvShowDetails.tvShowUIState.DetailItemUIState

class DetailUIStateAdapter(
    private var items: List<DetailItemUIState>,
    private val listener: BaseInteractionListener,
) : BaseAdapter<DetailItemUIState>(items, listener) {
    override val layoutID: Int = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding: ViewDataBinding =
            DataBindingUtil.inflate(inflater, viewType, parent, /* attachToParent = */ false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        bind(holder as ItemViewHolder, position)
    }

    override fun bind(holder: ItemViewHolder, position: Int) {
        when (val currentItem = items[position]) {
            is DetailItemUIState.Header -> {
                holder.binding.run {
                    setVariable(BR.item, currentItem.data)
                    setVariable(BR.listener, listener as DetailInteractionListener)
                }
            }

            is DetailItemUIState.Cast -> {
                holder.binding.run {
                    setVariable(
                        BR.adapterRecycler,
                        ActorAdapter(
                            currentItem.data.take(currentItem.data.size / 2),
                            R.layout.new_cast_item,
                            listener as ActorsInteractionListener
                        )
                    )

                    setVariable(
                        BR.secondListAdapter,
                        ActorAdapter(
                            currentItem.data.drop(currentItem.data.size / 2),
                            R.layout.new_cast_item,
                            listener
                        )
                    )
                }
            }

            is DetailItemUIState.Seasons -> {
                holder.binding.run {
                    setVariable(
                        BR.adapterRecycler,
                        SeasonAdapterUIState(
                            currentItem.data,
                            listener as SeasonInteractionListener
                        )
                    )
                }
            }

            is DetailItemUIState.Comment -> {
                holder.binding.run {
                    setVariable(BR.item, currentItem.data)
                    setVariable(BR.listener, listener)
                }
            }

            is DetailItemUIState.ReviewText -> {
                holder.binding.setVariable(BR.listener, listener as DetailInteractionListener)
            }

            is DetailItemUIState.Promotion -> {
                holder.binding.run {
                    setVariable(BR.listener, listener)
                }
            }

            is DetailItemUIState.BehindScenes -> {
                holder.binding.run {
                    setVariable(BR.item, currentItem.data)
                }
            }

            is DetailItemUIState.Poster -> {
                Log.d("DetailUIStateAdapter", "Binding Poster at position $position")
                holder.binding.run {
                    setVariable(BR.item, currentItem.data)
                    setVariable(BR.listener, listener as DetailInteractionListener)
                }
            }

            is DetailItemUIState.SimilarTvShows -> {
                holder.binding.run {
                    setVariable(
                        BR.adapterRecycler,
                        MovieAdapter(currentItem.data, listener as MovieInteractionListener)
                    )
                }
            }
        }

    }

    override fun setItems(newItems: List<DetailItemUIState>) {
        items = newItems.sortedBy { it.priority }
        super.setItems(items)
    }

    override fun areItemsSame(oldItem: DetailItemUIState, newItem: DetailItemUIState): Boolean {
        return oldItem.priority == newItem.priority
    }

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is DetailItemUIState.Poster -> R.layout.poster
            is DetailItemUIState.Header -> R.layout.media_card
            is DetailItemUIState.Seasons -> R.layout.list_season
            is DetailItemUIState.Cast -> R.layout.list_cast
            is DetailItemUIState.BehindScenes -> R.layout.item_behind_the_scenes
            is DetailItemUIState.SimilarTvShows -> R.layout.list_similar_movie
            is DetailItemUIState.Promotion -> R.layout.promotion_card
            is DetailItemUIState.Comment -> R.layout.item_tvshow_review
            is DetailItemUIState.ReviewText -> R.layout.item_review_text
        }
    }
}
