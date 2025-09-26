package com.karrar.movieapp.domain.mappers

interface NormalMapper<I, O>: Mapper<I, O> {
     override fun map(input: I): O
}