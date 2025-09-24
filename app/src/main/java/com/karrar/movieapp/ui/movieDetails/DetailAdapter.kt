package com.karrar.movieapp.ui.movieDetails

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import coil.load
import com.karrar.movieapp.BR
import com.karrar.movieapp.R
import com.karrar.movieapp.ui.adapters.*
import com.karrar.movieapp.ui.base.BaseAdapter
import com.karrar.movieapp.ui.base.BaseInteractionListener
import com.karrar.movieapp.ui.movieDetails.movieDetailsUIState.DetailItemUIState
import com.karrar.movieapp.utilities.SingleActionListener

class DetailAdapter(
    private var items: List<DetailItemUIState>,
    private val listener: BaseInteractionListener,
) : BaseAdapter<DetailItemUIState>(items, listener) {
    override val layoutID: Int = 0
    companion object {
        @JvmStatic
        @BindingAdapter("imageUrl")
        fun setImageUrl(view: ImageView, url: String?) {
            view.load(url) {
                placeholder(R.drawable.image)
                error(R.drawable.image)
                crossfade(true)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return ItemViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context), viewType, parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        bind(holder as ItemViewHolder, position)
    }

    override fun bind(holder: ItemViewHolder, position: Int) {
        when (val currentItem = items[position]) {
            is DetailItemUIState.Poster -> {
                holder.binding.run {
                    setVariable(BR.item, currentItem.data)
                    setVariable(BR.listener, listener as DetailInteractionListener)
                }
            }
            is DetailItemUIState.Header -> {
                holder.binding.run {
                    setVariable(BR.item, currentItem.data)
                    setVariable(BR.listener, listener as DetailInteractionListener)
                }
            }
//            is DetailItemUIState.MediaInfo ->{
//                holder.binding.run {
//                    setVariable(BR.item, currentItem.data)
//                    setVariable(BR.listener, listener as DetailInteractionListener)
//                }
//            }
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

            is DetailItemUIState.Promotion -> {
                holder.binding.setVariable(
                    BR.listener,
                    listener as SingleActionListener
                )
            }

            is DetailItemUIState.SimilarMovies -> {
                holder.binding.run {
                    setVariable(
                        BR.adapterRecycler,
                        MovieAdapter(currentItem.data, listener as MovieInteractionListener)
                    )
                }
            }

            is DetailItemUIState.Rating -> {
                holder.binding.run {
                    setVariable(BR.viewModel, currentItem.viewModel)
                }
            }

            is DetailItemUIState.Comment -> {
                holder.binding.run {
                    setVariable(BR.item, currentItem.data)
                    setVariable(BR.listener, listener)
                }
            }

            is DetailItemUIState.ReviewText -> {}
            DetailItemUIState.SeeAllReviewsButton -> {
                holder.binding.run {
                    setVariable(BR.listener, listener as DetailInteractionListener)
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
            is DetailItemUIState.Cast -> R.layout.list_cast
            is DetailItemUIState.SimilarMovies -> R.layout.list_similar_movie
            is DetailItemUIState.Promotion -> R.layout.promotion_card
            is DetailItemUIState.Rating -> R.layout.item_rating
            is DetailItemUIState.Comment -> R.layout.item_movie_review
            is DetailItemUIState.ReviewText -> R.layout.item_review_text
            DetailItemUIState.SeeAllReviewsButton -> R.layout.item_see_all_reviews
        }
    }

}


