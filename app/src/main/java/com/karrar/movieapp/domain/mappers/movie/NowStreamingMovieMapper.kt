package com.karrar.movieapp.domain.mappers.movie

import com.karrar.movieapp.BuildConfig
import com.karrar.movieapp.data.local.database.entity.movie.NowStreamingMovieEntity
import com.karrar.movieapp.domain.enums.MediaType
import com.karrar.movieapp.domain.mappers.Mapper
import com.karrar.movieapp.domain.models.Media
import javax.inject.Inject

class NowStreamingMovieMapper @Inject constructor() : Mapper<NowStreamingMovieEntity, Media> {
    override fun map(input: NowStreamingMovieEntity): Media {
        return Media(
            mediaID = input.id,
            mediaName = input.name,
            mediaImage = BuildConfig.IMAGE_BASE_PATH + input.imageUrl,
            mediaRate = input.rate,
            mediaDate = "",
            mediaType = MediaType.MOVIE.value,
        )
    }
}