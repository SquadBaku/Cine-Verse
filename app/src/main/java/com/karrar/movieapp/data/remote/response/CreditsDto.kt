package com.karrar.movieapp.data.remote.response


import com.google.gson.annotations.SerializedName
import com.karrar.movieapp.data.remote.response.actor.ActorDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

data class CreditsDto(
    @SerializedName("cast")
    val cast: List<ActorDto>? = listOf(),
    @SerializedName("crew")
    val crew: List<CrewDetailsDto>? = listOf(),
    @SerializedName("id")
    val id: Int? = 0,
)

@Serializable
data class CrewDetailsDto(
    @SerialName("adult")
    val adult: Boolean?,
    @SerialName("credit_id")
    val creditId: String?,
    @SerialName("department")
    val department: String?,
    @SerialName("gender")
    val gender: Int?,
    @SerialName("id")
    val id: Int?,
    @SerialName("job")
    val job: String?,
    @SerialName("known_for_department")
    val knownForDepartment: String?,
    @SerialName("name")
    val name: String?,
    @SerialName("original_name")
    val originalName: String?,
    @SerialName("popularity")
    val popularity: Double?,
    @SerialName("profile_path")
    val profilePath: String?,
)