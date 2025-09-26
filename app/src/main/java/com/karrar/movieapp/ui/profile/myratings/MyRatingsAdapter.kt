package com.karrar.movieapp.ui.profile.myratings

import com.karrar.movieapp.BR
import com.karrar.movieapp.R
import com.karrar.movieapp.domain.models.Rated
import com.karrar.movieapp.ui.base.BaseAdapter
import com.karrar.movieapp.ui.base.BaseInteractionListener

class RatedMoviesAdapter(items: List<RatedUIState>, listener: RatedMoviesInteractionListener) :
    BaseAdapter<RatedUIState>(items, listener) {
    override val layoutID: Int = R.layout.item_rated_movie
    override fun bind(holder: ItemViewHolder, position: Int) {
        super.bind(holder, position)
        val adapter = RatingAdapter(getItems()[position].userRating)
        holder.binding.setVariable(BR.adapter, adapter)


    }
}

interface RatedMoviesInteractionListener : BaseInteractionListener {
    fun onClickMovie(movieId: Int)
}