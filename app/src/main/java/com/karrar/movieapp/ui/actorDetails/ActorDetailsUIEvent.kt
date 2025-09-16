package com.karrar.movieapp.ui.actorDetails

sealed interface ActorDetailsUIEvent {
    object BackEvent : ActorDetailsUIEvent
    object SeeAllMovies : ActorDetailsUIEvent

    object SeeActorGallery : ActorDetailsUIEvent

    data class ClickMovieEvent(val movieID: Int) : ActorDetailsUIEvent
    data class OpenSocialMediaLink(val link: String) : ActorDetailsUIEvent
}