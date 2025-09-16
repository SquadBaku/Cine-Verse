package com.karrar.movieapp.domain.models

data class Season(
    val seasonId: Int = 0,
    val imageUrl: String = "",
    val seasonName: String = "",
    val seasonYear: String = "",
    val seasonNumber: Int = 0,
    val episodeCount: Int = 0,
    val rating: Float = 0F,
    val seasonDescription: String = "",
    val episodes: List<Episode> = emptyList(),
)