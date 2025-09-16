package com.karrar.movieapp.ui.actorDetails.actorGallery

import com.karrar.movieapp.utilities.Event

data class ActorGalleryUIState(
    val actorImages: List<String> = emptyList(),
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null,
    val backEvent: Event<Boolean>? = null
)