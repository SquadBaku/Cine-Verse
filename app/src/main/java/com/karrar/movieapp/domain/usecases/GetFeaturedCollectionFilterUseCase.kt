package com.karrar.movieapp.domain.featured

import com.karrar.movieapp.domain.models.FeaturedCollection

class GetFeaturedCollectionFilterUseCase {
    fun invoke(collection: FeaturedCollection): FeaturedFilterPayload {
        return FeaturedFilterPayload(
            collectionId = collection.id,
            genreIds = collection.genres.map { it.genreID },
            mediaTypes = collection.mediaTypes.map { it.name }
        )
    }
}

data class FeaturedFilterPayload(
    val collectionId: String,
    val genreIds: List<Int>,
    val mediaTypes: List<String>
)