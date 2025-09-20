package com.karrar.movieapp.domain.mappers

import com.karrar.movieapp.domain.models.Genre

interface MapperWithGenreList<I, O> : NormalMapper<I, O> {
    fun map(input: I, genreList: List<Genre>): O
    override fun map(input: I): O = map(input, emptyList())
}