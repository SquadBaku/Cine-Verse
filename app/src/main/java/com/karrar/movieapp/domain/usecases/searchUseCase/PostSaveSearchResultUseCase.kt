package com.karrar.movieapp.domain.usecases.searchUseCase

import com.karrar.movieapp.data.local.database.entity.SearchHistoryEntity
import com.karrar.movieapp.data.repository.MovieRepository
import com.karrar.movieapp.ui.search.mediaSearchUIState.MediaTypes
import javax.inject.Inject


class PostSaveSearchResultUseCase @Inject constructor(
    private val movieRepository: MovieRepository
    ) {
    suspend operator fun invoke(id: Int, name: String,mediaType: String) {
        movieRepository.insertSearchItem(
            SearchHistoryEntity(
                id = id.toLong(),
                search = name,
                mediaType = mediaType
            )
        )
    }
}