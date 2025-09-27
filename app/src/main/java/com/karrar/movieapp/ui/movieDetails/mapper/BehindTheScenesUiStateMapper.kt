package com.karrar.movieapp.ui.movieDetails.mapper

import com.karrar.movieapp.domain.mappers.Mapper
import com.karrar.movieapp.domain.models.CrewInfo
import com.karrar.movieapp.ui.movieDetails.movieDetailsUIState.BehindTheScenesState
import javax.inject.Inject

class BehindTheScenesUiStateMapper @Inject constructor() : Mapper<List<CrewInfo>, BehindTheScenesState> {
    override fun map(input: List<CrewInfo>): BehindTheScenesState {
        return BehindTheScenesState(
            characters = input.filter { it.job == "Characters" }.joinToString(", ") { it.name },
            director =

                input.filter { it.job in (listOf("Director", "Screenplay", "Story")) }
                    .joinToString(", ") { it.name },
            producer = input.filter { it.job == "Producer" }.joinToString(", ") { it.name },
            writer = input.filter { it.job == "Writer" }.joinToString(", ") { it.name },
        )
    }
}