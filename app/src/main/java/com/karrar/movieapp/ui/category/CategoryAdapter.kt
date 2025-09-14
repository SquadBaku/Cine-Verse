package com.karrar.movieapp.ui.category

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import com.karrar.movieapp.R
import com.karrar.movieapp.databinding.ItemGenersListBinding
import com.karrar.movieapp.ui.adapters.MediaInteractionListener
import com.karrar.movieapp.ui.base.BasePagingAdapter
import com.karrar.movieapp.ui.category.uiState.GenreUIState
import com.karrar.movieapp.ui.search.mediaSearchUIState.MediaUIState


class CategoryAdapter(
    listener: MediaInteractionListener,
    private val categoryListener: CategoryInteractionListener,
) : BasePagingAdapter<MediaUIState>(MediaComparator, listener) {

    override val layoutID: Int = R.layout.item_explore_grid

    private var genres: List<GenreUIState> = emptyList()
    private var selectedCategoryId: Int = -1

    companion object {
         const val VIEW_TYPE_GENRES = 0
         const val VIEW_TYPE_MEDIA = 1
    }

    fun setGenres(genres: List<GenreUIState>, selectedId: Int) {
        this.genres = genres
        this.selectedCategoryId = selectedId
        notifyItemChanged(0)
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) VIEW_TYPE_GENRES else VIEW_TYPE_MEDIA
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return when (viewType) {
            VIEW_TYPE_GENRES -> {
                val binding: ItemGenersListBinding = DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.item_geners_list,
                    parent,
                    false
                )
                GenresViewHolder(binding)
            }
            else -> super.onCreateViewHolder(parent, viewType)
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        when (holder) {
            is GenresViewHolder -> holder.bind(genres, selectedCategoryId, categoryListener)
            is ItemViewHolder -> {
                val item = getItem(position - 1)
                if (item != null) bind(item, holder)
            }
        }
    }

    inner class GenresViewHolder(
        private val binding: ItemGenersListBinding
    ) : BaseViewHolder(binding) {
        fun bind(genres: List<GenreUIState>, selectedId: Int, listener: CategoryInteractionListener) {
            val adapter = GenreAdapter(listener)
            binding.recyclerGenres.adapter = adapter
            adapter.submitList(genres, selectedId)
        }
    }

    object MediaComparator : DiffUtil.ItemCallback<MediaUIState>() {
        override fun areItemsTheSame(oldItem: MediaUIState, newItem: MediaUIState) =
            oldItem.mediaID == newItem.mediaID

        override fun areContentsTheSame(oldItem: MediaUIState, newItem: MediaUIState) =
            oldItem == newItem
    }
}
