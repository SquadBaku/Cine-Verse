package com.karrar.movieapp.ui.actorDetails.actorGallery

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.karrar.movieapp.databinding.ItemGalleryBinding

class ActorGalleryAdapter(
    private var images: List<List<String>> = emptyList()
) : RecyclerView.Adapter<ActorGalleryAdapter.ActorGalleryViewHolder>() {

    fun submitList(newImages: List<String>) {
        images = newImages.chunked(3)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActorGalleryViewHolder {
        val binding = ItemGalleryBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ActorGalleryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ActorGalleryViewHolder, position: Int) {
        holder.bind(images[position])
    }

    override fun getItemCount() = images.size

    class ActorGalleryViewHolder(private val binding: ItemGalleryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(images: List<String>) {
            if (images.isNotEmpty()) {
                Glide.with(binding.root.context)
                    .load(images[0])
                    .into(binding.actorImage1)
            } else {
                binding.actorImage1.setImageDrawable(null)
            }

            if (images.size > 1) {
                Glide.with(binding.root.context)
                    .load(images[1])
                    .into(binding.actorImage2)
            } else {
                binding.actorImage2.setImageDrawable(null)
            }

            if (images.size > 2) {
                Glide.with(binding.root.context)
                    .load(images[2])
                    .into(binding.actorImage3)
            } else {
                binding.actorImage3.setImageDrawable(null)
            }
        }
    }
}
