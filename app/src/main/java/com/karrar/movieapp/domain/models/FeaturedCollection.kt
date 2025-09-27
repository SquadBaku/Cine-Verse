package com.karrar.movieapp.domain.models

import com.karrar.movieapp.domain.enums.MediaType


data class FeaturedCollection(
    val id: String,
    val title: String,
    val imageRes: Int,
    val genres: Set<Genre>,
    val mediaTypes: Set<MediaType> = setOf(MediaType.MOVIE, MediaType.TV_SHOW)
)