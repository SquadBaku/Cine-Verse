package com.karrar.movieapp.ui.tvShowDetails.tvShowUIState

data class SeasonUIState(
    val imageUrl: String = "",
    val seasonName: String = "",
    val seasonYear: String = "",
    val seasonNumber: Int = 0,
    val rating: Float = 0F,
    val episodeCount: Int = 0,
    val seasonDescription: String = "",
)
