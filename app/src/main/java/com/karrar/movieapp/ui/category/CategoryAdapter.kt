package com.karrar.movieapp.ui.category

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.karrar.movieapp.R
import com.karrar.movieapp.databinding.ItemExploreGridBinding
import com.karrar.movieapp.databinding.ItemGenersListBinding
import com.karrar.movieapp.ui.adapters.MediaInteractionListener
import com.karrar.movieapp.ui.category.uiState.GenreUIState
import com.karrar.movieapp.ui.search.mediaSearchUIState.MediaUIState

class CategoryAdapter(
    private val listener: MediaInteractionListener,
    private val categoryListener: CategoryInteractionListener
) : PagingDataAdapter<MediaUIState, RecyclerView.ViewHolder>(MediaComparator) {

    private var genres: List<GenreUIState> = emptyList()
    private var selectedCategoryId: Int = -1

    companion object {
        private const val VIEW_TYPE_GENRES = 0
        private const val VIEW_TYPE_MEDIA = 1
    }

    fun setGenres(genres: List<GenreUIState>, selectedId: Int) {
        this.genres = genres
        this.selectedCategoryId = selectedId
        notifyItemChanged(0) // refresh genres row
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) VIEW_TYPE_GENRES else VIEW_TYPE_MEDIA
    }

    override fun getItemCount(): Int {
        // +1 for the genres row
        return super.getItemCount() + 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
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
            else -> {
                val binding: ItemExploreGridBinding = DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.item_explore_grid,
                    parent,
                    false
                )
                MediaViewHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is GenresViewHolder) {
            holder.bind(genres, selectedCategoryId, categoryListener)
        } else if (holder is MediaViewHolder) {
            val item = getItem(position - 1) // shift by 1 because first item is genres
            if (item != null) holder.bind(item, listener)
        }
    }

    inner class GenresViewHolder(
        private val binding: ItemGenersListBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(
            genres: List<GenreUIState>,
            selectedId: Int,
            listener: CategoryInteractionListener
        ) {
            binding.viewModel = null // prevent old binding
            val adapter = GenreAdapter(listener)
            binding.recyclerGenres.adapter = adapter
            adapter.submitList(genres, selectedId)
        }
    }

    inner class MediaViewHolder(
        private val binding: ItemExploreGridBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: MediaUIState, listener: MediaInteractionListener) {
            binding.item = item
            binding.listener = listener
            binding.executePendingBindings()
        }
    }

    object MediaComparator : DiffUtil.ItemCallback<MediaUIState>() {
        override fun areItemsTheSame(oldItem: MediaUIState, newItem: MediaUIState) =
            oldItem.mediaID == newItem.mediaID

        override fun areContentsTheSame(oldItem: MediaUIState, newItem: MediaUIState) =
            oldItem == newItem
    }
}
