package com.karrar.movieapp.data.remote.response.tvShow

import com.google.gson.annotations.SerializedName

data class EpisodeDetailsDto(
    @SerializedName("runtime")
    val runtime: Int? = null
)