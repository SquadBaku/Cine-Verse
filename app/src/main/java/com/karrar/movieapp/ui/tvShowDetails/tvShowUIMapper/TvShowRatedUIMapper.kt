package com.karrar.movieapp.ui.tvShowDetails.tvShowUIMapper

import com.karrar.movieapp.domain.mappers.Mapper
import com.karrar.movieapp.domain.models.Rated
import com.karrar.movieapp.ui.profile.myratings.RatedUIState
import java.util.Locale
import javax.inject.Inject

class TvShowRatedUIMapper @Inject constructor() : Mapper<Rated, RatedUIState> {
    override fun map(input: Rated): RatedUIState {
        return RatedUIState(
            id = input.id,
            rating = String.format(Locale.ENGLISH,"%.1f", input.rating),
            userRating = input.userRating,
            title = input.title,
            posterPath = input.posterPath,
            mediaType = input.mediaType,
            releaseDate = input.releaseDate,
            duration = input.duration,
            category = input.categoryIdList.toString()

        )
    }
}
