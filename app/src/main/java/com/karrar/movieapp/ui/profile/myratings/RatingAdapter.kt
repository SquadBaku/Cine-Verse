package com.karrar.movieapp.ui.profile.myratings

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.karrar.movieapp.R
import com.karrar.movieapp.databinding.ItemImageViewBinding

class RatingAdapter(private val rating: Int) :
    RecyclerView.Adapter<RatingAdapter.RatingViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): RatingViewHolder {
        val binding = ItemImageViewBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return RatingViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: RatingViewHolder,
        position: Int,
    ) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return 5
    }

    inner class RatingViewHolder(private val binding: ItemImageViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            with(binding) {
                if (position <= rating) {
                    root.setImageResource(R.drawable.selected_star_icon)
                } else {
                    root.setImageResource(R.drawable.unselected_star_icon)
                }
            }
        }
    }
}