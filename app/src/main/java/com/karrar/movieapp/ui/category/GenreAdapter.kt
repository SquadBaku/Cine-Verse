package com.karrar.movieapp.ui.category

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.karrar.movieapp.R
import com.karrar.movieapp.databinding.ItemGenerBinding
import com.karrar.movieapp.ui.category.uiState.GenreUIState

class GenreAdapter(
    private val listener: CategoryInteractionListener
) : RecyclerView.Adapter<GenreAdapter.GenreViewHolder>() {

    private val genres = mutableListOf<GenreUIState>()
    private var selectedId: Int = -1

    fun submitList(list: List<GenreUIState>, selected: Int = -1) {
        genres.clear()
        genres.addAll(list)
        selectedId = selected
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenreViewHolder {
        val binding: ItemGenerBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_gener,
            parent,
            false
        )
        return GenreViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GenreViewHolder, position: Int) {
        holder.bind(genres[position])
    }

    override fun getItemCount() = genres.size

    inner class GenreViewHolder(private val binding: ItemGenerBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: GenreUIState) {
            binding.genre = item
            binding.listener = listener

            binding.tvGenre.isSelected = item.genreID == selectedId

            binding.executePendingBindings()
        }
    }
}
