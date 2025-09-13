package com.karrar.movieapp.ui.profile.myratings

import com.karrar.movieapp.domain.mappers.MapperWithGenreList
import com.karrar.movieapp.domain.models.Genre
import com.karrar.movieapp.domain.models.Rated
import javax.inject.Inject

class RatedUIStateMapper @Inject constructor()
    : MapperWithGenreList<Rated, RatedUIState> {

    override fun map(input: Rated, categoryIdList: List<Genre>): RatedUIState {
        val categoryNames = input.categoryIdList
            .mapNotNull { id -> categoryIdList.firstOrNull { it.genreID == id }?.genreName }
        return RatedUIState(
            id = input.id,
            title = input.title,
            posterPath = input.posterPath,
            rating = input.rating.toString(),
            mediaType = input.mediaType,
            releaseDate = TimeFormatters.formatDate(input.releaseDate),
            category = categoryNames.joinToString(", "),
            duration = input.duration
        )
    }
}