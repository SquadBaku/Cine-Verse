package com.karrar.movieapp.domain.mappers

import com.karrar.movieapp.data.remote.response.CrewDetailsDto
import com.karrar.movieapp.domain.models.CrewInfo
import javax.inject.Inject

class CrewInfoMapper @Inject constructor(): Mapper<CrewDetailsDto, CrewInfo> {
    override fun map(input: CrewDetailsDto): CrewInfo {
        return CrewInfo(
            id = input.id ?: 0,
            name = input.name ?: "",
            job = input.job ?: "",
        )
    }
}