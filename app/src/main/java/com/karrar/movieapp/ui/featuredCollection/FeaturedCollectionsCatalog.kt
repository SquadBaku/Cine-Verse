package com.karrar.movieapp.ui.featuredCollection

import com.karrar.movieapp.R
import com.karrar.movieapp.domain.enums.MediaType
import com.karrar.movieapp.domain.models.FeaturedCollection
import com.karrar.movieapp.domain.models.Genre

object FeaturedCollectionsCatalog {

    val all: List<FeaturedCollection> = listOf(
        FeaturedCollection(
            id = "late_night_thrills",
            title = "Late-Night Thrills",
            imageRes = R.drawable.late_night_thrills,
            genres = setOf(
                Genre(1, "Horror"),
                Genre(2, "Thriller")
            ),
            mediaTypes = setOf(MediaType.MOVIE, MediaType.TV_SHOW)
        ),
        FeaturedCollection(
            id = "family_night",
            title = "Family Night Picks",
            imageRes = R.drawable.family_night_picks,
            genres = setOf(
                Genre(3, "Family"),
                Genre(4, "Comedy"),
                Genre(5, "Animation")
            ),
            mediaTypes = setOf(MediaType.MOVIE, MediaType.TV_SHOW)
        ),
        FeaturedCollection(
            id = "cinematic_masterpieces",
            title = "Cinematic Masterpieces",
            imageRes = R.drawable.cinematic_masterpieces,
            genres = setOf(
                Genre(6, "Drama")
            ),
            mediaTypes = setOf(MediaType.MOVIE, MediaType.TV_SHOW)
        ),
        FeaturedCollection(
            id = "based_on_true_events",
            title = "Based on True Events",
            imageRes = R.drawable.based_on_true_events,
            genres = setOf(
                Genre(7, "Drama"),
                Genre(8, "Crime")
            ),
            mediaTypes = setOf(MediaType.MOVIE, MediaType.TV_SHOW)
        ),
        FeaturedCollection(
            id = "feel_good",
            title = "Feel-Good Favorites",
            imageRes = R.drawable.feel_good_favorites,
            genres = setOf(
                Genre(9, "Comedy"),
                Genre(10, "Romance")
            ),
            mediaTypes = setOf(MediaType.MOVIE, MediaType.TV_SHOW)
        ),
        FeaturedCollection(
            id = "mind_bending",
            title = "Mind-Bending Stories",
            imageRes = R.drawable.mind_bending_stories,
            genres = setOf(
                Genre(11, "Science Fiction"),
                Genre(12, "Mystery")
            ),
            mediaTypes = setOf(MediaType.MOVIE, MediaType.TV_SHOW)
        )
    )
}