package com.karrar.movieapp.ui.featuredCollection

import android.R.attr.onClick
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.karrar.movieapp.databinding.FeaturedCollectionsItemsBinding


class FeaturedCollectionsAdapter(
    private val items: List<FeaturedItem>
) : RecyclerView.Adapter<FeaturedCollectionsAdapter.FeaturedViewHolder>() {

    inner class FeaturedViewHolder(private val binding: FeaturedCollectionsItemsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: FeaturedItem) {
            binding.imagePoster1.setImageResource(item.imageRes)
            binding.textTitle1.text = item.title


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeaturedViewHolder {
        val binding = FeaturedCollectionsItemsBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return FeaturedViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FeaturedViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}